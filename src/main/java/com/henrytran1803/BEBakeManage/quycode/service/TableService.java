package com.henrytran1803.BEBakeManage.quycode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;

import com.henrytran1803.BEBakeManage.quycode.entity.Area;
import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import com.henrytran1803.BEBakeManage.quycode.repository.AreaRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private AreaRepository areaRepository;

    // Lấy danh sách bàn theo ID khu vực
    public ApiResponse<List<Table>> getTablesByAreaId(Long areaId) {
        Optional<Area> areaOptional = areaRepository.findById(areaId);
        if (areaOptional.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
        }

        // Lấy danh sách bàn thuộc khu vực
        List<Table> tables = tableRepository.findAll()
                .stream()
                .filter(table -> table.getArea().getId().equals(areaId))
                .collect(Collectors.toList());

        if (tables.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.TABLE_NOT_FOUND);
        }

        return ApiResponse.Q_success(tables, QuyExeption.SUCCESS);
    }
    // Tạo mới bàn với QR Code
    public ApiResponse<Table> createTable(String name, Long areaId, boolean active) {
        Optional<Area> areaOptional = areaRepository.findById(areaId);
        if (areaOptional.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
        }
        Area area = areaOptional.get();

        // Tạo QR Code nội dung
        String qrContent =
                "http://localhost:3000/home";
        ByteArrayOutputStream qrImage = generateQRCodeImage(qrContent);

        // Lưu QR Code vào Local Storage và lấy URL
        String qrCodeUrl = saveQRCodeLocally(qrImage, name);

        // Tạo mới Table
        Table table = new Table();
        table.setName(name);
        table.setArea(area);
        table.setActive(active);
        table.setQrCodeUrl(qrCodeUrl);

        tableRepository.save(table);
        return ApiResponse.Q_success(table, QuyExeption.SUCCESS);
    }


    // Hàm tạo QR Code dưới dạng hình ảnh
    private ByteArrayOutputStream generateQRCodeImage(String qrContent) {
        // Define the QR code encoding hints
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // Ensure Vietnamese characters are encoded correctly

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream;
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }


    private String saveQRCodeLocally(ByteArrayOutputStream qrImage, String tableName) {
        try {
            // Thư mục lưu hình ảnh
            String uploadDir = "uploads/qrcodes/";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            // Tên file QR Code
            String fileName = tableName + "_qr_" + UUID.randomUUID() + ".png";
            String filePath = uploadDir + fileName;

            // Lưu hình ảnh vào thư mục
            Files.write(Paths.get(filePath), qrImage.toByteArray());

            // Trả về đường dẫn URL của file (truy cập từ máy chủ)
            return "/" + filePath; // Trả về URL dạng tương đối
        } catch (IOException e) {
            throw new RuntimeException("Error saving QR Code locally", e);
        }
    }

    public ApiResponse<Table> updateTable(Long id, String name, Long areaId, boolean active) {
        // Kiểm tra bàn có tồn tại không
        Optional<Table> tableOptional = tableRepository.findById(id);
        if (tableOptional.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.TABLE_NOT_FOUND);
        }

        Table table = tableOptional.get();

        // Nếu areaId được cung cấp, kiểm tra khu vực có tồn tại không
        if (areaId != null) {
            Optional<Area> areaOptional = areaRepository.findById(areaId);
            if (areaOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
            }
            table.setArea(areaOptional.get());
        }

        // Cập nhật thông tin tên và trạng thái bàn
        if (name != null && !name.isBlank()) {
            table.setName(name);
        }
        table.setActive(active);

        // Nếu cần tạo lại QR Code (khi name hoặc areaId thay đổi)
        if (name != null || areaId != null) {
            String qrContent = "Table: " + table.getName() + ", Area: " + table.getArea().getName();
            ByteArrayOutputStream qrImage = generateQRCodeImage(qrContent);
            String qrCodeUrl = saveQRCodeLocally(qrImage, table.getName());
            table.setQrCodeUrl(qrCodeUrl);
        }

        // Lưu lại vào cơ sở dữ liệu
        tableRepository.save(table);
        return ApiResponse.Q_success(table, QuyExeption.SUCCESS);
    }

}



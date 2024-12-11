package com.henrytran1803.BEBakeManage.quycode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private AreaRepository areaRepository;

    private static final String BASE_URL = "http://192.168.2.3:3000";

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

    public ApiResponse<Table> createTable(String name, Long areaId, boolean active) {
        try {
            Optional<Area> areaOptional = areaRepository.findById(areaId);
            if (areaOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
            }
            Area area = areaOptional.get();

            Table table = new Table();
            table.setName(name);
            table.setArea(area);
            table.setActive(active);

            try {
                String tempQrPath = null;

                // Tạo một temporary ID cho QR code
                String tempId = UUID.randomUUID().toString();
                String tableInfo = String.format("TABLE_ID:%s|URL:%s", tempId, BASE_URL);
                String qrContent = String.format("%s?qrdata=%s",
                        BASE_URL,
                        URLEncoder.encode(tableInfo, StandardCharsets.UTF_8));

                // Tạo QR code image tạm
                ByteArrayOutputStream qrImage = generateQRCodeImage(qrContent);
                if (qrImage == null) {
                    throw new RuntimeException("Failed to generate QR code image");
                }

                // Lưu QR code tạm và lấy URL
                String qrCodeUrl = saveQRCodeLocally(qrImage, name + "_temp");
                tempQrPath = qrCodeUrl;
                if (qrCodeUrl == null || qrCodeUrl.isEmpty()) {
                    throw new RuntimeException("Failed to save QR code image");
                }

                table.setQrCodeUrl(qrCodeUrl);
                table = tableRepository.save(table);

                // Tạo QR code mới với ID thật
                tableInfo = String.format("TABLE_ID:%s|URL:%s", table.getId(), BASE_URL);
                qrContent = String.format("%s?qrdata=%s",
                        BASE_URL,
                        URLEncoder.encode(tableInfo, StandardCharsets.UTF_8));

                qrImage = generateQRCodeImage(qrContent);
                qrCodeUrl = saveQRCodeLocally(qrImage, name);

                if (tempQrPath != null) {
                    deleteQRCodeFile(tempQrPath);
                }

                table.setQrCodeUrl(qrCodeUrl);
                table = tableRepository.save(table);

                return ApiResponse.Q_success(table, QuyExeption.SUCCESS);
            } catch (Exception e) {
                System.err.println("Error creating QR code: " + e.getMessage());
                return ApiResponse.Q_failure(null, QuyExeption.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error in createTable: " + e.getMessage());
            return ApiResponse.Q_failure(null, QuyExeption.INTERNAL_SERVER_ERROR);
        }
    }


    // Hàm tạo QR Code dưới dạng hình ảnh
    private ByteArrayOutputStream generateQRCodeImage(String qrContent) {
        // Define the QR code encoding hints
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Thêm error correction cao hơn
        hints.put(EncodeHintType.MARGIN, 2); // Margin nhỏ hơn để QR code rõ ràng hơn

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream;
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }


    private String saveQRCodeLocally(ByteArrayOutputStream qrImage, String name) {
        try {
            // Thư mục lưu hình ảnh
            String uploadDir = "uploads/qrcodes/";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                if (!created) {
                    throw new RuntimeException("Could not create directory: " + uploadDir);
                }
            }

            // Tên file QR Code với timestamp
            String fileName = name.replaceAll("\\s+", "_") + "_qr_" + System.currentTimeMillis() + ".png";
            String filePath = uploadDir + fileName;

            // Lưu hình ảnh
            Files.write(Paths.get(filePath), qrImage.toByteArray());

            // Kiểm tra file đã được tạo
            if (!new File(filePath).exists()) {
                throw new RuntimeException("Failed to save QR code file");
            }

            // Trả về đường dẫn URL
            return "/" + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving QR Code: " + e.getMessage());
        }
    }

    // Thêm phương thức xóa file QR
    private void deleteQRCodeFile(String qrCodePath) {
        try {
            // Bỏ dấu "/" ở đầu path nếu có
            if (qrCodePath.startsWith("/")) {
                qrCodePath = qrCodePath.substring(1);
            }

            Path path = Paths.get(qrCodePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Error deleting temporary QR code file: " + e.getMessage());
            // Không throw exception vì đây không phải lỗi nghiêm trọng
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


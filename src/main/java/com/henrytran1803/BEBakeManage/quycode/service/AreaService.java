package com.henrytran1803.BEBakeManage.quycode.service;



import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;

import com.henrytran1803.BEBakeManage.quycode.entity.Area;
import com.henrytran1803.BEBakeManage.quycode.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    // Lấy danh sách tất cả các Area
    public ApiResponse<List<Area>> getAllAreas() {
        List<Area> areas = areaRepository.findAll();
        return ApiResponse.Q_success(areas, QuyExeption.SUCCESS);
    }

    // Lấy Area theo ID
    public ApiResponse<Area> getAreaById(Long id) {
        Optional<Area> area = areaRepository.findById(id);
        return area.map(a -> ApiResponse.Q_success(a, QuyExeption.SUCCESS))
                .orElseGet(() -> ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND));
    }

    // Tạo mới Area
    public ApiResponse<Area> createArea(Area area) {
        Area savedArea = areaRepository.save(area);
        return ApiResponse.Q_success(savedArea, QuyExeption.SUCCESS);
    }

    // Cập nhật Area
    public ApiResponse<Area> updateArea(Long id, Area updatedArea) {
        Optional<Area> existingArea = areaRepository.findById(id);
        if (existingArea.isPresent()) {
            Area area = existingArea.get();
            area.setName(updatedArea.getName());
            area.setDescription(updatedArea.getDescription());
            areaRepository.save(area);
            return ApiResponse.Q_success(area, QuyExeption.SUCCESS);
        }
        return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
    }

    // Xóa Area
    public ApiResponse<Void> deleteArea(Long id) {
        if (areaRepository.existsById(id)) {
            areaRepository.deleteById(id);
            return ApiResponse.Q_success(null, QuyExeption.SUCCESS);
        }
        return ApiResponse.Q_failure(null, QuyExeption.AREA_NOT_FOUND);
    }
}

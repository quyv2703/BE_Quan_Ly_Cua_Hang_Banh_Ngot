package com.henrytran1803.BEBakeManage.quycode.controller;



import com.henrytran1803.BEBakeManage.common.response.ApiResponse;

import com.henrytran1803.BEBakeManage.quycode.entity.Area;
import com.henrytran1803.BEBakeManage.quycode.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Area>>> getAllAreas() {
        ApiResponse<List<Area>> response = areaService.getAllAreas();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response); // HTTP 204 nếu không có dữ liệu
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> getAreaById(@PathVariable Long id) {
        ApiResponse<Area> response = areaService.getAreaById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Area>> createArea(@RequestBody Area area) {
        ApiResponse<Area> response = areaService.createArea(area);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteArea(@PathVariable Long id) {
        ApiResponse<Void> response = areaService.deleteArea(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Area>> updateArea(@PathVariable Long id, @RequestBody Area area) {
        ApiResponse<Area> response = areaService.updateArea(id, area);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu cập nhật thành công
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy khu vực
        }
    }

}



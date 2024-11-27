package com.henrytran1803.BEBakeManage.quycode.controller;


import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import com.henrytran1803.BEBakeManage.quycode.request.TableRequest;
import com.henrytran1803.BEBakeManage.quycode.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping
    public ApiResponse<Table> createTable(@RequestBody TableRequest tableRequest) {
        return tableService.createTable(
                tableRequest.getName(),
                tableRequest.getAreaId(),
                tableRequest.isActive()
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Table> updateTable(
            @PathVariable Long id,
            @RequestBody TableRequest tableRequest) {
        return tableService.updateTable(id, tableRequest.getName(), tableRequest.getAreaId(), tableRequest.isActive());
    }

    // API: Lấy danh sách bàn theo khu vực
    @GetMapping("/by-area")
    public ResponseEntity<ApiResponse<List<Table>>> getTablesByAreaId(@RequestParam Long areaId) {
        ApiResponse<List<Table>> response = tableService.getTablesByAreaId(areaId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404
        }
    }
}


package com.henrytran1803.BEBakeManage.units.controller;

import java.util.List;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.units.dto.UnitCreationRequest;
import com.henrytran1803.BEBakeManage.units.entity.Units;
import com.henrytran1803.BEBakeManage.units.service.UnitService;

@RestController
@RequestMapping("/api/units")
public class UnitController {
	@Autowired
	private UnitService unitService;
	
	@PostMapping
    public ResponseEntity<ApiResponse<Units>> createUnit(@RequestBody UnitCreationRequest request) {

        try {
            Units createdUnit = unitService.createUnit(request);
            return ResponseEntity.ok(ApiResponse.success(createdUnit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.UNITS_CREATE_FAIL.getCode(), e.getMessage()));
        }
    }
	
	@GetMapping
    public ResponseEntity<ApiResponse<List<Units>>> getUnits() {

        try {
            List<Units> units = unitService.getUnits();
            return ResponseEntity.ok(ApiResponse.success(units));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.UNITS_GET_FAIL.getCode(), e.getMessage()));
        }
    }
	

	@GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Units>> getUnitById(@PathVariable("id") int id) {

        try {
            Units unit = unitService.getUnit(id);
            return ResponseEntity.ok(ApiResponse.success(unit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.UNITS_GET_FAIL.getCode(), e.getMessage()));
        }

    }
}

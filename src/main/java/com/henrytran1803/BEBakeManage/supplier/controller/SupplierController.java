package com.henrytran1803.BEBakeManage.supplier.controller;

import java.util.List;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.supplier.dto.SupplierRequest;
import com.henrytran1803.BEBakeManage.supplier.entity.Supplier;
import com.henrytran1803.BEBakeManage.supplier.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;
	
	@GetMapping
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        try {
            return ResponseEntity.ok(ApiResponse.success(suppliers));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.SUPPLIER_GET_FAIL.getCode(), e.getMessage()));
        }
    }
    
	@PostMapping
    public ResponseEntity<ApiResponse<Supplier>> addSupplier(
            @Valid @RequestBody SupplierRequest request, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.SUPPLIER_CREATE_FAIL.getCode(), bindingResult.getAllErrors().get(0).getDefaultMessage() ));
        }

        Supplier createdSupplier = supplierService.addSupplier(request);
        return ResponseEntity.ok(ApiResponse.success(createdSupplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(
            @PathVariable int id, 
            @Valid @RequestBody SupplierRequest request, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.SUPPLIER_UPDATE_FAIL.getCode(), bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, request);
            return ResponseEntity.ok(ApiResponse.success(updatedSupplier));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.SUPPLIER_UPDATE_FAIL.getCode(), e.getMessage()));
        }

    }
}

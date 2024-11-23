package com.henrytran1803.BEBakeManage.supplier.controller;

import java.util.List;

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
        return ResponseEntity.ok(ApiResponse.success(suppliers));
    }
    
	@PostMapping
    public ResponseEntity<ApiResponse<Supplier>> addSupplier(
            @Valid @RequestBody SupplierRequest request, 
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("SUPPLIER_CREATE_FAIL", "Invalid supplier data"));
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
                    .body(ApiResponse.error("SUPPLIER_UPDATE_FAIL", "Invalid supplier data"));
        }

        Supplier updatedSupplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedSupplier));
    }
    
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable int id) {
//        Supplier supplier = supplierService.getSupplierById(id)
//                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
//        return ResponseEntity.ok(ApiResponse.success(supplier));
//    }
}

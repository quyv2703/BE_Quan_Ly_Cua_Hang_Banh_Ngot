package com.henrytran1803.BEBakeManage.supplier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henrytran1803.BEBakeManage.supplier.dto.SupplierRequest;
import com.henrytran1803.BEBakeManage.supplier.entity.Supplier;
import com.henrytran1803.BEBakeManage.supplier.repository.SupplierRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    public Supplier addSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setNumber(request.getNumber());
        return supplierRepository.save(supplier);
    }


    @Transactional
    public Supplier updateSupplier(int id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
        
        supplier.setName(request.getName());
        supplier.setNumber(request.getNumber());
        
        return supplierRepository.save(supplier);
    }
}

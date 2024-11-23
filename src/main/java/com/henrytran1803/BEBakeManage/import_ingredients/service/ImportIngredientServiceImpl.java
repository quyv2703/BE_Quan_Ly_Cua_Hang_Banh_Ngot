package com.henrytran1803.BEBakeManage.import_ingredients.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientDetailRequest;
import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientRequest;
import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientResponse;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredient;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredientDetail;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredientDetailId;
import com.henrytran1803.BEBakeManage.import_ingredients.repository.ImportIngredientRepository;
import com.henrytran1803.BEBakeManage.ingredients.repository.IngredientRepository;

public class ImportIngredientServiceImpl implements ImportIngredientService {

    @Autowired
    private ImportIngredientRepository importIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional
    @Override
    public ImportIngredient importIngredients(ImportIngredientRequest request) {
        ImportIngredient importIngredient = new ImportIngredient();
        importIngredient.setUser_id(request.getUser_id());
        importIngredient.setImport_date(LocalDateTime.now());
        importIngredient.setId_supplier(request.getId_supplier());

        // Lưu bảng import_ingredients trước để có id cho details
        importIngredient = importIngredientRepository.save(importIngredient);
        
        List<ImportIngredientDetail> details = new ArrayList<>();
        double totalAmount = 0.0;

        for (ImportIngredientDetailRequest detailRequest : request.getIngredients()) {
            ImportIngredientDetailId detailId = new ImportIngredientDetailId();
            detailId.setImport_ingredient_id(importIngredient.getId());
            detailId.setIngredient_id(detailRequest.getIngredient_id());

            ImportIngredientDetail detail = new ImportIngredientDetail();
            detail.setId(detailId);
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPrice(detailRequest.getPrice());

            // Tăng số lượng trong bảng ingredients
            ingredientRepository.increaseQuantity(detailRequest.getIngredient_id(), detailRequest.getQuantity());

            totalAmount += detailRequest.getQuantity() * detailRequest.getPrice();
            details.add(detail);
        }
        
        importIngredient.setTotal_amount(totalAmount);
        importIngredient.setDetails(details);

        return importIngredientRepository.save(importIngredient);
    }

    @Transactional
    @Override
    public List<ImportIngredientResponse> getImportIngredients() {
        List<ImportIngredient> imports = importIngredientRepository.findAll();
        List<ImportIngredientResponse> responseList = new ArrayList<>();

        for (ImportIngredient importIngredient : imports) {
            ImportIngredientResponse response = new ImportIngredientResponse();
            response.setId(importIngredient.getId());
            response.setUser_id(importIngredient.getUser_id());
            response.setId_supplier(importIngredient.getId_supplier());
            response.setImport_date(importIngredient.getImport_date());
            response.setTotal_amount(importIngredient.getTotal_amount());

            List<ImportIngredientDetailRequest> detailRequests = new ArrayList<>();
            for (ImportIngredientDetail detail : importIngredient.getDetails()) {
                ImportIngredientDetailRequest detailRequest = new ImportIngredientDetailRequest();
                detailRequest.setIngredient_id(detail.getId().getIngredient_id());
                detailRequest.setQuantity(detail.getQuantity());
                detailRequest.setPrice(detail.getPrice());
                detailRequests.add(detailRequest);
            }

            response.setIngredients(detailRequests);
            responseList.add(response);
        }

        return responseList;
    }
}
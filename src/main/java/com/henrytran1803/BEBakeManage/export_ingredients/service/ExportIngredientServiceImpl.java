package com.henrytran1803.BEBakeManage.export_ingredients.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.henrytran1803.BEBakeManage.daily_productions.entity.DailyProduction;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.daily_productions.repository.DailyProductionRepository;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.henrytran1803.BEBakeManage.daily_productions.entity.DailyProduction;
//import com.henrytran1803.BEBakeManage.daily_productions.repository.DailyProductionRepository;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientDetailRequest;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientProductDetailRequest;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientRequest;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientResponse;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredient;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredientDetail;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredientDetailId;
import com.henrytran1803.BEBakeManage.export_ingredients.repository.ExportIngredientRepository;
import com.henrytran1803.BEBakeManage.ingredients.repository.IngredientRepository;
//import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
//import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;

@Service
public class ExportIngredientServiceImpl implements ExportIngredientService {

    @Autowired
    private ExportIngredientRepository exportIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private DailyProductionRepository dailyProductionRepository;

    @Autowired
    private ProductBatchRepository productBatchRepository;

    @Transactional
    @Override
    public ExportIngredient exportIngredients(ExportIngredientRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt((String) authentication.getPrincipal());
        // Kiểm tra số lượng nguyên liệu trước khi xuất

        for (ExportIngredientDetailRequest detailRequest : request.getIngredients()) {
            double currentQuantity = ingredientRepository.findById(detailRequest.getIngredient_id())
                                     .orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại"))
                                     .getQuantity();
            if (currentQuantity < detailRequest.getQuantity()) {
                throw new RuntimeException("Số lượng nguyên liệu không đủ để xuất.");
            }
        }
        
        // Lấy ngày hiện tại không lấy giờ
        LocalDateTime currentDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Thêm mới hoặc lấy daily_productions theo ngày hiện tại
        DailyProduction dailyProduction = dailyProductionRepository.findByProductionDate(currentDate)
                .orElseGet(() -> {
                    DailyProduction newDailyProduction = new DailyProduction();
                    newDailyProduction.setProductionDate(currentDate);
                    return dailyProductionRepository.save(newDailyProduction);
                });
        
        // Lưu thông tin xuất nguyên liệu vào bảng export_ingredients
        ExportIngredient exportIngredient = new ExportIngredient();
        exportIngredient.setSender_id(id); //id
        exportIngredient.setTotal_amount(request.getTotal_amount());
        exportIngredient.setExport_date(LocalDateTime.now());
        exportIngredient.setDaily_production_id(dailyProduction.getId());
        
        exportIngredient = exportIngredientRepository.save(exportIngredient);
        
        List<ExportIngredientDetail> details = new ArrayList<>();
        
        // Xuất nguyên liệu và sản phẩm
        for (ExportIngredientDetailRequest detailRequest : request.getIngredients()) {
            ExportIngredientDetailId detailId = new ExportIngredientDetailId();
            detailId.setExport_ingredient_id(exportIngredient.getId());  
            detailId.setIngredient_id(detailRequest.getIngredient_id());
            
            ExportIngredientDetail detail = new ExportIngredientDetail();
            detail.setId(detailId);
            detail.setQuantity(detailRequest.getQuantity());
            
            // Giảm số lượng nguyên liệu
            ingredientRepository.decreaseQuantity(detailRequest.getIngredient_id(), detailRequest.getQuantity());         
            
            details.add(detail);
        }

        exportIngredient.setDetails(details);
        exportIngredientRepository.save(exportIngredient);
        
        // Xử lý sản phẩm
        for (ExportIngredientProductDetailRequest productRequest : request.getProducts()) {
            ProductBatch productBatch = new ProductBatch();

            // Lấy đối tượng Product từ repository theo product_id
            Product product = productRepository.findById(productRequest.getProduct_id())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + productRequest.getProduct_id()));

            // Lấy đối tượng DailyProduction từ repository theo dailyProduction_id
            DailyProduction dlProduction = dailyProductionRepository.findById(dailyProduction.getId())
                    .orElseThrow(() -> new RuntimeException("DailyProduction not found for ID: " + dailyProduction.getId()));

            productBatch.setProduct(product);
            productBatch.setDailyProduction(dlProduction);
            productBatch.setQuantity(productRequest.getQuantity());
            productBatch.setStatus("ACTIVE");

            productBatchRepository.save(productBatch);
        }

        return exportIngredient;
    }

    @Transactional
    @Override
    public List<ExportIngredientResponse> getExportIngredients() {
        List<ExportIngredient> exports = exportIngredientRepository.findAll();
        List<ExportIngredientResponse> responseList = new ArrayList<>();

        for (ExportIngredient export : exports) {
            ExportIngredientResponse response = new ExportIngredientResponse();
            response.setId(export.getId());
            response.setSender_id(export.getSender_id());
            response.setExport_date(export.getExport_date());
            response.setTotal_amount(export.getTotal_amount());
            response.setDaily_production_id(export.getDaily_production_id());

            List<ExportIngredientDetailRequest> detailRequests = new ArrayList<>();
            for (ExportIngredientDetail detail : export.getDetails()) {
                ExportIngredientDetailRequest detailRequest = new ExportIngredientDetailRequest();
                detailRequest.setIngredient_id(detail.getId().getIngredient_id());
                detailRequest.setQuantity(detail.getQuantity());
                detailRequests.add(detailRequest);
            }

            response.setIngredients(detailRequests);
            responseList.add(response);
        }

        return responseList;
    }
}
package com.henrytran1803.BEBakeManage.product.entity;

import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.product.dto.ProductDetailProjection;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.Image.entity.Image; // Import Image entity
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "description", nullable = false, length = 250)
    private String description;
    @Column(name = "shelf_life_days")
    private int shelfLifeDays;
    @Column(name = "status")
    private Boolean status;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @Column(name = "discount_limit")
    private Double discountLimit;

    @Column(name = "shelf_life_days_warning")
    private int shelfLifeDaysWarning;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>(); // Thêm mối quan hệ với Image

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductHistory> productHistories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductBatch> productBatches = new ArrayList<>();

    public void addProductHistory(ProductHistory history) {
        productHistories.add(history);
        history.setProduct(this);
    }

    public void removeProductHistory(ProductHistory history) {
        productHistories.remove(history);
        history.setProduct(null);
    }



    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public Integer getRecipeId() {
        return recipe != null ? recipe.getId() : null;
    }
}
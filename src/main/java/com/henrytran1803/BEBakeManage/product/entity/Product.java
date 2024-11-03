package com.henrytran1803.BEBakeManage.product.entity;

import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.Image.entity.Image; // Import Image entity
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "category_id")
    private Integer categoryId;

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

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    private Recipe recipe;

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>(); // Thêm mối quan hệ với Image

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductHistory> productHistories = new ArrayList<>();

    public void addProductHistory(ProductHistory history) {
        productHistories.add(history);
        history.setProduct(this);
    }

    public void removeProductHistory(ProductHistory history) {
        productHistories.remove(history);
        history.setProduct(null);
    }

    public void addImage(Image image) {
        images.add(image);
        image.setProductId(this.id);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setProductId(null);
    }
}

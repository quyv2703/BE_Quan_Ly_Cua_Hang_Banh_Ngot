package com.henrytran1803.BEBakeManage.product.specification;

import com.henrytran1803.BEBakeManage.product.dto.ProductSearchCriteria;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(ProductSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query.getResultType().equals(Product.class)) {
                root.fetch("category", JoinType.LEFT);
                root.fetch("images", JoinType.LEFT);
                root.fetch("recipe", JoinType.LEFT);
            }
            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("categoryId"),
                        criteria.getCategoryId()
                ));
            }

            if (criteria.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("status"),
                        criteria.getStatus()
                ));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("currentPrice"),
                        criteria.getMinPrice()
                ));
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("currentPrice"),
                        criteria.getMaxPrice()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
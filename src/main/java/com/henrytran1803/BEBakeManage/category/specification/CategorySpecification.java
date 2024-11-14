package com.henrytran1803.BEBakeManage.category.specification;

import com.henrytran1803.BEBakeManage.category.dto.CategorySearchCriteria;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {
    public static Specification<Category> getSpecification(CategorySearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("isActive"),
                        criteria.getIsActive()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
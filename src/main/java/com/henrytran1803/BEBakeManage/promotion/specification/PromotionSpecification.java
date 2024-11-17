package com.henrytran1803.BEBakeManage.promotion.specification;


import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.dto.PromotionSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PromotionSpecification {
    public static Specification<Promotion> getSpecification(PromotionSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), criteria.getId()));
            }

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.getDescription().toLowerCase() + "%"));
            }

            if (criteria.getDiscount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("discount"), criteria.getDiscount()));
            }

            if (criteria.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), criteria.getIsActive()));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), criteria.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package com.henrytran1803.BEBakeManage.supplier.specification;

import com.henrytran1803.BEBakeManage.supplier.dto.SupplierSearchCriteria;
import com.henrytran1803.BEBakeManage.supplier.entity.Supplier;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SupplierSpecification {

    public static Specification<Supplier> getSpecification(SupplierSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.getNumber() != null && !criteria.getNumber().isEmpty()) {
                predicates.add(builder.equal(root.get("number"), criteria.getNumber()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
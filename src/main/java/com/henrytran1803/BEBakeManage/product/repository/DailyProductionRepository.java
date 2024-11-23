package com.henrytran1803.BEBakeManage.product.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.henrytran1803.BEBakeManage.product.entity.DailyProduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyProductionRepository extends JpaRepository<DailyProduction, Integer> {
	Optional<DailyProduction> findByProductionDate(LocalDate productionDate);
}
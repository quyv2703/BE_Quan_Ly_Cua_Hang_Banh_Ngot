package com.henrytran1803.BEBakeManage.daily_productions.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.henrytran1803.BEBakeManage.daily_productions.entity.DailyProduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyProductionRepository extends JpaRepository<DailyProduction, Integer> {
	Optional<DailyProduction> findByProductionDate(LocalDateTime productionDate);
}
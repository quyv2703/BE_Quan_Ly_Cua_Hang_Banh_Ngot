//package com.henrytran1803.BEBakeManage.daily_productions.repository;
//
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.henrytran1803.BEBakeManage.daily_productions.entity.DailyProduction;
//
//public interface DailyProductionRepository extends JpaRepository<DailyProduction, Integer> {
//	Optional<DailyProduction> findByProductionDate(LocalDate productionDate);
//}
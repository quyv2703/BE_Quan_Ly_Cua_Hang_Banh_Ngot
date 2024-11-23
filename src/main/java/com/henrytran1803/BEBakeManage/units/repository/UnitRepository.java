package com.henrytran1803.BEBakeManage.units.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.henrytran1803.BEBakeManage.units.entity.Units;

@Repository
public interface UnitRepository extends JpaRepository<Units, Integer>{
	
}

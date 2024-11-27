package com.henrytran1803.BEBakeManage.units.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henrytran1803.BEBakeManage.units.dto.UnitCreationRequest;
import com.henrytran1803.BEBakeManage.units.entity.Units;
import com.henrytran1803.BEBakeManage.units.repository.UnitRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    public Units createUnit(UnitCreationRequest request) {
        Units unit = new Units();
        unit.setName(request.getName());
        return unitRepository.save(unit);
    }

    @Override
    public List<Units> getUnits() {
        return unitRepository.findAll();
    }

    @Override
    public Units getUnit(int id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found with id " + id));
    }
}
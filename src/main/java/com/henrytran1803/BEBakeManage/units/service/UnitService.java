package com.henrytran1803.BEBakeManage.units.service;

import java.util.List;

import com.henrytran1803.BEBakeManage.units.dto.UnitCreationRequest;
import com.henrytran1803.BEBakeManage.units.entity.Units;

public interface UnitService {
    Units createUnit(UnitCreationRequest request);
    List<Units> getUnits();
    Units getUnit(int id);
}


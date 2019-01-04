package com.dyzhsw.efficient.service.impl;

import com.dyzhsw.efficient.dao.EquipmentLogDao;
import com.dyzhsw.efficient.entity.EquipmentLog;
import com.dyzhsw.efficient.service.EquipmentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentLogServiceImpl implements EquipmentLogService {


    @Autowired
    private EquipmentLogDao equipmentLogDao;

    @Override
    public int addEquipmentLog(EquipmentLog equipmentLog) {

        return equipmentLogDao.insertInfo(equipmentLog);

    }


}

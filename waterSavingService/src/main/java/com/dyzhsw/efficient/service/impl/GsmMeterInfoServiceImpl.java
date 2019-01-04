package com.dyzhsw.efficient.service.impl;

import com.dyzhsw.efficient.dao.GsmMeterInfoDao;
import com.dyzhsw.efficient.entity.GsmMeterInfo;
import com.dyzhsw.efficient.service.GsmMeterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GsmMeterInfoServiceImpl implements GsmMeterInfoService {


    @Autowired
    private GsmMeterInfoDao gsmMeterInfoDao;


    @Override
    public void addGsmMeterInfo(GsmMeterInfo gsmMeterInfo) {

        gsmMeterInfoDao.addGsmMeterInfo(gsmMeterInfo);
    }

    @Override
    public GsmMeterInfo selectByEquNo(String equNo) {
        return gsmMeterInfoDao.selectByEquipmentNo(equNo);
    }
}

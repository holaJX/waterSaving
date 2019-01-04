package com.dyzhsw.efficient.service.impl;


import com.dyzhsw.efficient.dao.FkqValveInfoDao;
import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.FkqValveInfoVo;
import com.dyzhsw.efficient.service.FkqValveInfoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FkqValveInfoServiceImpl implements FkqValveInfoService {


    @Autowired
    private FkqValveInfoDao fkqValveInfoDao;

    @Override
    public void addFkqValveInfo(FkqValveInfo fkqValveInfo) {

        fkqValveInfoDao.addFkqValveInfo(fkqValveInfo);
    }

    @Override
    public FkqValveInfo selectByEquId(String equId) {
        return fkqValveInfoDao.selectByEquipmentId(equId);
    }

	@Override
	public int insertByValveInfoImport(FkqValveInfo fkqValveInfo) {
		return fkqValveInfoDao.insertByValveInfoImport(fkqValveInfo);
	}

	@Override
	public List<FkqValveInfoVo> selectByOfficeId(String officeId) {
		return fkqValveInfoDao.selectByOfficeId(officeId);
	}

}

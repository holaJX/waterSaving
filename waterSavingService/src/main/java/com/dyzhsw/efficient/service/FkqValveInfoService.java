package com.dyzhsw.efficient.service;

import java.util.List;

import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.FkqValveInfoVo;

public interface FkqValveInfoService {

    void addFkqValveInfo(FkqValveInfo fkqValveInfo);

    FkqValveInfo selectByEquId(String equId);

	int insertByValveInfoImport(FkqValveInfo fkqValveInfo);

	List<FkqValveInfoVo> selectByOfficeId(String officeId);
}

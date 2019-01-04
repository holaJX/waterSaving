package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.FkqValveInfoVo;

public interface FkqValveInfoDao {


    void addFkqValveInfo(FkqValveInfo fkqValveInfo);

    FkqValveInfo selectByEquipmentId(String equipmentId);
    
    
    List<Map<String, Object>> selectFkqAllApp(Map<String,Object>map);

	int insertByValveInfoImport(FkqValveInfo fkqValveInfo);


    Map<String, Object> selectFkqNewApp(Map<String, Object>map );

	List<FkqValveInfoVo> selectByOfficeId(String officeId);

}

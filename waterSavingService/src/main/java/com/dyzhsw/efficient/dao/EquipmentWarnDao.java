package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.EquipmentWarn;
import org.apache.ibatis.annotations.Param;

public interface EquipmentWarnDao {
	
	//查找全部
    List<EquipmentWarn> selectAll(EquipmentWarn equipmentWarn);


    List<EquipmentWarn> getEquWarnInfoListByEquNo(String equipmentNo);

    List<EquipmentWarn> getEquWarnInfoListByConditions(@Param("equType") Integer equType, @Param("warnType") Integer warnType, @Param("warnInfo") String warnInfo, @Param("remarks") String remarks, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int deletedById(String id);

    int getWarnNum(String beforeWarnTime);
    
    List<EquipmentWarn> getEquipmentWarnExport(@Param("equType") Integer equType, @Param("warnType") Integer warnType, @Param("warnInfo") String warnInfo, @Param("remarks") String remarks, @Param("startTime") String startTime, @Param("endTime") String endTime);

}

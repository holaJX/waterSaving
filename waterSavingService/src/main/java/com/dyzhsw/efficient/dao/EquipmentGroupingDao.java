package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.EquipmentGrouping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipmentGroupingDao {


    int addEquipmentGroup(EquipmentGrouping equipmentGrouping);

    EquipmentGrouping selectById(String id);

    List<EquipmentGrouping> getGroupingList();

    int deletedGroupingInfoById(@Param("currUserId") String currUserId, @Param("id") String id);

    int updateGroupingInfo(EquipmentGrouping equipmentGrouping);
    
    List<EquipmentGrouping> getGroupingListInfo(EquipmentGrouping equipmentGrouping);

    List<EquipmentGrouping> getGroupListByOfficeId(String officeId);

    List<EquipmentGrouping> getGroupListByOfficeIdAndName(@Param("shoubuIdList") List<String> shoubuIdList, @Param("name") String name);

	EquipmentGrouping selectByNameImport(String name);

	void updateOfficeId(@Param("officeId") String officeId, @Param("currUserId") String currUserId);

	List<String> selectIdByOfficeId(String officeId);
	
	EquipmentGrouping selectByOfficeNameImport(@Param("officeId")String officeId, @Param("name")String name);

}

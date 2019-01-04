package com.dyzhsw.efficient.service;


import java.util.List;

import com.dyzhsw.efficient.entity.EquipmentGrouping;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * 设备分组相关service
 * create by LiHD
 */
public interface EquipmentGroupingService {

    int addEquipmentGroup(EquipmentGrouping equipmentGrouping);

    PageInfo<EquipmentGrouping> getGroupingList(Integer pageNo, Integer pageSize, String name, String currOfficeId, String isSys, String conditionOfficeId);

    Boolean deletedGroupingInfoById(String currUserId, String ids);

    EquipmentGrouping selectGroupInfoById(Integer pageNo, Integer pageSize, PageInfo<EquipmentInfo> pageInfo, String id);

    int updateGroupingInfo(EquipmentGrouping equipmentGrouping);
    
    List<EquipmentGrouping> getGroupingListInfo(EquipmentGrouping equipmentGrouping);

    List<EquipmentGrouping> getGroupListByOfficeId(String officeId);

    boolean deletedGroupEqu(String currUserId, String ids);
    
    EquipmentGrouping selectByNameImport(String name);

	EquipmentGrouping selectByOfficeNameImport(String officeId, String name);

}

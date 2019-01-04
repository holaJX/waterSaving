package com.dyzhsw.efficient.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.dao.EquipmentGroupingDao;
import com.dyzhsw.efficient.dao.EquipmentInfoDao;
import com.dyzhsw.efficient.dao.EquipmentRelationDao;
import com.dyzhsw.efficient.dao.SysOfficeDao;
import com.dyzhsw.efficient.entity.EquipmentGrouping;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentRelation;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.service.EquipmentGroupingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EquipmentGroupingServiceImpl implements EquipmentGroupingService {


    @Autowired
    private EquipmentGroupingDao equipmentGroupingDao;

    @Autowired
    private SysOfficeDao sysOfficeDao;

    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    @Autowired
    private EquipmentRelationDao equipmentRelationDao;

    @Override
    public int addEquipmentGroup(EquipmentGrouping equipmentGrouping) {

        return  equipmentGroupingDao.addEquipmentGroup(equipmentGrouping);
    }

    

    @Override
    public PageInfo<EquipmentGrouping> getGroupingList(Integer pageNo, Integer pageSize, String name, String officeId, String isSys, String conditionOfficeId) {

        List<EquipmentGrouping> equipmentGroupingList = new ArrayList<>();
        if (isSys.equals("1") && StringUtils.isEmpty(conditionOfficeId)) {
            PageHelper.startPage(pageNo, pageSize);
            equipmentGroupingList = equipmentGroupingDao.getGroupListByOfficeIdAndName(null, name);
        } else {
            // 判断当前用户机构是否为首部
            List<String> shoubuIdList = new ArrayList<>();
            SysOffice currOffice = sysOfficeDao.selectById(officeId);
            if (currOffice.getType().equals("3")) { // 是首部
                shoubuIdList.add(currOffice.getId());
                PageHelper.startPage(pageNo, pageSize);
                equipmentGroupingList = equipmentGroupingDao.getGroupListByOfficeIdAndName(shoubuIdList, name);
            } else { // 不是首部
                List<SysOffice> shoubuOfficeList = sysOfficeDao.selectShouBuByOfficeId(officeId);
                for (SysOffice shoubuOffice : shoubuOfficeList) {
                    shoubuIdList.add(shoubuOffice.getId());
                }
                PageHelper.startPage(pageNo, pageSize);
                equipmentGroupingList = equipmentGroupingDao.getGroupListByOfficeIdAndName(shoubuIdList, name);
            }
        }

        PageInfo<EquipmentGrouping> page = new PageInfo<>(equipmentGroupingList);
        return page;
    }


    @Override
    public Boolean deletedGroupingInfoById(String currUserId, String ids) {

        String[] groupIdAry = ids.split(",");
        for (String groupId : groupIdAry) {
            if (equipmentGroupingDao.deletedGroupingInfoById(currUserId, groupId) > 0) {
                // 更新设备表设备分组字段
                equipmentInfoDao.updateGroupId(currUserId, groupId);
                return true;
            }
        }

        return false;
    }


    @Override
    public EquipmentGrouping selectGroupInfoById(Integer pageNo, Integer pageSize, PageInfo<EquipmentInfo> pageInfo, String id) {

        EquipmentGrouping equipmentGrouping = equipmentGroupingDao.selectById(id);
        String officeId = equipmentGrouping.getOfficeId();
        SysOffice groupOffice = sysOfficeDao.selectById(officeId);
        if (groupOffice != null) {
            equipmentGrouping.setOfficeName(groupOffice.getName());
            String parentIds = groupOffice.getParentIds();
            parentIds = parentIds + "," + equipmentGrouping.getOfficeId();
            String[] officeIds = parentIds.split(",");
            List<String> officeIdList = Arrays.asList(officeIds);
            equipmentGrouping.setOfficeIdList(officeIdList);
        }

        // 获取分组下的阀控器
        PageHelper.startPage(pageNo, pageSize);
        List<EquipmentInfo> equipmentInfoList = equipmentInfoDao.getEquipmentListByGroupId(id);
        for (EquipmentInfo equipmentInfo : equipmentInfoList) {
            if (equipmentInfo != null) {
                equipmentInfo.setEquipmentTypeName("阀门控制器");
                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByValveControllerId(equipmentInfo.getId());
                if (equipmentRelation != null && equipmentRelation.getPiezometerId() != null) {
                    equipmentInfo.setRelationEquId(equipmentRelation.getPiezometerId());
                    EquipmentInfo relationEquInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPiezometerId());
                    if (relationEquInfo != null) {
                        relationEquInfo.setEquipmentTypeName("压力表");
                        equipmentInfo.setRelationEquName(relationEquInfo.getName());
                        equipmentInfo.setRelationEqu(relationEquInfo);
                        List<EquipmentInfo> relationEquList = new ArrayList<>();
                        relationEquList.add(relationEquInfo);
                        equipmentInfo.setRelationEquList(relationEquList);
                    }
                }
            }
        }
        pageInfo = new PageInfo<>(equipmentInfoList);
        equipmentGrouping.setPageInfo(pageInfo);

        return equipmentGrouping;
    }


    @Override
    public int updateGroupingInfo(EquipmentGrouping equipmentGrouping) {

        EquipmentGrouping oldGroup = equipmentGroupingDao.selectById(equipmentGrouping.getId());
        if (!StringUtils.isEmpty(equipmentGrouping.getOfficeId()) && !equipmentGrouping.getOfficeId().equals(oldGroup.getOfficeId())) {
            List<String> groupIdList = new ArrayList<>();
            groupIdList.add(equipmentGrouping.getId());
            equipmentInfoDao.emptyGroupId(groupIdList, equipmentGrouping.getUpdateBy());
        }

        return equipmentGroupingDao.updateGroupingInfo(equipmentGrouping);
    }


	@Override
	public List<EquipmentGrouping> getGroupingListInfo(EquipmentGrouping equipmentGrouping) {
		return equipmentGroupingDao.getGroupingListInfo(equipmentGrouping);
	}

    @Override
    public List<EquipmentGrouping> getGroupListByOfficeId(String officeId) {

        return equipmentGroupingDao.getGroupListByOfficeId(officeId);
    }


    @Override
    public boolean deletedGroupEqu(String currUserId, String ids) {

        String[] idAry = ids.split(",");
        for (String id : idAry) {
            int i = equipmentInfoDao.updateGroupIdById(currUserId, id);
        }
        return true;
    }



	@Override
	public EquipmentGrouping selectByNameImport(String name) {
		return equipmentGroupingDao.selectByNameImport(name);
	}



	@Override
	public EquipmentGrouping selectByOfficeNameImport(String officeId, String name) {
		return equipmentGroupingDao.selectByOfficeNameImport(officeId, name);
	}

}

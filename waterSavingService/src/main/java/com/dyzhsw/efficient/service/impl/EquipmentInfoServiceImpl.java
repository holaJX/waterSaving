package com.dyzhsw.efficient.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.dao.*;
import com.dyzhsw.efficient.dto.AddEquipmentInfoDTO;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {

    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    @Autowired
    private FkqValveInfoDao fkqValveInfoDao;

    @Autowired
    private EquipmentGroupingDao equipmentGroupingDao;

    @Autowired
    private GsmMeterInfoDao gsmMeterInfoDao;

    @Autowired
    private EquipmentRelationDao equipmentRelationDao;

    @Autowired
    private SysOfficeDao sysOfficeDao;

    @Autowired
    private EquipmentWarnDao equipmentWarnDao;

    @Autowired
    private WmTerminalinfoDao wmTerminalinfoDao;

    @Autowired
    private WmTerminalStateInfoDao wmTerminalStateInfoDao;

    @Autowired
    private SysDictDao sysDictDao;

    @Autowired
    private PressureHistoryDao pressureHistoryDao;

    @Autowired
    private WmSensormonitorDao wmSensormonitorDao;

    @Autowired
    private GsmMeterInstantaneousflowfordayDao gsmMeterInstantaneousflowfordayDao;

    @Autowired
    private WmMetercalcFlowdetailDao wmMetercalcFlowdetailDao;

    @Autowired
    private TaskEquListDao taskEquListDao;

    @Override
    public int addEquipmentInfo(EquipmentInfo equipmentInfo) {

        return equipmentInfoDao.addEquipmentInfo(equipmentInfo);
    }

    @Override
    public PageInfo<EquipmentInfo> getEquipmentListByEquTypeAndOfficeId(Integer pageNo, Integer pageSize, String officeId, String equType, String name, String equipmentNo, String groupId, Integer isonline, String isSys, String conditionOfficeId) {

        List<EquipmentInfo> equipmentInfoList = new ArrayList<>();

        if (isSys.equals("1") && StringUtils.isEmpty(conditionOfficeId)) {
            if (equType.equals("1")) { // 水肥机
                PageHelper.startPage(pageNo, pageSize);
                equipmentInfoList = equipmentInfoDao.getSfjListByCondition(null, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                    if (!StringUtils.isEmpty(equipmentInfo.getOfficeId())) {
                        SysOffice sysOffice = sysOfficeDao.selectById(equipmentInfo.getOfficeId());
                        equipmentInfo.setOfficeName(sysOffice.getName());
                    }
                    WmTerminalinfo wmTerminalinfo = wmTerminalinfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                    if (wmTerminalinfo != null && !StringUtils.isEmpty(wmTerminalinfo.getIsonline())) {
                        equipmentInfo.setIsonline(Integer.valueOf(wmTerminalinfo.getIsonline()));
                    } else {
                        equipmentInfo.setIsonline(0);
                    }
                    WmTerminalStateInfo wmTerminalStateInfo = wmTerminalStateInfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                    if (wmTerminalStateInfo != null && wmTerminalStateInfo.getState() != null) {
                        equipmentInfo.setWmState(wmTerminalStateInfo.getState());
                    } else {
                        equipmentInfo.setWmState(255);
                    }
                }
            } else if (equType.equals("2")) { // 过滤器（无状态信息）
                PageHelper.startPage(pageNo, pageSize);
                equipmentInfoList = equipmentInfoDao.getGlqListByCondition(null, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                    equipmentInfo.setIsonline(3);
                    if (!StringUtils.isEmpty(equipmentInfo.getOfficeId())) {
                        SysOffice sysOffice = sysOfficeDao.selectById(equipmentInfo.getOfficeId());
                        equipmentInfo.setOfficeName(sysOffice.getName());
                    }
                    EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPercolatorId(equipmentInfo.getId());
                    if (equipmentRelation != null) {
                        if (equipmentRelation.getFlowmeterId() != null) {
                            EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getFlowmeterId());
                            if (relationEquipmentInfo != null) {
                                if (!StringUtils.isEmpty(equipmentInfo.getOfficeId())) {
                                    SysOffice sysOffice = sysOfficeDao.selectById(equipmentInfo.getOfficeId());
                                    relationEquipmentInfo.setOfficeName(sysOffice.getName());
                                }
                                equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                List<EquipmentInfo> relationEquList = new ArrayList<>();
                                relationEquList.add(relationEquipmentInfo);
                                equipmentInfo.setRelationEquList(relationEquList);
                            }
                        }
                    }
                }
            } else if (equType.equals("3")) { // 阀门控制器
                PageHelper.startPage(pageNo, pageSize);
                equipmentInfoList = equipmentInfoDao.getFkqListByCondition(null, Integer.valueOf(equType), name, equipmentNo, groupId, isonline);
                for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                    // 获取阀控器状态
                    FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(equipmentInfo.getId());
                    if (fkqValveInfo != null) {
                        equipmentInfo.setIsonline(fkqValveInfo.getIsonline());
                        equipmentInfo.setValve1Status(fkqValveInfo.getValve1Status());
                        equipmentInfo.setValve2Status(fkqValveInfo.getValve2Status());
                    }
                    EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByValveControllerId(equipmentInfo.getId());
                    if (equipmentRelation != null) {
                        if (equipmentRelation.getPiezometerId() != null) {
                            EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPiezometerId());
                            if (relationEquipmentInfo != null) {
                                relationEquipmentInfo.setGroupingName(equipmentInfo.getGroupingName());
                                equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                List<EquipmentInfo> relationEquList = new ArrayList<>();
                                relationEquList.add(relationEquipmentInfo);
                                equipmentInfo.setRelationEquList(relationEquList);
                            }
                        }
                    }
                }
            }
        } else {
            // 根据officeId获取机构信息
            SysOffice sysOffice = sysOfficeDao.selectById(officeId);
            List<String> shoubuIdList = new ArrayList<>();
            if (sysOffice.getType().equals("3")) { // 当前机构为首部类型
                shoubuIdList.add(sysOffice.getId());
                if (equType.equals("1")) { // 水肥机
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getSfjListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        equipmentInfo.setOfficeName(sysOffice.getName());
                        WmTerminalinfo wmTerminalinfo = wmTerminalinfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                        if (wmTerminalinfo != null && !StringUtils.isEmpty(wmTerminalinfo.getIsonline())) {
                            equipmentInfo.setIsonline(Integer.valueOf(wmTerminalinfo.getIsonline()));
                        } else {
                            equipmentInfo.setIsonline(0);
                        }
                        WmTerminalStateInfo wmTerminalStateInfo = wmTerminalStateInfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                        if (wmTerminalStateInfo != null && wmTerminalStateInfo.getState() != null) {
                            equipmentInfo.setWmState(wmTerminalStateInfo.getState());
                        } else {
                            equipmentInfo.setWmState(255);
                        }
                    }
                } else if (equType.equals("2")) { // 过滤器（无状态信息）
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getGlqListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        equipmentInfo.setIsonline(3);
                        equipmentInfo.setOfficeName(sysOffice.getName());
                        EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPercolatorId(equipmentInfo.getId());
                        if (equipmentRelation != null) {
                            if (equipmentRelation.getFlowmeterId() != null) {
                                EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getFlowmeterId());
                                if (relationEquipmentInfo != null) {
                                    relationEquipmentInfo.setOfficeName(sysOffice.getName());
                                    equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                    List<EquipmentInfo> relationEquList = new ArrayList<>();
                                    relationEquList.add(relationEquipmentInfo);
                                    equipmentInfo.setRelationEquList(relationEquList);
                                }
                            }
                        }
                    }
                } else if (equType.equals("3")) { // 阀门控制器
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getFkqListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, groupId, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        // 获取阀控器状态
                        FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(equipmentInfo.getId());
                        if (fkqValveInfo != null) {
                            equipmentInfo.setIsonline(fkqValveInfo.getIsonline());
                            equipmentInfo.setValve1Status(fkqValveInfo.getValve1Status());
                            equipmentInfo.setValve2Status(fkqValveInfo.getValve2Status());
                        }
                        EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByValveControllerId(equipmentInfo.getId());
                        if (equipmentRelation != null) {
                            if (equipmentRelation.getPiezometerId() != null) {
                                EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPiezometerId());
                                if (relationEquipmentInfo != null) {
                                    relationEquipmentInfo.setGroupingName(equipmentInfo.getGroupingName());
                                    equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                    List<EquipmentInfo> relationEquList = new ArrayList<>();
                                    relationEquList.add(relationEquipmentInfo);
                                    equipmentInfo.setRelationEquList(relationEquList);
                                }
                            }
                        }
                    }
                }
            } else { // 非首部类型
                // 1.查询首部系统
                List<SysOffice> sysOfficeList = sysOfficeDao.selectShouBuByOfficeId(officeId);
                for (SysOffice sysOffice1 : sysOfficeList) {
                    shoubuIdList.add(sysOffice1.getId());
                }

                if (equType.equals("1")) { // 水肥机
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getSfjListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        WmTerminalinfo wmTerminalinfo = wmTerminalinfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                        if (wmTerminalinfo != null && !StringUtils.isEmpty(wmTerminalinfo.getIsonline())) {
                            equipmentInfo.setIsonline(Integer.valueOf(wmTerminalinfo.getIsonline()));
                        } else {
                            equipmentInfo.setIsonline(0);
                        }
                        WmTerminalStateInfo wmTerminalStateInfo = wmTerminalStateInfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                        if (wmTerminalStateInfo != null && wmTerminalStateInfo.getState() != null) {
                            equipmentInfo.setWmState(wmTerminalStateInfo.getState());
                        } else {
                            equipmentInfo.setWmState(255);
                        }
                    }
                } else if (equType.equals("2")) { // 过滤器（无状态信息）
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getGlqListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, null, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        equipmentInfo.setIsonline(3);
                        EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPercolatorId(equipmentInfo.getId());
                        if (equipmentRelation != null) {
                            if (equipmentRelation.getFlowmeterId() != null) {
                                EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getFlowmeterId());
                                if (relationEquipmentInfo != null) {
                                    equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                    List<EquipmentInfo> relationEquList = new ArrayList<>();
                                    relationEquList.add(relationEquipmentInfo);
                                    equipmentInfo.setRelationEquList(relationEquList);
                                }
                            }
                        }
                    }
                } else if (equType.equals("3")) { // 阀门控制器
                    PageHelper.startPage(pageNo, pageSize);
                    equipmentInfoList = equipmentInfoDao.getFkqListByCondition(shoubuIdList, Integer.valueOf(equType), name, equipmentNo, groupId, isonline);
                    for (EquipmentInfo equipmentInfo : equipmentInfoList) {
                        // 获取阀控器状态
                        FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(equipmentInfo.getId());
                        if (fkqValveInfo != null) {
                            equipmentInfo.setIsonline(fkqValveInfo.getIsonline());
                            equipmentInfo.setValve1Status(fkqValveInfo.getValve1Status());
                            equipmentInfo.setValve2Status(fkqValveInfo.getValve2Status());
                        }
                        EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByValveControllerId(equipmentInfo.getId());
                        if (equipmentRelation != null) {
                            if (equipmentRelation.getPiezometerId() != null) {
                                EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPiezometerId());
                                if (relationEquipmentInfo != null) {
                                    relationEquipmentInfo.setGroupingName(equipmentInfo.getGroupingName());
                                    equipmentInfo.setRelationEqu(relationEquipmentInfo);
                                    List<EquipmentInfo> relationEquList = new ArrayList<>();
                                    relationEquList.add(relationEquipmentInfo);
                                    equipmentInfo.setRelationEquList(relationEquList);
                                }
                            }
                        }
                    }
                }
            }
        }

        PageInfo<EquipmentInfo> page = new PageInfo<>(equipmentInfoList);
        return page;
    }


    @Override
    public PageInfo<EquipmentInfo> getCollectEquipmentListByOfficeId(Integer pageNo, Integer pageSize, String officeId, String name, String equipmentNo, String affiliationId, Integer isonline, String isSys) {

        List<EquipmentInfo> equipmentInfoList = new ArrayList<>();

        if (StringUtils.isEmpty(affiliationId)) {
            officeId = affiliationId;
        }

        if (isSys.equals("1") && StringUtils.isEmpty(affiliationId)) {
            PageHelper.startPage(pageNo, pageSize);
            equipmentInfoList = equipmentInfoDao.getCollectEquipmentListByConditions(null, name, equipmentNo, isonline);
            equipmentInfoList = handleEquipmentList(equipmentInfoList);
        } else {
            // 根据officeId获取机构信息
            SysOffice sysOffice = sysOfficeDao.selectById(officeId);
            List<String> shoubuIdList = new ArrayList<>();
            if (sysOffice.getType().equals("3")) { // 当前用户所在机构为首部类型
                shoubuIdList.add(sysOffice.getId());
                PageHelper.startPage(pageNo, pageSize);
                equipmentInfoList = equipmentInfoDao.getCollectEquipmentListByConditions(shoubuIdList, name, equipmentNo, isonline);
                equipmentInfoList = handleEquipmentList(equipmentInfoList);
            } else { // 非首部系统
                // 1.查询首部系统
                List<SysOffice> sysOfficeList = sysOfficeDao.selectShouBuByOfficeId(officeId);
                for (SysOffice sysOffice1 : sysOfficeList) {
                    shoubuIdList.add(sysOffice1.getId());
                }
                PageHelper.startPage(pageNo, pageSize);
                equipmentInfoList = equipmentInfoDao.getCollectEquipmentListByConditions(shoubuIdList, name, equipmentNo, isonline);
                equipmentInfoList = handleEquipmentList(equipmentInfoList);
            }
        }

        PageInfo<EquipmentInfo> page = new PageInfo<>(equipmentInfoList);
        return page;
    }


    private List<EquipmentInfo> handleEquipmentList(List<EquipmentInfo> equipmentInfoList) {
        for (EquipmentInfo equipmentInfo : equipmentInfoList) {
            List<SysDict> equTypeList = sysDictDao.getEquTypeList();
            for (SysDict equType : equTypeList) {
                if (equType.getValue().equals(equipmentInfo.getEquipmentType()+"")) {
                    equipmentInfo.setEquipmentTypeName(equType.getLabel());
                }
            }
            if (equipmentInfo.getEquipmentType() == 4) { // 流量计
                GsmMeterInfo gsmMeterInfo = gsmMeterInfoDao.selectByEquipmentNo(equipmentInfo.getEquipmentNo());
                if (gsmMeterInfo != null && !StringUtils.isEmpty(gsmMeterInfo.getIsonline())) {
                    equipmentInfo.setIsonline(Integer.valueOf(gsmMeterInfo.getIsonline()));
                }
                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByFlowmeterId(equipmentInfo.getId());
                if (equipmentRelation != null) {
                    if (equipmentRelation.getPercolatorId() != null) {
                        EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPercolatorId());
                        if (relationEquipmentInfo != null) {
                            equipmentInfo.setRelationEquId(relationEquipmentInfo.getId());
                            equipmentInfo.setRelationEquName(relationEquipmentInfo.getName());
                        }
                    }
                }
            } else { // 压力表
                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPiezometerId(equipmentInfo.getId());
                if (equipmentRelation != null) {
                    if (equipmentRelation.getValveControllerId() != null) {
                        EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getValveControllerId());
                        if (relationEquipmentInfo != null) {
                            equipmentInfo.setRelationEquId(relationEquipmentInfo.getId());
                            equipmentInfo.setRelationEquName(relationEquipmentInfo.getName());
                        }
                    }
                }
            }
        }

        return equipmentInfoList;
    }


    @Override
    public EquipmentInfo getEquipmentInfoByIdAndType(String id) {

        EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoById(id);
        if (equipmentInfo != null) {
            Integer equType = equipmentInfo.getEquipmentType();
            String equTypeName = sysDictDao.selectTypeNameByEquType(equType+"");
            equipmentInfo.setEquipmentTypeName(equTypeName);
            SysOffice sysOffice = sysOfficeDao.selectById(equipmentInfo.getOfficeId());
            if (sysOffice != null) {
                String parentIds = sysOffice.getParentIds();
                parentIds = parentIds + "," + equipmentInfo.getOfficeId();
                String[] officeIds = parentIds.split(",");
                List<String> officeIdList = Arrays.asList(officeIds);
                equipmentInfo.setOfficeIdList(officeIdList);
            }
            if (equType == 1) { // 水肥机
                // 1、获取施肥机相关数据
                WmMetercalcFlowdetail flowdetail = wmMetercalcFlowdetailDao.getDetailInfoByEquNo(equipmentInfo.getEquipmentNo());
                if (flowdetail != null) {
                    equipmentInfo.setInstantaneousflow(flowdetail.getInstantaneousflow());
                }
                WmTerminalStateInfo wmTerminalStateInfo = wmTerminalStateInfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                if (wmTerminalStateInfo != null && wmTerminalStateInfo.getState() != null) {
                    equipmentInfo.setWmState(wmTerminalStateInfo.getState());
                } else {
                    equipmentInfo.setWmState(255);
                }

            } else if (equType == 2) { // 过滤器
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByPercolatorId(equipmentInfo.getId());
                if (equRelation != null) {
                    EquipmentInfo relationEqu = equipmentInfoDao.getEquipmentInfoById(equRelation.getFlowmeterId());
                    if (relationEqu != null) {
                        equipmentInfo.setRelationEqu(relationEqu);
                        List<EquipmentInfo> relationEquList = new ArrayList<>();
                        relationEquList.add(relationEqu);
                        equipmentInfo.setRelationEquList(relationEquList);
                    }
                }
            } else if (equType == 3) { // 阀门控制器
                FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(equipmentInfo.getId());
                if (fkqValveInfo != null) {
                    equipmentInfo.setIsonline(fkqValveInfo.getIsonline());
                    equipmentInfo.setValve1Status(fkqValveInfo.getValve1Status());
                    equipmentInfo.setValve2Status(fkqValveInfo.getValve2Status());
                }
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByValveControllerId(equipmentInfo.getId());
                if (equRelation != null) {
                    EquipmentInfo relationEqu = equipmentInfoDao.getEquipmentInfoById(equRelation.getPiezometerId());
                    if (relationEqu != null) {
                        equipmentInfo.setRelationEqu(relationEqu);
                        List<EquipmentInfo> relationEquList = new ArrayList<>();
                        relationEquList.add(relationEqu);
                        equipmentInfo.setRelationEquList(relationEquList);
                    }
                }
            } else if (equType == 4) { // 流量计
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByFlowmeterId(equipmentInfo.getId());
                if (equRelation != null) {
                    EquipmentInfo relationEqu = equipmentInfoDao.getEquipmentInfoById(equRelation.getPercolatorId());
                    if (relationEqu != null) {
                        equipmentInfo.setRelationEqu(relationEqu);
                        List<EquipmentInfo> relationEquList = new ArrayList<>();
                        relationEquList.add(relationEqu);
                        equipmentInfo.setRelationEquList(relationEquList);
                    }
                }
            } else if (equType == 5) { // 压力计
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByPiezometerId(equipmentInfo.getId());
                if (equRelation != null) {
                    EquipmentInfo relationEqu = equipmentInfoDao.getEquipmentInfoById(equRelation.getValveControllerId());
                    if (relationEqu != null) {
                        equipmentInfo.setRelationEqu(relationEqu);
                        List<EquipmentInfo> relationEquList = new ArrayList<>();
                        relationEquList.add(relationEqu);
                        equipmentInfo.setRelationEquList(relationEquList);
                    }
                }
            } else {
                // nothing to do
            }
        }

        return equipmentInfo;
    }


    @Override
    public List<EquipmentInfo> getOptionalsCollectEquipmentList(String officeId, Integer equType) {
        List<EquipmentInfo> equipmentInfoList = new ArrayList<>();
        if (equType == 1) { // 压力表
            List<EquipmentInfo> tempList = equipmentInfoDao.getEquipmentListByEquTypeAndOfficeId(officeId, 5);
            for (EquipmentInfo equipmentInfo : tempList) {
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByPiezometerId(equipmentInfo.getId());
                if (equRelation == null || equRelation.getValveControllerId() == null) {
                    equipmentInfoList.add(equipmentInfo);
                }
            }
        } else { // 流量计
            List<EquipmentInfo> tempList = equipmentInfoDao.getEquipmentListByEquTypeAndOfficeId(officeId, 4);
            for (EquipmentInfo equipmentInfo : tempList) {
                EquipmentRelation equRelation = equipmentRelationDao.selectInfoByFlowmeterId(equipmentInfo.getId());
                if (equRelation == null || equRelation.getPercolatorId() == null) {
                    equipmentInfoList.add(equipmentInfo);
                }
            }
        }

        return equipmentInfoList;
    }


    @Override
    public int updateEquipmentInfo(EquipmentInfo equipmentInfo) {
        return equipmentInfoDao.updateEquipmentInfo(equipmentInfo);
    }


    @Override
    public PageInfo<EquipmentWarn> getEquWarnInfoList(Integer pageNo, Integer pageSize, String equipmentNo, Integer equipmentType) {

        PageHelper.startPage(pageNo, pageSize);
        List<EquipmentWarn> equipmentWarnList = equipmentWarnDao.getEquWarnInfoListByEquNo(equipmentNo);
        String warnType = "";
        if (equipmentType == 1) {
            warnType = "wm_warn_type";
        }
        if (equipmentType == 4) {
            warnType = "gsm_warn_type";
        }
        if (equipmentType == 5) {
            warnType = "pre_warn_type";
        }
        List<SysDict> warnTypeList = sysDictDao.selectWarnTypeByType(warnType);
        for (EquipmentWarn equipmentWarn : equipmentWarnList) {
            for (SysDict sysDict : warnTypeList) {
                if (equipmentWarn.getWarnType().equals(sysDict.getValue())) {
                    equipmentWarn.setWarnTypeName(sysDict.getLabel());
                }
            }
        }
        PageInfo<EquipmentWarn> page = new PageInfo<>(equipmentWarnList);
        return page;
    }


    @Override
    public PageInfo<EquipmentWarn> getEquWarnList(Integer pageNo, Integer pageSize, Integer equType, Integer warnType, String warnInfo, String remarks, String startTime, String endTime) {

        PageHelper.startPage(pageNo, pageSize);
        List<EquipmentWarn> equipmentWarnList = equipmentWarnDao.getEquWarnInfoListByConditions(equType, warnType, warnInfo, remarks, startTime, endTime);
        if (equipmentWarnList != null && equipmentWarnList.size() > 0) {
            List<SysDict> wmWarnTypeList = sysDictDao.selectWarnTypeByType("wm_warn_type");
            List<SysDict> gsmWarnTypeList = sysDictDao.selectWarnTypeByType("gsm_warn_type");
            List<SysDict> preWarnTypeList = sysDictDao.selectWarnTypeByType("pre_warn_type");

            for (EquipmentWarn equWarn : equipmentWarnList) {
                if (equWarn.getEquipmentType() == 1) {
                    for (SysDict sysDict : wmWarnTypeList) {
                        if (equWarn.getWarnType().equals(sysDict.getValue())) {
                            equWarn.setWarnTypeName(sysDict.getLabel());
                        }
                    }
                } else if (equWarn.getEquipmentType() == 4) {
                    for (SysDict sysDict : gsmWarnTypeList) {
                        if (equWarn.getWarnType().equals(sysDict.getValue())) {
                            equWarn.setWarnTypeName(sysDict.getLabel());
                        }
                    }
                } else if (equWarn.getEquipmentType() == 5) {
                    for (SysDict sysDict : preWarnTypeList) {
                        if (equWarn.getWarnType().equals(sysDict.getValue())) {
                            equWarn.setWarnTypeName(sysDict.getLabel());
                        }
                    }
                }
            }
        }

        PageInfo<EquipmentWarn> page = new PageInfo<>(equipmentWarnList);
        return page;

    }


    @Override
    public void deletedEquipmentInfoById(String currUserId, String ids, Integer equipmentType) {

        String[] idAry = ids.split(",");
        for (String id : idAry) {
            if (equipmentInfoDao.deletedEquipmentInfoById(currUserId, id) > 0) {
                if (equipmentType == 1) { // 施肥机
                    // 删除定时任务绑定关系
                    taskEquListDao.deletedByEquId(id);
                } else if (equipmentType == 2) { // 过滤器
                    equipmentRelationDao.deletedByPercolatorId(id);
                } else if (equipmentType == 3) { // 阀控器
                    // 删除设备绑定关系
                    equipmentRelationDao.deletedByValveControllerId(id);
                    // 删除定时任务绑定关系
                    taskEquListDao.deletedByEquId(id);
                } else if (equipmentType == 4) { // 流量计
                    equipmentRelationDao.deletedByFlowmeterId(id);
                } else if (equipmentType == 5) { // 压力表
                    equipmentRelationDao.deletedByPiezometerId(id);
                }
            }
        }
    }


    @Override
    public List<EquipmentInfo> getValveList(int equType) {
        List<EquipmentInfo> equipmentInfo = equipmentInfoDao.getValveList(equType);
        return equipmentInfo;
    }

    @Override
    public List<Map<String,Object>> getEquListByOfficeId(String officeId, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("officeId", officeId);
        map.put("type", type);
        List<Map<String,Object>> equipmentInfo = equipmentInfoDao.getEquListByOfficeId(map);
        return equipmentInfo;
    }

    @Override
    public List<EquipmentInfo> selectNotIn(String[] taskList, String officeId, int type) {
        List<EquipmentInfo> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("taskList", taskList);
        map.put("officeId", officeId);
        map.put("type", type);
        list = equipmentInfoDao.selectNotIn(map);
        return list;
    }


    @Override
    @Transactional
    public BaseResponse saveEquipmentTransaction(AddEquipmentInfoDTO equipmentInfoDTO) {
        EquipmentInfo equipmentInfo = equipmentInfoDTO.getEquipmentInfo();
        String flowmeterId = equipmentInfoDTO.getFlowmeterId();
        String currUserId = equipmentInfoDTO.getCurrentUserId();
        String piezometerId = equipmentInfoDTO.getPiezometerId();
        addEquipmentInfo(equipmentInfo);
        if (equipmentInfo.getEquipmentType() == 1) {  // 水肥机
            // 1.水肥机信息表创建一条相关数据
            WmTerminalinfo wmTerminalinfo = new WmTerminalinfo();
            wmTerminalinfo.setId(IDUtils.createUUID());
            wmTerminalinfo.setMaxValue(equipmentInfo.getMaxValue());
            wmTerminalinfo.setMinValue(equipmentInfo.getMinValue());
            wmTerminalinfo.setTerminalId(equipmentInfo.getEquipmentNo());
            wmTerminalinfo.setCreateTime(new Date());
            wmTerminalinfoDao.insertInfo(wmTerminalinfo);
        } else if (equipmentInfo.getEquipmentType() == 2) {  // 过滤器（没有单独的基础数据表）
            // 1.判断是否需要做绑定操作 (绑定流量计)
            if (flowmeterId != null) {
                EquipmentRelation equipmentRelation = new EquipmentRelation();
                equipmentRelation.setId(IDUtils.createUUID());
                equipmentRelation.setFlowmeterId(flowmeterId);
                equipmentRelation.setPercolatorId(equipmentInfo.getId());
                equipmentRelation.setCreateBy(currUserId);
                equipmentRelation.setCreateTime(new Date());
                equipmentRelationDao.addEquRelationInfo(equipmentRelation);
            }
        } else if (equipmentInfo.getEquipmentType() == 3) {  // 阀门控制器
            // 1.阀控信息表创建一条相关数据
            FkqValveInfo fkqValveInfo = new FkqValveInfo();
            fkqValveInfo.setId(IDUtils.createUUID());
            fkqValveInfo.setUpdateTime(new Date());
            fkqValveInfo.setEquipmentId(equipmentInfo.getId());
            fkqValveInfoDao.addFkqValveInfo(fkqValveInfo);
            // 2.判断是否需要做绑定操作（绑定压力计）
            if (piezometerId != null) {
                EquipmentRelation equipmentRelation = new EquipmentRelation();
                equipmentRelation.setId(IDUtils.createUUID());
                equipmentRelation.setPiezometerId(piezometerId);
                equipmentRelation.setValveControllerId(equipmentInfo.getId());
                equipmentRelation.setCreateBy(currUserId);
                equipmentRelation.setCreateTime(new Date());
                equipmentRelationDao.addEquRelationInfo(equipmentRelation);
            }
        } else if (equipmentInfo.getEquipmentType() == 4) {  // 流量计
            // 1.流量计信息表创建一条相关数据
            GsmMeterInfo gsmMeterInfo = new GsmMeterInfo();
            gsmMeterInfo.setId(IDUtils.createUUID());
            gsmMeterInfo.setAddress(equipmentInfo.getEquipmentNo());
            gsmMeterInfo.setCreateTime(new Date());
            gsmMeterInfo.setUpdateTime(new Date());
            gsmMeterInfoDao.addGsmMeterInfo(gsmMeterInfo);

        } else if (equipmentInfo.getEquipmentType() == 5) {  // 压力计（没有单独的基础数据表）
            // nothing to do
        }
        return BaseResponseUtil.success();
    }


    @Override
    public List<EquipmentInfo> queryEquipmentListPage(Map map) {
        return equipmentInfoDao.selectEquipmentByMap(map);
    }
    @Override
    public List<EquipmentInfo> processEquipmentHandle(List<EquipmentInfo> currentList) {
        List<EquipmentInfo> equipmentInfoList = new ArrayList<>();
        for (EquipmentInfo equipmentInfo : currentList
                ) {
            if (equipmentInfo.getEquipmentType() == 1)//水肥机
            {
                WmTerminalinfo wmTerminalinfo = wmTerminalinfoDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                if (wmTerminalinfo != null) {
                    equipmentInfo.setIsonline(Integer.parseInt(wmTerminalinfo.getIsonline()));
                    equipmentInfo.setMaxValue(wmTerminalinfo.getMaxValue());
                    equipmentInfo.setMinValue(wmTerminalinfo.getMinValue());
                }
            }
            if (equipmentInfo.getEquipmentType() == 2)//过滤器
            {
                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPercolatorId(equipmentInfo.getId());
                if (equipmentRelation != null) {
                    if (equipmentRelation.getFlowmeterId() != null) {
                        EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getFlowmeterId());
                        if (relationEquipmentInfo != null) {
                            equipmentInfo.setRelationEqu(relationEquipmentInfo);
                        }
                    }
                }
            }
            if (equipmentInfo.getEquipmentType() == 3)//阀控器
            {
                FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(equipmentInfo.getId());
                if (fkqValveInfo != null) {
                    equipmentInfo.setIsonline(fkqValveInfo.getIsonline());
                    equipmentInfo.setValve1Status(fkqValveInfo.getValve1Status());
                    equipmentInfo.setValve2Status(fkqValveInfo.getValve2Status());
                }
                EquipmentGrouping equipmentGrouping = equipmentGroupingDao.selectById(equipmentInfo.getId());
                if (equipmentGrouping != null)
                    equipmentInfo.setGroupingName(equipmentGrouping.getName());
            }
            if (equipmentInfo.getEquipmentType() == 4) { // 流量计
                GsmMeterInfo gsmMeterInfo = gsmMeterInfoDao.selectByEquipmentNo(equipmentInfo.getEquipmentNo());
                if (gsmMeterInfo != null && !StringUtils.isEmpty(gsmMeterInfo.getIsonline())) {
                    equipmentInfo.setIsonline(Integer.valueOf(gsmMeterInfo.getIsonline()));
                }
                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByFlowmeterId(equipmentInfo.getId());
                if (equipmentRelation != null) {
                    if (equipmentRelation.getPercolatorId() != null) {
                        EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getPercolatorId());
                        if (relationEquipmentInfo != null) {
                            equipmentInfo.setRelationEquId(relationEquipmentInfo.getId());
                            equipmentInfo.setRelationEquName(relationEquipmentInfo.getName());
                        }
                    }
                }
            }
            if (equipmentInfo.getEquipmentType() == 5) {//压力计

                EquipmentRelation equipmentRelation = equipmentRelationDao.selectInfoByPiezometerId(equipmentInfo.getId());
                if (equipmentRelation != null) {
                    if (equipmentRelation.getValveControllerId() != null) {
                        EquipmentInfo relationEquipmentInfo = equipmentInfoDao.getEquipmentInfoById(equipmentRelation.getValveControllerId());
                        if (relationEquipmentInfo != null) {
                            equipmentInfo.setRelationEquId(relationEquipmentInfo.getId());
                            equipmentInfo.setRelationEquName(relationEquipmentInfo.getName());
                        }
                    }
                }
            }
            // 根据officeId获取机构信息
            SysOffice sysOffice = sysOfficeDao.selectById(equipmentInfo.getOfficeId());
            equipmentInfo.setOfficeName(sysOffice.getName());
            equipmentInfoList.add(equipmentInfo);
        }
        return equipmentInfoList;
    }

    @Override
    public List<Map<String, Object>> getFkq() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = equipmentInfoDao.getFkq();
        return list;
    }

    @Override
    public List<Map<String, Object>> getWm() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = equipmentInfoDao.getWm();
        return list;
    }
    
    @Override
    public List<Map<String, Object>> getWmByOfficeId(String officeId) {
    	List<Map<String, Object>> list = new ArrayList<>();
    	list = equipmentInfoDao.getWmByOfficeId(officeId);
        return list;
    }


    @Override
    public void deletedEquWarn(String ids) {

        String[] idAry = ids.split(",");
        for (String id : idAry) {
            int i = equipmentWarnDao.deletedById(id);
        }
    }

    @Override
    public int getWarnNum(String beforeWarnTime) {
        return equipmentWarnDao.getWarnNum(beforeWarnTime);
    }

    @Override
    public List<EquipmentInfo> getOptionalEquByGroupId(String officeId) {

        return equipmentInfoDao.getOptionalEquByGroupId(officeId);
    }

    @Override
    public void addEquToGroup(String ids, String groupId) {

        String[] idAry = ids.split(",");
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        for (String id : idAry) {
            equipmentInfo.setId(id);
            equipmentInfo.setGroupingId(groupId);
            int i = equipmentInfoDao.updateEquipmentInfo(equipmentInfo);
        }
    }


    @Override
    public Map<String, Object> getEquChartInfo(String id, Integer chartType, Integer pageNo, Integer pageSize) {

        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("graph", new ArrayList<>());
        resultMap.put("pageInfo", new PageInfo<>());

        EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoById(id);
        Integer equType = equipmentInfo.getEquipmentType();
        if (equType == 1) { // 施肥机
            if (chartType == 1) {
                List<WmSensormonitor> wmSensormonitorList = wmSensormonitorDao.selectGraphByEquNo(equipmentInfo.getEquipmentNo());
                Collections.reverse(wmSensormonitorList); // 倒序排列
                resultMap.put("graph", wmSensormonitorList);
                return resultMap;
            } else {
                PageHelper.startPage(pageNo, pageSize);
                List<WmSensormonitor> wmSensormonitorList = wmSensormonitorDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                PageInfo<WmSensormonitor> pageInfo = new PageInfo<>(wmSensormonitorList);
                resultMap.put("pageInfo", pageInfo);
                return resultMap;
            }
        } else if (equType == 2) { // 过滤器
            EquipmentRelation equRelation = equipmentRelationDao.selectInfoByPercolatorId(id);
            if (chartType == 1) {
                if (equRelation == null || equRelation.getFlowmeterId() == null) { // 未绑定设备
                    return resultMap;
                }
                String flowmeterId = equRelation.getFlowmeterId();
                EquipmentInfo flowmeter = equipmentInfoDao.getEquipmentInfoById(flowmeterId);
                if (flowmeter != null) {
                    List<GsmMeterInstantaneousflowforday> gsmMeterInstantaneousflowfordayList = gsmMeterInstantaneousflowfordayDao.selectGraphByEquNo(flowmeter.getEquipmentNo());
                    Collections.reverse(gsmMeterInstantaneousflowfordayList); // 倒序排列
                    resultMap.put("graph", gsmMeterInstantaneousflowfordayList);
                    return resultMap;
                }
            } else {
                if (equRelation == null || equRelation.getFlowmeterId() == null) { // 未绑定设备
                    return resultMap;
                }
                String flowmeterId = equRelation.getFlowmeterId();
                EquipmentInfo flowmeter = equipmentInfoDao.getEquipmentInfoById(flowmeterId);
                if (flowmeter != null) {
                    PageHelper.startPage(pageNo, pageSize);
                    List<GsmMeterInstantaneousflowforday> gsmMeterInstantaneousflowfordayList = gsmMeterInstantaneousflowfordayDao.selectByEquNo(flowmeter.getEquipmentNo());
                    PageInfo<GsmMeterInstantaneousflowforday> pageInfo = new PageInfo<>(gsmMeterInstantaneousflowfordayList);
                    resultMap.put("pageInfo", pageInfo);
                    return resultMap;
                }
            }
        } else if (equType == 3) { // 阀控器
            EquipmentRelation equRelation = equipmentRelationDao.selectInfoByValveControllerId(id);
            if (chartType == 1) { // 曲线图
                if (equRelation == null || equRelation.getPiezometerId() == null) { // 未绑定设备
                    return resultMap;
                }
                String piezometerId = equRelation.getPiezometerId();
                EquipmentInfo piezometer = equipmentInfoDao.getEquipmentInfoById(piezometerId);
                if (piezometer != null) {
                    List<PressureHistory> pressureHistoryList = pressureHistoryDao.selectGraphByEquNo(piezometer.getEquipmentNo());
                    Collections.reverse(pressureHistoryList); // 倒序排列
                    resultMap.put("graph", pressureHistoryList);
                    return resultMap;
                }
            } else { // 列表
                if (equRelation == null || equRelation.getPiezometerId() == null) { // 未绑定设备
                    return resultMap;
                }
                String piezometerId = equRelation.getPiezometerId();
                EquipmentInfo piezometer = equipmentInfoDao.getEquipmentInfoById(piezometerId);
                if (piezometer != null) {
                    PageHelper.startPage(pageNo, pageSize);
                    List<PressureHistory> pressureHistoryList = pressureHistoryDao.selectByEquNo(piezometer.getEquipmentNo());
                    PageInfo<PressureHistory> pageInfo = new PageInfo<>(pressureHistoryList);
                    resultMap.put("pageInfo", pageInfo);
                    return resultMap;
                }

            }
        } else if (equType == 4) { // 流量计
            if (chartType == 1) {
                List<GsmMeterInstantaneousflowforday> gsmMeterInstantaneousflowfordayList = gsmMeterInstantaneousflowfordayDao.selectGraphByEquNo(equipmentInfo.getEquipmentNo());
                Collections.reverse(gsmMeterInstantaneousflowfordayList); // 倒序排列
                resultMap.put("graph", gsmMeterInstantaneousflowfordayList);
                return resultMap;
            } else {
                PageHelper.startPage(pageNo, pageSize);
                List<GsmMeterInstantaneousflowforday> gsmMeterInstantaneousflowfordayList = gsmMeterInstantaneousflowfordayDao.selectByEquNo(equipmentInfo.getEquipmentNo());
                PageInfo<GsmMeterInstantaneousflowforday> pageInfo = new PageInfo<>(gsmMeterInstantaneousflowfordayList);
                resultMap.put("pageInfo", pageInfo);
                return resultMap;
            }
        } else if (equType == 5) { // 压力表
            EquipmentInfo piezometer = equipmentInfoDao.getEquipmentInfoById(id);
            if (piezometer != null) {
                if (chartType == 1) { // 曲线图
                    List<PressureHistory> pressureHistoryList = pressureHistoryDao.selectGraphByEquNo(piezometer.getEquipmentNo());
                    Collections.reverse(pressureHistoryList); // 倒序排列
                    resultMap.put("graph", pressureHistoryList);
                    return resultMap;
                } else { // 列表
                    PageHelper.startPage(pageNo, pageSize);
                    List<PressureHistory> pressureHistoryList = pressureHistoryDao.selectByEquNo(piezometer.getEquipmentNo());
                    PageInfo<PressureHistory> pageInfo = new PageInfo<>(pressureHistoryList);
                    resultMap.put("pageInfo", pageInfo);
                    return resultMap;
                }
            }
        }

        return resultMap;

    }


    @Override
    public EquipmentInfo selectByEquNo(String equNo) {

        return equipmentInfoDao.selectByEquNo(equNo);
    }
    
    @Override
    public String StringNoById(String id) {

        return equipmentInfoDao.StringNoById(id);
    }


	@Override
	public List<EquipmentInfo> getEquipmentInfo(EquipmentInfo equipmentInfo) {
		return equipmentInfoDao.getEquipmentInfo(equipmentInfo);
	}

	@Override
	public EquipmentInfo getEquipmentInfoById(String id) {
		return equipmentInfoDao.getEquipmentInfoById(id);
	}

	@Override
	public List<EquipmentInfo> getEquipmentInfoFilter(EquipmentInfo equipmentInfo) {
		return equipmentInfoDao.getEquipmentInfoFilter(equipmentInfo);
	}

	@Override
	public List<EquipmentInfo> getEquipmentInfoCollection(EquipmentInfo equipmentInfo) {
		return equipmentInfoDao.getEquipmentInfoCollection(equipmentInfo);
	}

	@Override
	public List<EquipmentInfo> getEquipmentInfoExport(String name,String equipmentNo,Integer isonline,String groupId,String officeId) {
		return equipmentInfoDao.getEquipmentInfoExport(name,equipmentNo,isonline,groupId,officeId);
	}

	@Override
	public List<EquipmentInfo> getEquipmentInfoFilterExport(String name,String equipmentNo,Integer isonline,String officeId) {
		return equipmentInfoDao.getEquipmentInfoFilterExport(name,equipmentNo,isonline,officeId);
	}

	@Override
	public List<EquipmentInfo> getManureExport(String name,String equipmentNo,Integer isonline,String officeId) {
		return equipmentInfoDao.getManureExport(name,equipmentNo,isonline,officeId);
	}

	@Override
	public List<EquipmentInfo> getCollectionExport(String name,String equipmentNo,Integer isonline,String officeId) {
		return equipmentInfoDao.getCollectionExport(name,equipmentNo,isonline,officeId);
	}

	@Override
	public int insertValveImport(EquipmentInfo equipmentInfo) {
		return equipmentInfoDao.insertValveImport(equipmentInfo);
	}

}

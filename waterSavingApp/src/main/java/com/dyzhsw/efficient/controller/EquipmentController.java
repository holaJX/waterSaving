package com.dyzhsw.efficient.controller;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.dto.AddEquipmentInfoDTO;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.*;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pjx
 * @Date: 2018/12/7 13:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/equipment")
@Api(value = "设备列表相关接口")
public class EquipmentController {


    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private SysService sysService;

    @Autowired
    private FkqValveInfoService fkqValveInfoService;

    @Autowired
    private GsmMeterInfoService gsmMeterInfoService;

    @Autowired
    private WmTerminalinfoService wmTerminalinfoService;

    @Autowired
    private EquipmentRelationService equipmentRelationService;

    @ResponseBody
    @ApiOperation(value = "添加设备", notes = "在设备列表中添加新设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "设备名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equipmentType", value = "设备类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "officeId", value = "设备归属机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "groupingId", value = "设备分组id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pointlng", value = "经度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pointlat", value = "纬度", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "maxValue", value = "上限值", required = false, dataType = "String"),
            @ApiImplicitParam(name = "minValue", value = "下限值", required = false, dataType = "String"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "valveCtrlId", value = "绑定的控制器id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "piezometerId", value = "绑定的压力表id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "flowmeterId", value = "绑定的流量计id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/addEquipmentInfo", method = RequestMethod.POST)
    public BaseResponse addEquipmentInfo(EquipmentInfo equipmentInfo, String valveCtrlId, String piezometerId, String flowmeterId) {
        if (equipmentInfo == null) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "设备不能为空");
        }
        if (StringUtils.isEmpty(equipmentInfo.getOfficeId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "设备归属不能为空");
        }
        AddEquipmentInfoDTO equipmentInfoDTO = new AddEquipmentInfoDTO();
        equipmentInfoDTO.setCurrentUserId(ContextUser.getUserId());
        equipmentInfo.setId(IDUtils.createUUID());
        equipmentInfo.setCreateBy(ContextUser.getUserId());
        equipmentInfo.setCreateTime(new Date());
        equipmentInfoDTO.setEquipmentInfo(equipmentInfo);
        equipmentInfoDTO.setFlowmeterId(flowmeterId);
        equipmentInfoDTO.setPiezometerId(piezometerId);
        equipmentInfoDTO.setValveCtrlId(valveCtrlId);
        BaseResponse baseResponse = equipmentInfoService.saveEquipmentTransaction(equipmentInfoDTO);
        return baseResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/getEquipmentList", method = RequestMethod.POST)
    @ApiOperation(value = "查询设备列表", notes = "根据用户信息获取水肥机、过滤器、阀控器、流量计、压力计等设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "equType", value = "设备类型", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "设备归属地", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "设备名称", required = false, paramType = "query", dataType = "String")
    })
    public BaseResponse getEquipmentList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "equType", required = false) String equType,
                                         @RequestParam(value = "officeId", required = false) String officeId,
                                         @RequestParam(value = "name", required = false) String name) {
        try {
            String currUserId = ContextUser.getUserId();
            SysRole role = sysService.selectByUserId(currUserId);
            if (role == null) {
                return BaseResponseUtil.error(ResultEnum.INVALID_ROLEINFO.getStateCode(), ResultEnum.INVALID_ROLEINFO.getMessage());
            }
            if (StringUtils.isEmpty(officeId)) {
                officeId = role.getOfficeId();//当前角色所属归属地
            }
            Map<String, String> map = new HashMap<>();
            map.put("type", equType);
            map.put("officeId", officeId);
            map.put("name", name);
            PageHelper.startPage(pageNo, pageSize, true);
            List<EquipmentInfo> currentList = equipmentInfoService.queryEquipmentListPage(map);
            PageInfo<EquipmentInfo> page = new PageInfo<>(currentList);
            List<EquipmentInfo> equipmentInfoList = equipmentInfoService.processEquipmentHandle(page.getList());
            page.setList(equipmentInfoList);
            return BaseResponseUtil.success(page);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    @ResponseBody
    @ApiOperation(value="获取可选采集设备列表", notes="根据机构id获取可选采集设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "机构id",paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "equType", value = "设备类型(1:压力表 2：流量计)",paramType = "query", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getOptionalsCollectEquipmentList", method = RequestMethod.POST)
    public BaseResponse<List<EquipmentInfo>> getOptionalsCollectEquipmentList(String officeId, Integer equType) {

        try {
            String currUserId = ContextUser.getUserId();
            SysRole role = sysService.selectByUserId(currUserId);
            if (StringUtils.isEmpty(officeId)) {
                officeId = role.getOfficeId();//当前角色所属归属地
            }
            List<EquipmentInfo> equipmentInfoList = equipmentInfoService.getOptionalsCollectEquipmentList(officeId, equType);
            return BaseResponseUtil.success(equipmentInfoList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value = "获取设备详情", notes = "根据设备id获取设备详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getEquipmentInfoByIdAndType", method = RequestMethod.POST)
    public BaseResponse getEquipmentInfoByIdAndType(@RequestParam(value = "id", required = true) String id) {

        EquipmentInfo equipmentInfo = equipmentInfoService.getEquipmentInfoByIdAndType(id);
        if (equipmentInfo != null) {
            return BaseResponseUtil.success(equipmentInfo);
        } else {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "该记录不存在");
        }
    }

    @ResponseBody
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "设备名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "equipmentType", value = "设备类型", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "officeId", value = "设备归属机构id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "groupingId", value = "设备分组id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pointlng", value = "经度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pointlat", value = "纬度", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "maxValue", value = "上限值", required = false, dataType = "String"),
            @ApiImplicitParam(name = "minValue", value = "下限值", required = false, dataType = "String"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(name = "piezometerId", value = "绑定的压力表id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "flowmeterId", value = "绑定的流量计id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateEquipmentInfo", method = RequestMethod.POST)
    public BaseResponse updateEquipmentInfo(EquipmentInfo equipmentInfo, String piezometerId, String flowmeterId) {

        try {

            String currUserId = ContextUser.getUserId();
            if (StringUtils.isEmpty(equipmentInfo.getId())) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "设备id不能为空");
            }
            if (equipmentInfo.getEquipmentType() == 1 || equipmentInfo.getEquipmentType() == 4) {
                if (StringUtils.isEmpty(equipmentInfo.getEquipmentNo())) {
                    return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "设备编号不能为空");
                }
            }
            if (equipmentInfoService.updateEquipmentInfo(equipmentInfo) > 0) {
                if (equipmentInfo.getEquipmentType() == 1) {  // 水肥机
                    // 判断设备编号是否更改
                    WmTerminalinfo wmTerminalinfo = wmTerminalinfoService.selectByEquNo(equipmentInfo.getEquipmentNo());
                    if (wmTerminalinfo == null) {
                        // 水肥机信息表创建一条相关数据
                        WmTerminalinfo newWmTerminalinfo = new WmTerminalinfo();
                        newWmTerminalinfo.setId(IDUtils.createUUID());
                        newWmTerminalinfo.setMaxValue(equipmentInfo.getMaxValue());
                        newWmTerminalinfo.setMinValue(equipmentInfo.getMinValue());
                        newWmTerminalinfo.setTerminalId(equipmentInfo.getEquipmentNo());
                        newWmTerminalinfo.setCreateTime(new Date());
                        wmTerminalinfoService.insertInfo(wmTerminalinfo);
                    }
                } else if (equipmentInfo.getEquipmentType() == 2) {  // 过滤器（没有单独的基础数据表）
                    // 1.判断是否需要做绑定操作 (绑定流量计)
                    if (flowmeterId != null) {
                        EquipmentRelation equipmentRelation = equipmentRelationService.selectByPercolatorId(equipmentInfo.getId());
                        if (equipmentRelation == null) {
                            EquipmentRelation newEquipmentRelation = new EquipmentRelation();
                            newEquipmentRelation.setId(IDUtils.createUUID());
                            newEquipmentRelation.setFlowmeterId(flowmeterId);
                            newEquipmentRelation.setPercolatorId(equipmentInfo.getId());
                            newEquipmentRelation.setCreateBy(currUserId);
                            newEquipmentRelation.setCreateTime(new Date());
                            equipmentRelationService.addEquRelationInfo(newEquipmentRelation);
                        } else {
                            equipmentRelation.setFlowmeterId(flowmeterId);
                            equipmentRelationService.updateEquRelationInfo(equipmentRelation);
                        }
                    }
                } else if (equipmentInfo.getEquipmentType() == 3) {  // 阀门控制器

                    // 1.判断设备编号是否更改
                    FkqValveInfo fkqValveInfo = fkqValveInfoService.selectByEquId(equipmentInfo.getId());
                    if (fkqValveInfo == null) {
                        // 阀控信息表创建一条相关数据
                        FkqValveInfo newFkqValveInfo = new FkqValveInfo();
                        newFkqValveInfo.setId(IDUtils.createUUID());
                        newFkqValveInfo.setUpdateTime(new Date());
                        newFkqValveInfo.setEquipmentId(equipmentInfo.getId());
                        fkqValveInfoService.addFkqValveInfo(newFkqValveInfo);
                    }

                    // 2.判断是否需要做绑定操作（绑定压力计）
                    if (piezometerId != null) {
                        EquipmentRelation equipmentRelation = equipmentRelationService.selectByValveControllerId(equipmentInfo.getId());
                        if (equipmentRelation == null) {
                            EquipmentRelation mewEquipmentRelation = new EquipmentRelation();
                            mewEquipmentRelation.setId(IDUtils.createUUID());
                            mewEquipmentRelation.setPiezometerId(piezometerId);
                            mewEquipmentRelation.setValveControllerId(equipmentInfo.getId());
                            mewEquipmentRelation.setCreateBy(currUserId);
                            mewEquipmentRelation.setCreateTime(new Date());
                            equipmentRelationService.addEquRelationInfo(mewEquipmentRelation);
                        } else {
                            equipmentRelation.setPiezometerId(piezometerId);
                            equipmentRelationService.updateEquRelationInfo(equipmentRelation);
                        }
                    }
                } else if (equipmentInfo.getEquipmentType() == 4) {  // 流量计
                    // 判断设备编号是否更改
                    GsmMeterInfo gsmMeterInfo = gsmMeterInfoService.selectByEquNo(equipmentInfo.getEquipmentNo());
                    if (gsmMeterInfo == null) {
                        GsmMeterInfo newGsmMeterInfo = new GsmMeterInfo();
                        newGsmMeterInfo.setId(IDUtils.createUUID());
                        newGsmMeterInfo.setAddress(equipmentInfo.getEquipmentNo());
                        newGsmMeterInfo.setCreateTime(new Date());
                        newGsmMeterInfo.setUpdateTime(new Date());
                        gsmMeterInfoService.addGsmMeterInfo(newGsmMeterInfo);
                    }
                } else if (equipmentInfo.getEquipmentType() == 5) {  // 压力计（没有单独的基础数据表）
                    // nothing to do
                }
                return BaseResponseUtil.success();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }

}

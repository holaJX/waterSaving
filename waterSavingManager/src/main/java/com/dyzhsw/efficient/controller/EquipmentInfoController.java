package com.dyzhsw.efficient.controller;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.config.redis.RedisUtil;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.*;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipmentInfo")
@Api(value = "设备列表相关接口")
public class EquipmentInfoController {


    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private FkqValveInfoService fkqValveInfoService;

    @Autowired
    private GsmMeterInfoService gsmMeterInfoService;

    @Autowired
    private WmTerminalinfoService wmTerminalinfoService;

    @Autowired
    private EquipmentRelationService equipmentRelationService;

    @Autowired
    private SysService sysService;

    @Autowired
    private EquipmentLogService equipmentLogService;



    /**
     * 施肥机接口地址
     */
    @Value("${jxwater.manureservice}")
    private String manureHost;
    /**
     * 阀控器接口地址
     */
    @Value("${jxwater.microcontrol}")
    private String microcontrolHost;



    @ResponseBody
    @ApiOperation(value="添加设备", notes="在设备列表中添加新设备")
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
            @ApiImplicitParam(name = "relationEquId", value = "绑定的设备的id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "telemetryStationAddr", value = "网关地址", required = false, dataType = "String")
//            @ApiImplicitParam(name = "valveCtrlId", value = "绑定的控制器id", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "piezometerId", value = "绑定的压力表id", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "flowmeterId", value = "绑定的流量计id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/addEquipmentInfo", method = RequestMethod.POST)
    public BaseResponse addEquipmentInfo(EquipmentInfo equipmentInfo, String valveCtrlId, String relationEquId, String telemetryStationAddr, HttpServletRequest request) {


        if (StringUtils.isEmpty(equipmentInfo.getName()) || StringUtils.isEmpty(equipmentInfo.getEquipmentNo()) || equipmentInfo.getEquipmentType() == null
                || StringUtils.isEmpty(equipmentInfo.getOfficeId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        if (!StringUtils.isEmpty(equipmentInfo.getMaxValue()) && !StringUtils.isEmpty(equipmentInfo.getMinValue())) {
            if (Integer.valueOf(equipmentInfo.getMinValue()) > Integer.valueOf(equipmentInfo.getMaxValue())) {
                return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), "下限值不能大于上限值");
            }
        }

        SysOffice checkOffice = sysService.getOfficeInfoById(equipmentInfo.getOfficeId());
        if (checkOffice != null && !checkOffice.getType().equals("3")) {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "设备归属只能指定为首部级别");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        try {

            EquipmentInfo tempInfo = equipmentInfoService.selectByEquNo(equipmentInfo.getEquipmentNo());
            if (tempInfo != null) {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "设备编号已存在！");
            }

            equipmentInfo.setId(IDUtils.createUUID());
            equipmentInfo.setCreateBy(currUserId);
            equipmentInfo.setCreateTime(new Date());

            if (equipmentInfoService.addEquipmentInfo(equipmentInfo) > 0) {
                if (equipmentInfo.getEquipmentType() == 1) {  // 水肥机
                    // 1.水肥机信息表创建一条相关数据
                    WmTerminalinfo wmTerminalinfo = new WmTerminalinfo();
                    wmTerminalinfo.setId(IDUtils.createUUID());
                    wmTerminalinfo.setMaxValue(equipmentInfo.getMaxValue());
                    wmTerminalinfo.setMinValue(equipmentInfo.getMinValue());
                    wmTerminalinfo.setTerminalId(equipmentInfo.getEquipmentNo());
                    wmTerminalinfo.setCreateTime(new Date());
                    wmTerminalinfoService.insertInfo(wmTerminalinfo);
                } else if (equipmentInfo.getEquipmentType() == 2) {  // 过滤器（没有单独的基础数据表）
                    // 1.判断是否需要做绑定操作 (绑定流量计)
                    if (relationEquId != null && !relationEquId.equals("")) {
                        EquipmentRelation equipmentRelation = new EquipmentRelation();
                        equipmentRelation.setId(IDUtils.createUUID());
                        equipmentRelation.setFlowmeterId(relationEquId);
                        equipmentRelation.setPercolatorId(equipmentInfo.getId());
                        equipmentRelation.setCreateBy(currUserId);
                        equipmentRelation.setCreateTime(new Date());
                        equipmentRelationService.addEquRelationInfo(equipmentRelation);
                    }
                } else if (equipmentInfo.getEquipmentType() == 3) {  // 阀门控制器
                    // 1.阀控信息表创建一条相关数据
                    FkqValveInfo fkqValveInfo = new FkqValveInfo();
                    fkqValveInfo.setId(IDUtils.createUUID());
                    fkqValveInfo.setTelemetryStationAddr(telemetryStationAddr);
                    fkqValveInfo.setUpdateTime(new Date());
                    fkqValveInfo.setEquipmentId(equipmentInfo.getId());
                    fkqValveInfoService.addFkqValveInfo(fkqValveInfo);
                    // 2.判断是否需要做绑定操作（绑定压力计）
                    if (relationEquId != null && !relationEquId.equals("")) {
                        EquipmentRelation equipmentRelation = new EquipmentRelation();
                        equipmentRelation.setId(IDUtils.createUUID());
                        equipmentRelation.setPiezometerId(relationEquId);
                        equipmentRelation.setValveControllerId(equipmentInfo.getId());
                        equipmentRelation.setCreateBy(currUserId);
                        equipmentRelation.setCreateTime(new Date());
                        equipmentRelationService.addEquRelationInfo(equipmentRelation);
                    }
                } else if (equipmentInfo.getEquipmentType() == 4) {  // 流量计
                    // 1.流量计信息表创建一条相关数据
                    GsmMeterInfo gsmMeterInfo = new GsmMeterInfo();
                    gsmMeterInfo.setId(IDUtils.createUUID());
                    gsmMeterInfo.setAddress(equipmentInfo.getEquipmentNo());
                    gsmMeterInfo.setCreateTime(new Date());
                    gsmMeterInfo.setUpdateTime(new Date());
                    gsmMeterInfoService.addGsmMeterInfo(gsmMeterInfo);

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


    @ResponseBody
    @ApiOperation(value="查询控制设备列表", notes="根据用户信息获取水肥机、过滤器、阀控器设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "equType", value = "设备类型（1：水肥机 2：过滤器 3：阀控器）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "设备名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "归属id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "groupId", value = "阀控器分组id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isonline", value = "在线状态(0:离线 1:在线)", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getEquipmentList", method = RequestMethod.POST)
    public BaseResponse<PageInfo<EquipmentInfo>> getEquipmentList(Integer pageNo, Integer pageSize, String equType, String name, String equipmentNo, String officeId, String groupId, Integer isonline, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";
            String isSys = map.get("isSys")+"";

            PageInfo<EquipmentInfo> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            if (!StringUtils.isEmpty(officeId)) {
                currOfficeId = officeId;
            }
            pageInfo = equipmentInfoService.getEquipmentListByEquTypeAndOfficeId(pageNo, pageSize, currOfficeId, equType, name, equipmentNo, groupId, isonline, isSys, officeId);

            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="查询采集设备列表", notes="根据用户信息获取采集设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "设备名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "affiliationId", value = "归属id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isonline", value = "在线状态(0:离线 1:在线)", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getCollectEquipmentList", method = RequestMethod.POST)
    public BaseResponse<PageInfo<EquipmentInfo>> getCollectEquipmentList(Integer pageNo, Integer pageSize, String name, String equipmentNo, String affiliationId, Integer isonline, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";
            String currOfficeId = map.get("officeId")+"";
            String isSys = map.get("isSys")+"";

            PageInfo<EquipmentInfo> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            pageInfo = equipmentInfoService.getCollectEquipmentListByOfficeId(pageNo, pageSize, currOfficeId, name, equipmentNo, affiliationId, isonline, isSys);

            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取设备详情", notes="根据设备id获取设备详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String")
//            @ApiImplicitParam(name = "equType", value = "设备类型（1、施肥机 2、过滤器 3、阀控器 4、流量计 5、压力表）", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getEquipmentInfoByIdAndType", method = RequestMethod.POST)
    public BaseResponse<EquipmentInfo> getEquipmentInfoByIdAndType(String id, HttpServletRequest request) {

        if (StringUtils.isEmpty(id)) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            EquipmentInfo equipmentInfo = equipmentInfoService.getEquipmentInfoByIdAndType(id);
            return BaseResponseUtil.success(equipmentInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取可选采集设备列表", notes="根据机构id获取可选采集设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "机构id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equType", value = "设备类型(1:压力表 2：流量计)", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getOptionalsCollectEquipmentList", method = RequestMethod.POST)
    public BaseResponse<List<EquipmentInfo>> getOptionalsCollectEquipmentList(String officeId, Integer equType, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            List<EquipmentInfo> equipmentInfoList = equipmentInfoService.getOptionalsCollectEquipmentList(officeId, equType);
            return BaseResponseUtil.success(equipmentInfoList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="修改设备信息", notes="修改设备信息")
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
            @ApiImplicitParam(name = "relationEquId", value = "绑定的设备的id", required = false, dataType = "String")
//            @ApiImplicitParam(name = "piezometerId", value = "绑定的压力表id", required = false, dataType = "String"),
//            @ApiImplicitParam(name = "flowmeterId", value = "绑定的流量计id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateEquipmentInfo", method = RequestMethod.POST)
    public BaseResponse updateEquipmentInfo(EquipmentInfo equipmentInfo, String relationEquId,  HttpServletRequest request) {

        if (StringUtils.isEmpty(equipmentInfo.getId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            SysOffice checkOffice = sysService.getOfficeInfoById(equipmentInfo.getOfficeId());
            if (checkOffice != null && !checkOffice.getType().equals("3")) {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "设备归属只能指定为首部级别");
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
                        wmTerminalinfoService.insertInfo(newWmTerminalinfo);
                    }
                } else if (equipmentInfo.getEquipmentType() == 2) {  // 过滤器（没有单独的基础数据表）
                    // 1.判断是否需要做绑定操作 (绑定流量计)
                    if (!StringUtils.isEmpty(relationEquId)) {
                        EquipmentRelation equipmentRelation = equipmentRelationService.selectByPercolatorId(equipmentInfo.getId());
                        if (equipmentRelation == null) {
                            EquipmentRelation newEquipmentRelation = new EquipmentRelation();
                            newEquipmentRelation.setId(IDUtils.createUUID());
                            newEquipmentRelation.setFlowmeterId(relationEquId);
                            newEquipmentRelation.setPercolatorId(equipmentInfo.getId());
                            newEquipmentRelation.setCreateBy(currUserId);
                            newEquipmentRelation.setCreateTime(new Date());
                            equipmentRelationService.addEquRelationInfo(newEquipmentRelation);
                        } else {
                            equipmentRelation.setFlowmeterId(relationEquId);
                            equipmentRelationService.updateEquRelationInfo(equipmentRelation);
                        }
                    } else {
                        equipmentRelationService.deletedByFlowmeterId(equipmentInfo.getId());
                    }
                } else if (equipmentInfo.getEquipmentType() == 3) {  // 阀门控制器

                    // 判断是否需要做绑定操作（绑定压力计）
                    if (!StringUtils.isEmpty(relationEquId)) {
                        EquipmentRelation equipmentRelation = equipmentRelationService.selectByValveControllerId(equipmentInfo.getId());
                        if (equipmentRelation == null) {
                            EquipmentRelation mewEquipmentRelation = new EquipmentRelation();
                            mewEquipmentRelation.setId(IDUtils.createUUID());
                            mewEquipmentRelation.setPiezometerId(relationEquId);
                            mewEquipmentRelation.setValveControllerId(equipmentInfo.getId());
                            mewEquipmentRelation.setCreateBy(currUserId);
                            mewEquipmentRelation.setCreateTime(new Date());
                            equipmentRelationService.addEquRelationInfo(mewEquipmentRelation);
                        } else {
                            equipmentRelation.setPiezometerId(relationEquId);
                            equipmentRelationService.updateEquRelationInfo(equipmentRelation);
                        }
                    } else {
                        equipmentRelationService.deletedByValveControllerId(equipmentInfo.getId());
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


    @ResponseBody
    @ApiOperation(value="获取设备详情页报警信息列表", notes="根据设备编号获取报警信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equipmentType", value = "设备类型（1、施肥机 2、过滤器 3、阀控器 4、流量计 5、压力表）", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getEquInfoWarn", method = RequestMethod.POST)
    public BaseResponse<PageInfo<EquipmentWarn>> getEquInfoWarn(Integer pageNo, Integer pageSize, String equipmentNo, Integer equipmentType, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            PageInfo<EquipmentWarn> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            pageInfo = equipmentInfoService.getEquWarnInfoList(pageNo, pageSize, equipmentNo, equipmentType);

            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="删除设备", notes="根据设备id和设备类型删除设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "设备ids(逗号分隔的字符串)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "equipmentType", value = "设备类型（1、施肥机 2、过滤器 3、阀控器 4、流量计 5、压力表）", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/deletedEquipmentInfoById", method = RequestMethod.POST)
    public BaseResponse deletedEquipmentInfoById(String ids, Integer equipmentType, HttpServletRequest request) {


        if (StringUtils.isEmpty(ids) || equipmentType == null) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            equipmentInfoService.deletedEquipmentInfoById(currUserId, ids, equipmentType);

            return BaseResponseUtil.success();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取设备报警信息列表", notes="根据设备类型获取报警信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "equType", value = "设备类型（1、施肥机 2、过滤器 3、阀控器 4、流量计 5、压力表）", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "warnType", value = "报警类型", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "warnInfo", value = "报警信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "remarks", value = "备注信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间(2018-12-13 10:07:12)", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间(2018-12-13 10:07:12)", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getEquWarn", method = RequestMethod.POST)
    public BaseResponse<PageInfo<EquipmentWarn>> getEquWarn(Integer pageNo, Integer pageSize, Integer equType, Integer warnType, String warnInfo, String remarks, String startTime, String endTime, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            PageInfo<EquipmentWarn> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            pageInfo = equipmentInfoService.getEquWarnList(pageNo, pageSize, equType, warnType, warnInfo, remarks, startTime, endTime);

            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取设备类型列表", notes="获取设备类型列表")
    @RequestMapping(value = "/getEquTypeList", method = RequestMethod.POST)
    public BaseResponse<List<SysDict>> getEquTypeList(Integer equType, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            List<SysDict> equTypeList = sysService.getEquTypeList();

            return BaseResponseUtil.success(equTypeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取设备报警类型列表", notes="根据设备类型获取设备报警类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equType", value = "设备类型（1、施肥机 4、流量计 5、压力表）", required = true, dataType = "Integer"),
    })
    @RequestMapping(value = "/getEquWarnTypeList", method = RequestMethod.POST)
    public BaseResponse<List<SysDict>> getEquWarnTypeList(Integer equType, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            List<SysDict> equWarnTypeList = sysService.getEquWarnTypeListByEquType(equType);

            return BaseResponseUtil.success(equWarnTypeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="删除报警信息", notes="根据id删除报警信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "逗号分隔的id字符串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/deletedEquWarn", method = RequestMethod.POST)
    public BaseResponse deletedEquWarn(String ids, HttpServletRequest request) {


        if (StringUtils.isEmpty(ids)) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            equipmentInfoService.deletedEquWarn(ids);

            return BaseResponseUtil.success();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取报警信息提示数量", notes="获取报警信息提示数量")
    @RequestMapping(value = "/getWarnNum", method = RequestMethod.POST)
    public BaseResponse<Integer> getWarnNum(HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            String beforeWarnTime = (String) redisUtil.get("warnTime");

            int warnNum = 0;
            if (beforeWarnTime != null) {
                warnNum = equipmentInfoService.getWarnNum(beforeWarnTime);
            }

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(date);
            redisUtil.set("warnTime", time);

            return BaseResponseUtil.success(warnNum);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="阀控器开关控制", notes="根据设备编号控制设备开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "设备id(多个设备的话以逗号分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "state1", value = "线路1（1表示关阀，2表示开阀）", required = false, dataType = "String"),
            @ApiImplicitParam(name = "state2", value = "线路2（1表示关阀，2表示开阀）", required = false, dataType = "String"),
            @ApiImplicitParam(name = "allState", value = "1表示全关，2表示全开", required = false, dataType = "String")
    })
    @RequestMapping(value = "/fkqControlReq", method = RequestMethod.POST)
    public BaseResponse fkqControlReq(String ids, String state1, String state2, String allState, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            if (allState != null) {
                return microValveReq(ids, allState, allState);
            } else {
                return microValveReq(ids, state1, state2);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    private BaseResponse microValveReq(String ids, String control1Status, String control2Status) throws IOException {
		/*远程控制*/
        Map<String, Object> paramMap = new HashMap<>();
        //创建请求对象
        String url = this.microcontrolHost + "/MicroControl/valveControl/control";
        paramMap.put("equipmentId", ids);
        paramMap.put("control1Status",control1Status);
        paramMap.put("control2Status",control2Status);
        String result = HttpClientTools.doPost(url, paramMap);
        JSONObject obj = JSONObject.parseObject(result);
        @SuppressWarnings("rawtypes")
        BaseResponse res = new BaseResponse();
        if (obj != null) {
            if (obj.getInteger("stateCode") != null) {
                res.setStateCode(obj.getInteger("stateCode"));
            } else {
                res.setStateCode(500);
            }
            res.setMessage(obj.getString("message"));
            res.setObject(obj);
        }
        //保存手动控制日志
        EquipmentLog equLog = new EquipmentLog();
        equLog.setId(IDUtils.createUUID());
        if("200".equals(obj.getString("stateCode"))) {
            equLog.setType(1);
        } else {
            equLog.setType(2);
        }
        String content = "";
        if (control1Status.equals("1")) {
            content = content + "线路一：关阀；";
        } else {
            content = content + "线路一：开阀；";
        }
        if (control2Status.equals("1")) {
            content = content + "线路二：关阀";
        } else {
            content = content + "线路二：开阀";
        }
        content = content + res.getMessage();
        equLog.setTitle(content);
        equLog.setCreateTime(new Date());
        equLog.setRemarks(obj.toJSONString());
        String[] idAry = ids.split(",");
        for (String id : idAry) {
            EquipmentInfo equipmentInfo = equipmentInfoService.getEquipmentInfoById(id);
            equLog.setEquipmentNo(equipmentInfo.getEquipmentNo());
            equipmentLogService.addEquipmentLog(equLog);
        }
        return res;
    }



    @ResponseBody
    @ApiOperation(value="施肥机开关控制", notes="根据设备编号控制设备开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentNos", value = "设备编号(多个设备的话以逗号分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "wmState", value = "0表示关，1表示开", required = true, dataType = "String")
    })
    @RequestMapping(value = "/wmTerminalReq", method = RequestMethod.POST)
    public BaseResponse wmTerminalReq(String equipmentNos, String wmState, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            int successNum = 0;
            int failNum = 0;

            String[] equipmentNoAry = equipmentNos.split(",");
            for (String equNo : equipmentNoAry) {
                Boolean result = wmControllerReq(equNo, wmState);
                if (result) {
                    successNum = successNum + 1;
                } else {
                    failNum = failNum + 1;
                }
            }
            String message = "";
            if (successNum != 0 && failNum != 0) {
                message = "成功 "+successNum+"个" + "失败 "+failNum+"个";
            }
            if (successNum != 0 && failNum == 0) {
                message = "成功 "+successNum+"个";
            }
            if (successNum == 0 && failNum != 0) {
                message = "失败 "+failNum+"个";
            }

            return BaseResponseUtil.success(ResultEnum.SUCCESS.getStateCode(), message, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    private Boolean wmControllerReq(String equipmentNo, String switchState) throws IOException {
		/*远程控制*/
        Map<String, Object> paramMap = new HashMap<>();
        //创建请求对象
        String url = this.manureHost + "/jxwatermanureservice/terminalControllService/terminalControlReq";
        paramMap.put("switchState", switchState);
        paramMap.put("terminalId",equipmentNo);
        String result =HttpClientTools.doPost(url, paramMap);
        JSONObject obj = JSONObject.parseObject(result);
        if (obj != null) {
            @SuppressWarnings("rawtypes")
            BaseResponse res = new BaseResponse();
            res.setStateCode(obj.getInteger("stateCode"));
            res.setMessage(obj.getString("message"));
            res.setObject(obj);
            //保存手动控制日志
            EquipmentLog equLog = new EquipmentLog();
            equLog.setId(IDUtils.createUUID());
            if("200".equals(obj.getString("stateCode"))) {
                equLog.setType(1);
                if (switchState.equals("0")) {
                    equLog.setTitle("关阀成功");
                } else {
                    equLog.setTitle("开阀成功");
                }
                equLog.setEquipmentNo(equipmentNo);
                equLog.setCreateTime(new Date());
                equLog.setRemarks(obj.toJSONString());
                equipmentLogService.addEquipmentLog(equLog);
                return true;
            } else {
                equLog.setType(2);
                if (switchState.equals("0")) {
                    equLog.setTitle("关阀失败");
                } else {
                    equLog.setTitle("开阀失败");
                }
                equLog.setEquipmentNo(equipmentNo);
                equLog.setCreateTime(new Date());
                equLog.setRemarks(obj.toJSONString());
                equipmentLogService.addEquipmentLog(equLog);
                return false;
            }
        } else {
            EquipmentLog equLog = new EquipmentLog();
            equLog.setId(IDUtils.createUUID());
            equLog.setType(2);
            if (switchState.equals("0")) {
                equLog.setTitle("关阀失败");
            } else {
                equLog.setTitle("开阀失败");
            }
            equLog.setEquipmentNo(equipmentNo);
            equLog.setCreateTime(new Date());
            equLog.setRemarks("服务异常");
            equipmentLogService.addEquipmentLog(equLog);
            return false;
        }

    }



    @ResponseBody
    @ApiOperation(value="获取设备详情页数据图信息", notes="根据设备id获取详情页数据图信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "chartType", value = "图表类型（1、曲线图 2、列表）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getEquChartInfo", method = RequestMethod.POST)
    public BaseResponse<Map<String, Object>> getEquChartInfo(String id, Integer chartType, Integer pageNo, Integer pageSize, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            Map<String, Object> resultMap = equipmentInfoService.getEquChartInfo(id, chartType, pageNo, pageSize);

            return BaseResponseUtil.success(resultMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    
	/**
	 * 获取流量计
	 */
	@ResponseBody
    @ApiOperation(value="获取流量计", notes="获取流量计")
    @RequestMapping(value = "/getLlj", method = RequestMethod.POST) 
	public BaseResponse<Map<String,Object>> getLlj() {
		List<Map<String,Object>>map = new ArrayList<>();
		map= equipmentInfoService.getFkq();
		return BaseResponseUtil.success(200, "查询成功", map);
	}
	
	/**
	 * 获取施肥机
	 */
	@ResponseBody
    @ApiOperation(value="获取施肥机", notes="获取施肥机")
    @RequestMapping(value = "/getWm", method = RequestMethod.POST) 
	public BaseResponse<Map<String,Object>> getWm() {
		List<Map<String,Object>>map = new ArrayList<>();
		map= equipmentInfoService.getWm();
		return BaseResponseUtil.success(200, "查询成功", map);
	}

}

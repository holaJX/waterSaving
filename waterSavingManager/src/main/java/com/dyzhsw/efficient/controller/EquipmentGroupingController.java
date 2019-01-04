package com.dyzhsw.efficient.controller;


import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.dao.EquipmentInfoDao;
import com.dyzhsw.efficient.dao.SysOfficeDao;
import com.dyzhsw.efficient.entity.*;
import com.dyzhsw.efficient.service.EquipmentGroupingService;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.SysOfficeService;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipmentGroup")
@Api(value = "设备分组相关接口")
public class EquipmentGroupingController {


    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private EquipmentGroupingService equipmentGroupingService;

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private SysService sysService;



    @ResponseBody
    @ApiOperation(value="添加分组", notes="在分组列表中添加新分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "分组名称", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "groupType", value = "分组类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "officeId", value = "归属部门的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isenabled", value = "启用状态(0:启用 1:不启用)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String")
    })
    @RequestMapping(value = "/addEquipmentGroup", method = RequestMethod.POST)
    public BaseResponse addEquipmentGroup(EquipmentGrouping equipmentGrouping, HttpServletRequest request) {

        if (StringUtils.isEmpty(equipmentGrouping.getName()) || StringUtils.isEmpty(equipmentGrouping.getOfficeId())) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        SysOffice checkOffice = sysService.getOfficeInfoById(equipmentGrouping.getOfficeId());
        if (checkOffice != null && !checkOffice.getType().equals("3")) {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "设备归属只能指定为首部级别");
        }

        equipmentGrouping.setId(IDUtils.createUUID());
        equipmentGrouping.setCreateBy(currUserId);
        equipmentGrouping.setCreateTime(new Date());

        if (equipmentGroupingService.addEquipmentGroup(equipmentGrouping) > 0) {
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取分组列表", notes="获取分组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "分组名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "officeId", value = "归属机构id", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getGroupingList", method = RequestMethod.POST)
    public BaseResponse<PageInfo<EquipmentGrouping>> getGroupingList(Integer pageNo, Integer pageSize, String name, String officeId, HttpServletRequest request) {

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

            PageInfo<EquipmentGrouping> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            if (!StringUtils.isEmpty(officeId)) {
                currOfficeId = officeId;
            }
            pageInfo = equipmentGroupingService.getGroupingList(pageNo, pageSize, name, currOfficeId, isSys, officeId);

            return BaseResponseUtil.success(pageInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="获取分组列表(添加阀控器)", notes="根据机构id获取分组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "首部系统id", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/getGroupListByOfficeId", method = RequestMethod.POST)
    public BaseResponse<List<EquipmentGrouping>> getGroupListByOfficeId(String officeId, HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";


            List<EquipmentGrouping> equipmentGroupingList = equipmentGroupingService.getGroupListByOfficeId(officeId);

            return BaseResponseUtil.success(equipmentGroupingList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="删除分组", notes="根据分组id删除分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "分组的id(多个情况逗号分隔)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deletedGroupingInfoById", method = RequestMethod.POST)
    public BaseResponse deletedGroupingInfoById(String ids, HttpServletRequest request) {

        if (StringUtils.isEmpty(ids)) {
            return BaseResponseUtil.error(ResultEnum.INVALID_PARAMETER.getStateCode(), ResultEnum.INVALID_PARAMETER.getMessage());
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
        String currUserId = map.get("id")+"";

        if (equipmentGroupingService.deletedGroupingInfoById(currUserId, ids)) {
            return BaseResponseUtil.success();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="获取分组详情", notes="根据分组id获取分组详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分组的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getGroupingInfo", method = RequestMethod.POST)
    public BaseResponse<EquipmentGrouping> getGroupingInfo(String id, Integer pageNo, Integer pageSize, HttpServletRequest request) {

        try {

            PageInfo<EquipmentInfo> pageInfo = new PageInfo<>();
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;

            EquipmentGrouping equipmentGrouping = equipmentGroupingService.selectGroupInfoById(pageNo, pageSize, pageInfo, id);
            return BaseResponseUtil.success(equipmentGrouping);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="修改分组信息", notes="根据分组id修改分组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分组的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "分组名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "groupType", value = "分组类型", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "officeId", value = "归属部门的id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isenabled", value = "启用状态(0:启用 1:不启用)", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String")
    })
    @RequestMapping(value = "/updateGroupingInfo", method = RequestMethod.POST)
    public BaseResponse updateGroupingInfo(EquipmentGrouping equipmentGrouping, HttpServletRequest request) {

        if (StringUtils.isEmpty(equipmentGrouping.getId())) {
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

            SysOffice checkOffice = sysService.getOfficeInfoById(equipmentGrouping.getOfficeId());
            if (checkOffice != null && !checkOffice.getType().equals("3")) {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "设备归属只能指定为首部级别");
            }


            equipmentGrouping.setUpdateBy(currUserId);
            equipmentGrouping.setUpdateTime(new Date());

            if (equipmentGroupingService.updateGroupingInfo(equipmentGrouping) > 0) {
                return BaseResponseUtil.success();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="移除分组内设备", notes="根据设备信息将设备移除分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "逗号分隔的设备id字符串", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/deletedGroupEqu", method = RequestMethod.POST)
    public BaseResponse deletedGroupEqu(String ids, HttpServletRequest request) {


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

            boolean result = equipmentGroupingService.deletedGroupEqu(currUserId, ids);
            if (result) {
                return BaseResponseUtil.success();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }



    @ResponseBody
    @ApiOperation(value="向分组内添加设备时获取可选设备", notes="获取可选设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "分组归属机构id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getOptionalEqu", method = RequestMethod.POST)
    public BaseResponse getOptionalEqu(String officeId, HttpServletRequest request) {

        try {

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
            String currUserId = map.get("id")+"";

            List<EquipmentInfo> equipmentInfoList = equipmentInfoService.getOptionalEquByGroupId(officeId);

            return BaseResponseUtil.success(equipmentInfoList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


    @ResponseBody
    @ApiOperation(value="向分组内添加设备", notes="向分组内添加设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "设备id(多个设备逗号分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "groupId", value = "分组id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addEquToGroup", method = RequestMethod.POST)
    public BaseResponse addEquToGroup(String ids, String groupId, HttpServletRequest request) {

        if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(groupId)) {
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

            equipmentInfoService.addEquToGroup(ids, groupId);

            return BaseResponseUtil.success();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }




}

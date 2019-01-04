package com.dyzhsw.efficient.controller;


import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.EquipmentGrouping;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.service.EquipmentGroupingService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/equipmentGroup")
@Api(value = "设备分组相关接口")
public class EquipmentGroupingController {


    @Autowired
    private EquipmentGroupingService equipmentGroupingService;

    @ResponseBody
    @ApiOperation(value = "根据归属地id获取分组", notes = "获取分组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officeId", value = "归属地id", required = true,paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/getGroupListByOfficeId", method = RequestMethod.POST)
    public BaseResponse getGroupListByOfficeId(@RequestParam(value = "officeId") String officeId) {
        List<EquipmentGrouping> equipmentGroupingList = equipmentGroupingService.getGroupListByOfficeId(officeId);
        return BaseResponseUtil.success(equipmentGroupingList);
    }


    @ResponseBody
    @ApiOperation(value = "添加分组", notes = "在分组列表中添加新分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "分组名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "groupType", value = "分组类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "officeId", value = "归属部门的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isenabled", value = "启用状态(0:启用 1:不启用)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String")
    })
    @RequestMapping(value = "/addEquipmentGroup", method = RequestMethod.POST)
    public BaseResponse addEquipmentGroup(EquipmentGrouping equipmentGrouping) {

        String currUserId = ContextUser.getUserId();

        equipmentGrouping.setId(IDUtils.createUUID());
        equipmentGrouping.setCreateBy(currUserId);
        equipmentGrouping.setCreateTime(new Date());

        if (equipmentGroupingService.addEquipmentGroup(equipmentGrouping) > 0) {
            return BaseResponseUtil.success();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }


//    @ResponseBody
//    @ApiOperation(value = "获取分组列表", notes = "获取分组列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
//            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer")
//    })
//    @RequestMapping(value = "/getGroupingList", method = RequestMethod.POST)
//    public BaseResponse getGroupingList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
////        PageInfo<EquipmentGrouping> pageInfo = new PageInfo<>();
////        pageInfo = equipmentGroupingService.getGroupListByOfficeId(pageNo, pageSize);
//        return BaseResponseUtil.success(null);
//
//    }


    @ResponseBody
    @ApiOperation(value = "删除分组", notes = "根据分组id删除分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分组的id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deletedGroupingInfoById", method = RequestMethod.POST)
    public BaseResponse deletedGroupingInfoById(String id, HttpServletRequest request) {

        String currUserId = ContextUser.getUserId();
        if (equipmentGroupingService.deletedGroupingInfoById(currUserId, id)) {
            return BaseResponseUtil.success();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }


    @ResponseBody
    @ApiOperation(value = "获取分组详情", notes = "根据分组id获取分组详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分组的id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/getGroupingInfo", method = RequestMethod.POST)
    public BaseResponse getGroupingInfo(String id, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        PageInfo<EquipmentInfo> pageInfo = new PageInfo<>();
        EquipmentGrouping equipmentGrouping = equipmentGroupingService.selectGroupInfoById(pageNo, pageSize, pageInfo, id);
        return BaseResponseUtil.success(equipmentGrouping);

    }


    @ResponseBody
    @ApiOperation(value = "修改分组信息", notes = "根据分组id修改分组信息")
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

        String currUserId = ContextUser.getUserId();
        equipmentGrouping.setUpdateBy(currUserId);
        equipmentGrouping.setUpdateTime(new Date());
        if (equipmentGroupingService.updateGroupingInfo(equipmentGrouping) > 0) {
            return BaseResponseUtil.success();
        } else {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "修改失败");
        }

    }


}

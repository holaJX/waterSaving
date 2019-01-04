package com.dyzhsw.efficient.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyzhsw.efficient.dto.SysAreaDTO;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: pjx
 * @Date: 2018/12/11 11:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/area")
@Api(value = "区域相关接口")
public class AreaController {
   @Autowired
    private SysService sysService;

    @ResponseBody
    @ApiOperation(value = "获取区域列表", notes = "获取区域树")
    @RequestMapping(value = "/treeList", method = RequestMethod.POST)
    public BaseResponse treeList(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("parentId",'0');
        List<SysAreaDTO> areas = sysService.getAreas(map);
        return BaseResponseUtil.success(areas);
    }
}

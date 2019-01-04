package com.dyzhsw.efficient.controller;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: pjx
 * @Date: 2018/12/12 10:58
 * @Version 1.0
 */
@RestController
@RequestMapping("/office")
@Api(value = "机构相关接口(机构分为公司、片区、首部系统三个级别)")
public class SysOfficeController {
    @Autowired
    private SysService sysService;

    @ResponseBody
    @ApiOperation(value="获取机构列表", notes="获取机构列表")
    @RequestMapping(value = "/getSysOfficeList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAll", value = "isAll=0获取所有组织机构；不传该参数则获取当前用户角色所属的组织机构", required = false, dataType = "String")
    })
    public BaseResponse<List<SysOffice>> getSysOfficeList(@RequestParam(value = "isAll",required = false) String isAll) {

        try {
            String currUserId = ContextUser.getUserId();
            String officeId = sysService.getCurrOfficeId(currUserId);
            List<SysOffice> sysOfficeList = new ArrayList<>();
            if (!StringUtils.isEmpty(isAll) && isAll.equals("0")) {
                sysOfficeList = sysService.getAllOfficeTree();
            } else {
                SysOffice sysOffice = sysService.getCurrOfficeTree(officeId);
                sysOfficeList.add(sysOffice);
            }
            return BaseResponseUtil.success(sysOfficeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());

    }
}

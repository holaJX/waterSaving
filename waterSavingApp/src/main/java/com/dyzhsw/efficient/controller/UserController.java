package com.dyzhsw.efficient.controller;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.SysUser;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: pjx
 * @Date: 2018/12/6 15:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户相关接口")
public class UserController {

    @Autowired
    private SysService sysService;

    @ResponseBody
    @RequestMapping(value = "/appLogin", method = RequestMethod.POST)
    @ApiOperation(value="App登录", notes="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户登录名", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true,paramType = "query", dataType = "String")
    })
    public BaseResponse login( String loginName, String password) {

        try {
            if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
                return BaseResponseUtil.error(ResultEnum.INVALID_LOGINNAME_PASSWORD.getStateCode(), ResultEnum.INVALID_LOGINNAME_PASSWORD.getMessage());
            }
            return sysService.login(loginName, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="修改密码", notes="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "老密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "rePwd", value = "确认新密码", required = true, dataType = "String")
    })
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "两次密码不一致");
        }
        String currUserId = ContextUser.getUserId();
        SysUser sysUser = sysService.selectById(currUserId);
        String encodePwd = DigestUtils.sha1Hex(oldPwd);
        if (sysUser.getPassword().equals(encodePwd)) {
            String newPwdEncode = DigestUtils.sha1Hex(newPwd);
            Map<String,String> map= new HashMap<>();
            map.put("id",sysUser.getId());
            map.put("newPwd",newPwdEncode);
            int i = sysService.changePwd(map);
            if (i > 0) {
                return BaseResponseUtil.success();
            } else {
                return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "修改密码失败");
            }
        } else {
            return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), "旧密码输入错误");
        }

    }


    /**
     * 测试接口是否走鉴权,(是否走了拦截器)
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Object test() {
        Claims claims = ContextUser.getClaims();
        System.out.println(claims);
        return BaseResponseUtil.success(ContextUser.getUserId());
    }

}

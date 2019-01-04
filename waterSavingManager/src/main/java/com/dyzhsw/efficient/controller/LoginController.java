package com.dyzhsw.efficient.controller;


import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
@Api(value = "登陆相关接口")
public class LoginController {


    @Autowired
    private SysService sysService;


    @ResponseBody
    @ApiOperation(value="用户登录", notes="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户登录名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse login(String loginName, String password){

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


    @ResponseBody
    @ApiOperation(value="退出登陆", notes="退出登陆")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BaseResponse logout(HttpServletRequest request){

        try {

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
            }
            String token = authHeader.substring(11);
            return sysService.loginOut(token);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BaseResponseUtil.error(ResultEnum.ERROR.getStateCode(), ResultEnum.ERROR.getMessage());
    }



    public static void main(String[] args) {
        System.out.println(IDUtils.createUUID());
    }

}

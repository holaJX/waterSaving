package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.dto.ResultObjectDTO;
import com.dyzhsw.efficient.service.SysService;
import com.dyzhsw.efficient.utils.BaseResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserControllerTest extends BaseJunit {


    @Test
    public void testLogin() {
        String loginName="admin";
        String password ="1";
        BaseResponse login = sysService.login(loginName, password);
        String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(clazz +"---->"+method+"--->:"+ login.getObject());
        assertTrue(login.getStateCode() == 200);
    }

    @Test
    public void testChangePwd() {
        fail("Not yet implemented");
    }

    @Test
    public void testTest() {
        fail("Not yet implemented");
    }

}

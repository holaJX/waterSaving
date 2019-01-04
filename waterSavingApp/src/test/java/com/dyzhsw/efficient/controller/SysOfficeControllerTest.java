package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.service.SysService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SysOfficeControllerTest extends BaseJunit {

    @Test
    public void testGetSysOfficeList() {

        String currUserId ="1"; //ContextUser.getUserId();
        String officeId = sysService.getCurrOfficeId(currUserId);
        SysOffice sysOffice = sysService.getCurrOfficeTree(officeId);
        assertTrue(sysOffice != null);
        System.out.println(sysOffice);

    }

}

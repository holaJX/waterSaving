package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import com.alibaba.druid.util.StringUtils;
import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.SysRole;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;

public class EquipmentControllerTest extends BaseJunit{

	@Autowired
	private EquipmentInfoService equipmentInfoService;
	@Test
	public void testAddEquipmentInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquipmentList() {

//		String currUserId = "1";
//		SysRole role = sysService.selectByUserId(currUserId);
//		String officeId =role.getOfficeId();
//		String equType="1";//施肥机
//		int pageNo=1;
//		int pageSize=15;
//		if (role == null) {
//			fail("角色为空");
//		}
//		if(StringUtils.isEmpty(officeId))
//		{
//			officeId = role.getOfficeId();//当前角色所属归属地
//		}
//		Map<String,String> map =new HashMap<>();
//		map.put("type",equType);
//		map.put("officeId",officeId);
//		PageInfo<EquipmentInfo> pageInfo =equipmentInfoService.queryEquipmentListPage(map);
//		assertTrue(pageInfo.getList().size()>0);
	}

	@Test
	public void testGetEquipmentListByOffice() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCollectEquipmentList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEquipmentInfoByIdAndType() {
		fail("Not yet implemented");
	}

}

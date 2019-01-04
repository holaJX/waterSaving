package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentWarn;
import com.dyzhsw.efficient.service.EquipmentWarnService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class EquipmentWarnControllerTest extends BaseJunit{
	
	@Autowired
	private EquipmentWarnService equipmentWarnService;

	@Test
	public void testSelectList() {

		PageHelper.startPage(1,10); 
		
		EquipmentInfo equipmentInfo=new EquipmentInfo();
		equipmentInfo.setName("");
		EquipmentWarn equipmentWarn=new EquipmentWarn();
		equipmentWarn.setEquipmentNo("");
		equipmentWarn.setEquipmentInfo(equipmentInfo);
	
	
		List<EquipmentWarn> list = equipmentWarnService.selectAll(equipmentWarn);
		System.out.println(list.size());
	}

}

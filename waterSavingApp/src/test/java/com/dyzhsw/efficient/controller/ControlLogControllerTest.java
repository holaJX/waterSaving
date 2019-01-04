package com.dyzhsw.efficient.controller;


import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.service.ControlLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class ControlLogControllerTest  extends BaseJunit {

	
	@Autowired
	private ControlLogService controlLogService;
	
	@Test
	public void testSelectList() {
		
		
		PageHelper.startPage(1,15); 
		String name="";
		String type="";
		String start="";
		String end="";
		Map<String,Object>map = new HashMap<>();
			map.put("name", name);
			map.put("type", type);
			map.put("start", start);
			map.put("end", end);		
		List<Map<String, Object>> list = controlLogService.selectcontrolAll(map);		
		System.out.println(list.size()+"==========");
	}

}

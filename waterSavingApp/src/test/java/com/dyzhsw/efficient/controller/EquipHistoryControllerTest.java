package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.entity.PressureHistory;
import com.dyzhsw.efficient.service.EquipHistoryDataService;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class EquipHistoryControllerTest  extends BaseJunit {
	//压力计历史数据
		@Autowired
		private EquipHistoryDataService equipHistoryDataService;
		
	@Test
	public void testPressureList() {
		PageHelper.startPage(1,10); 
		Map<String, String> map=new HashMap<String, String>();
		map.put("address", "");
		map.put("end", "");
		map.put("start", "");		
		List<PressureHistory> list = equipHistoryDataService.getPressureList(map);
		System.out.println(list.size());
	}

	@Test
	public void testWmmtercalcFlowList() {
		PageHelper.startPage(1,10); 
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("address", "");
		map.put("end", "");
		map.put("start", "");		
		List<Map<String, Object>> list = equipHistoryDataService.getWmMFList(map);
		System.out.println(list.size());
		
	}

	@Test
	public void testWaterListApp() {
		PageHelper.startPage(1,10); 
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("address", "");
		map.put("end", "");
		map.put("start", "");	
		List<Map<String, Object>> list = equipHistoryDataService.getWaterListApp(map);
		System.out.println(list.size());
	}

}

package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.service.GsmMeterCountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class GsmMeterCountControllerTest  extends BaseJunit {

	@Autowired
	private GsmMeterCountService gsmMetercountService;
	
	@Test
	public void testSelectcountwater() {
		PageHelper.startPage(1,16); 
		Map<String,Object>map = new HashMap<>();
			map.put("officeid", "");	
			map.put("equipmentNo", "");		
			map.put("start", "");
			map.put("end", "");		
		List<Map<String, Object>> list = gsmMetercountService.selectWatercount(map);		
		assertNull(list); 
	}

	@Test
	public void testAllcountwater() {
		Map<String,Object>map = new HashMap<>();
		List<Map<String, Object>> list = gsmMetercountService.selectWatercount(map);	
		Map<String,String> mapresult = new HashMap<String,String>();
		double total=0.0;		
		for(int i=0;i<list.size();i++){			
			total+=Double.parseDouble(list.get(i).get("userwaternum").toString());			
		}
		mapresult.put("total", total+"");
		assertNull(mapresult);
	}

}

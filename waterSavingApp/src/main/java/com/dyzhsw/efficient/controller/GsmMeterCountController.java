package com.dyzhsw.efficient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dyzhsw.efficient.service.GsmMeterCountService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月13日
 * @description 用水量统计
 * 
 */

@RestController
@RequestMapping("/getcountwater")
@Api(value = "流量计用水量统计接口")
public class GsmMeterCountController {
	
	@Autowired
	private GsmMeterCountService gsmMetercountService;

	/**
	 * 查找所有设备用水量
	 */
	@ResponseBody
	@RequestMapping(value="/selectcountwater",method=RequestMethod.POST)
	@ApiOperation(value="片区流量计设备用水量统计信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "officeid", value = "设备归属",required = false,paramType = "query" , dataType = "String"),						
						@ApiImplicitParam(name = "equipmentNo", value = "设备编号",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse selectcountwater(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String officeid,String equipmentNo,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String,Object>map = new HashMap<>();
			map.put("officeid", officeid);	
			map.put("equipmentNo", equipmentNo);		
			map.put("start", start);
			map.put("end", end);		
		List<Map<String, Object>> list = gsmMetercountService.selectWatercount(map);		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
	/**
	 * 查找操作日志(分页)
	 */
	@ResponseBody
	@RequestMapping(value="/allcountwater",method=RequestMethod.POST)
	@ApiOperation(value="总水量统计信息")
	public BaseResponse allcountwater( ) {	
		Map<String,Object>map = new HashMap<>();
		List<Map<String, Object>> list = gsmMetercountService.selectWatercount(map);	
		Map<String,String> mapresult = new HashMap<String,String>();
		double total=0.0;		
		for(int i=0;i<list.size();i++){			
			total+=Double.parseDouble(list.get(i).get("userwaternum").toString());			
		}
		mapresult.put("total", total+"");
		return BaseResponseUtil.success(200, "查询成功", mapresult);
	}
	
	/**
	 * 获取所有片区统计用水量
	 */
	@ResponseBody
	@RequestMapping(value="/getWaterareadataApp",method=RequestMethod.POST)
	@ApiOperation(value="获取所有片区统计用水量")
	public BaseResponse getWaterareadataApp() {
		
		List<Map<String, Object>>  list = gsmMetercountService.getWaterareadataApp();		
		
		return BaseResponseUtil.success(200, "查询成功", list);
	}
}

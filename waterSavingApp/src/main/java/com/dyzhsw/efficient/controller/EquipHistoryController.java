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

import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.entity.PressureHistory;
import com.dyzhsw.efficient.service.EquipHistoryDataService;
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
 * @creatDate 2018年12月11日
 * @description 描述
 */

@RestController
@RequestMapping("/equiphistory")
@Api(value = "设备历史数据及最新数据获取")
public class EquipHistoryController {
	
	//压力计历史数据
	@Autowired
	private EquipHistoryDataService equipHistoryDataService;
	
	
	
	/**
	 * 获取压力计历史数据(分页)
	 */
	@ResponseBody
	@RequestMapping(value="/pressureList",method=RequestMethod.POST)
	@ApiOperation(value="获取设备压力计历史数据")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", value = "设备名称",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "equipmentno", value = "设备编号",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse pressureList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String name,String equipmentno,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String, String> map=new HashMap<String, String>();
		map.put("equipname", name);
		map.put("address", equipmentno);
		map.put("end", end);
		map.put("start", start);
		map.put("officeid", "");
		map.put("groupingid", "");
		List<PressureHistory> list = equipHistoryDataService.getPressureList(map);
		PageInfo<PressureHistory> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
	/**
	 * 获取施肥机历史数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/wmmtercalcFlowList",method=RequestMethod.POST)
	@ApiOperation(value="获取施肥机历史数据")
	@ApiImplicitParams({@ApiImplicitParam(name = "equipmentno", value = "设备编号",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse wmmtercalcFlowList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String equipmentno,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("address", equipmentno);
		map.put("end", end);
		map.put("start", start);		
		List<Map<String, Object>> list = equipHistoryDataService.getWmMFList(map);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
	/**
	 * 获取流量计历史数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/waterListApp",method=RequestMethod.POST)
	@ApiOperation(value="获取流量计历史数据")
	@ApiImplicitParams({@ApiImplicitParam(name = "equipmentno", value = "设备编号",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse WaterListApp(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String equipmentno,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("address", equipmentno);
		map.put("end", end);
		map.put("start", start);		
		List<Map<String, Object>> list = equipHistoryDataService.getWaterListApp(map);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
	/**
	 * 获取流量计历史数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getNewdata",method=RequestMethod.POST)
	@ApiOperation(value="获取最新数据")
	@ApiImplicitParams({@ApiImplicitParam(name = "type", value = "设备类型1:水肥机 3:阀控器  4:流量计 5:压力表 ",required = true,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "equipmentno", value = "设备编号",required = true,paramType = "query" , dataType = "String")})
	public BaseResponse getNewdata(String equipmentno,String type) {
		
		Map<String, Object> map=new HashMap<String, Object>();		
		map.put("address", equipmentno);		
		if("1".equals(type)){
			Map<String, Object> mapdata=equipHistoryDataService.getWmMFnewdata(map);
			return BaseResponseUtil.success(200, "水肥机查询成功", mapdata);
		}else if("3".equals(type)){
			Map<String, Object> mapdata=equipHistoryDataService.selectFkqNewApp(map);
			return BaseResponseUtil.success(200, "阀控器查询成功", mapdata);
		}else if("4".equals(type)){
				Map<String, Object> mapdata=equipHistoryDataService.getWaternewdataApp(map);
			return BaseResponseUtil.success(200, "流量计查询成功", mapdata);
		}else if("5".equals(type)){
			Map<String, String> mappress=new HashMap<String, String>();		
			mappress.put("address", equipmentno);	
			PressureHistory pressureHistory=equipHistoryDataService.selectnewdata(mappress);
			return BaseResponseUtil.success(200, "压力表 查询成功", pressureHistory);
		}else{
			return BaseResponseUtil.success(200, "暂无数据", null);
			
		}		
	}
	
	
	/**
	 * 
			 * 
			 * @auth lhl
			 * @description 阀控器
			 * @creatDate 2018年12月19日
			 * @param param1
			 * @param param2
			 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectFkqAllApp",method=RequestMethod.POST)
	@ApiOperation(value="获取设备阀控器历史数据")
	@ApiImplicitParams({@ApiImplicitParam(name = "equipname", value = "设备名称",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "equipmentno", value = "设备编号",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse selectFkqAllApp(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String equipname,String equipmentno,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("equipName", equipname);
		map.put("equipmentNo", equipmentno);
		map.put("endTime", end);
		map.put("startTime", start);
		List<Map<String, Object>>  list = equipHistoryDataService.selectFkqAllApp(map);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
}

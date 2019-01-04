package com.dyzhsw.efficient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.StatisticsService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.ListUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author: yaorui
 * @date: 2018年10月9日 上午10:53:33 
 */

@Controller
@RequestMapping("/statistics")
@Api(value = "管理端统计相关接口")
public class StatisticsController {
	
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private EquipmentInfoService equipmentInfoService;
	

	
	/**
	 * 获取用水量统计
	 */
    @ResponseBody
    @ApiOperation(value="获取用水量统计", notes="获取用水量统计")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "start", value = "开始时间", required = true, dataType = "Date"),
    	@ApiImplicitParam(name = "end", value = "结束时间", required = true, dataType = "Date")
    })
    @RequestMapping(value = "/selectWaterList", method = RequestMethod.POST) 
	public BaseResponse<Map<String,Object>> selectWaterList(String end ,String start) {

        Map<String,Object>map = new HashMap<>();		
		List<Map<String,Object>> lm = new ArrayList<>();
		List<EquipmentInfo> listEqu = equipmentInfoService.getValveList(4);
		if(ListUtils.isNotEmpty(listEqu)) {
			for(int i = 0;i<listEqu.size();i++) {
				Map<String,Object> mapWater= new HashMap<>();
				List<Map<String,Object>> list = statisticsService.selectWaterList(listEqu.get(i).getId(),start,end);	
				if(list.size()>0) {
				mapWater.put("list", list);	
				}
				mapWater.put("id", listEqu.get(i).getId());
				mapWater.put("name", listEqu.get(i).getName());
				lm.add(mapWater);
			}
			 map.put("waterList", lm);
			 List<Map<String,Object>> list1 = statisticsService.selectWaterOrder(start,end);
			 map.put("waterOrder", list1);
		}
		return BaseResponseUtil.success(200, "查询成功", map);
	}
	
	/**
	 * 获取施肥量统计
	 */
	@ResponseBody
    @ApiOperation(value=" 获取施肥量统计", notes=" 获取施肥量统计")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "start", value = "开始时间", required = true, dataType = "Date"),
    	@ApiImplicitParam(name = "end", value = "结束时间", required = true, dataType = "Date")
    })
    @RequestMapping(value = "/selectSfList", method = RequestMethod.POST) 
	public BaseResponse<Map<String,Object>> selectSfList(String end ,String start) {
		 Map<String,Object>map = new HashMap<>();		
			List<Map<String,Object>> lm = new ArrayList<>();
			List<EquipmentInfo> listEqu = equipmentInfoService.getValveList(1);
			if (end!=null) {
				end = end+" "+"23:59:59";
			}
			if(ListUtils.isNotEmpty(listEqu)) {
				for(int i = 0;i<listEqu.size();i++) {
					Map<String,Object> mapWater= new HashMap<>();
					List<Map<String,Object>> list = statisticsService.selectSfList(listEqu.get(i).getId(),start,end);	
					if(list.size()>0) {
					mapWater.put("list", list);	
					}
					mapWater.put("id", listEqu.get(i).getId());
					mapWater.put("name", listEqu.get(i).getName());
					lm.add(mapWater);
				}
				 map.put("sfList", lm);
				 List<Map<String,Object>> list1 = statisticsService.selectSfOrder(start,end);
				 map.put("sfOrder", list1);
			}
			return BaseResponseUtil.success(200, "查询成功", map);
	}
	

	/**
	 * 获取用水量明细
	 */
	@ResponseBody
    @ApiOperation(value=" 获取用水量明细", notes=" 获取用水量明细")
    @ApiImplicitParams({@ApiImplicitParam(name = "address", value = "设备id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "start", value = "开始时间", required = true, dataType = "Date"),
    	@ApiImplicitParam(name = "end", value = "结束时间", required = true, dataType = "Date")
    })
    @RequestMapping(value = "/getWaterList", method = RequestMethod.POST) 
	public BaseResponse<PageInfo<Map<String,Object>>> getWaterList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String address,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		List<Map<String,Object>> list = statisticsService.getWaterList(address,start,end);
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	/**
	 * 获取施肥量明细
	 */
	@ResponseBody
    @ApiOperation(value=" 获取施肥量明细", notes=" 获取施肥量明细")
    @ApiImplicitParams({@ApiImplicitParam(name = "address", value = "设备id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "start", value = "开始时间", required = true, dataType = "Date"),
    	@ApiImplicitParam(name = "end", value = "结束时间", required = true, dataType = "Date")
    })
    @RequestMapping(value = "/getSfList", method = RequestMethod.POST) 
	public BaseResponse<PageInfo<Map<String,Object>>> getSfList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
		@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String address,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		if (end!=null) {
			end = end+" "+"23:59:59";
		}
		List<Map<String,Object>> list = statisticsService.getSfList(address,start,end);
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(list);
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
}

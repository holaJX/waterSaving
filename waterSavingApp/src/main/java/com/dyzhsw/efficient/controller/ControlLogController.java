package com.dyzhsw.efficient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.service.ControlLogService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lhl
 * @date: 2018年12月7日 
 */

@Controller
@RequestMapping("/controlLog")
@Api(value = "设备智能操作日志接口")
public class ControlLogController {
	
	@Autowired
	private ControlLogService controlLogService;

	
	/**
	 * 查找操作日志(分页)
	 */
	@ResponseBody
	@RequestMapping(value="/selectList",method=RequestMethod.POST)
	@ApiOperation(value="获取设备操作日志信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", value = "名称",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "type", value = "类型",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "end", value = "结束时间",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "start", value = "开始时间",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse selectList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String name,String type,String end ,String start) {
		PageHelper.startPage(pageNum,pageSize); 
		Map<String,Object>map = new HashMap<>();
			map.put("name", name);
			map.put("type", type);
			map.put("start", start);
			map.put("end", end);		
		List<Map<String, Object>> list = controlLogService.selectcontrolAll(map);		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	/**
	 * 依据id查找操作日志
	 */
	/*@ResponseBody
	@RequestMapping(value="/selectById",method=RequestMethod.POST)
	@ApiOperation(value="获取操作详细信息", notes="依据id查询详细")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "定时任务id",required = false, dataType = "String")})
	public BaseResponse waterHouseById(String id) {
		ControlLog con = controlLogService.selectById(id);
		return BaseResponseUtil.success(200, "查询成功", con);
	}*/
	
	/**
	 * 增加操作日志
	 */
	/*@ResponseBody
	@RequestMapping("/addInfo")
	public BaseResponse addHouse(ControlLog con) {
		controlLogService.insertInfo(con);
		return BaseResponseUtil.success();
	}*/
	
	/**
	 * 删除
	 */
	/*@ResponseBody
	@RequestMapping(value="/deleteById",method=RequestMethod.POST)
	public BaseResponse deleteById(String id[]) {
		int result = 0;
		result = controlLogService.deleteById(id);
		if(result > 0) {
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "删除失败");
	}*/
	
	
}

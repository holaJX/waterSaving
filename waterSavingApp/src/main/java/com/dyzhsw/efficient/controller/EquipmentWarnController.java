package com.dyzhsw.efficient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentWarn;
import com.dyzhsw.efficient.service.EquipmentWarnService;
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
 * @author lhl 2018年12月10日
 * 类说明
 */

@RestController
@RequestMapping("/equipwarn")
@Api(value = "设备报警日志接口")
public class EquipmentWarnController {

	@Autowired
	private EquipmentWarnService equipmentWarnService;
	
	
	/**
	 * 查询报警日志列表
	 */
	@RequestMapping(value="/selectList",method=RequestMethod.POST)
	@ApiOperation(value="获取报警日志信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "equipname", value = "设备名称", paramType = "query" ,required = false, dataType = "String"),
						@ApiImplicitParam(name = "equipmentNo", value = "设备编号",paramType = "query" ,required = false, dataType = "String"),
						@ApiImplicitParam(name = "warnType", value = "报警类型",paramType = "query" ,required = false, dataType = "String")})
	public BaseResponse selectList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String equipname,String equipmentNo,String warnType) { 
		
		PageHelper.startPage(pageNum,pageSize); 
		
		EquipmentInfo equipmentInfo=new EquipmentInfo();
		equipmentInfo.setName(equipname);
		EquipmentWarn equipmentWarn=new EquipmentWarn();
		equipmentWarn.setEquipmentNo(equipmentNo);
		equipmentWarn.setEquipmentInfo(equipmentInfo);
	
		if(warnType!=null&&warnType!=""){
			equipmentWarn.setWarnType(warnType);
		}
		
		List<EquipmentWarn> list = equipmentWarnService.selectAll(equipmentWarn);
		PageInfo<EquipmentWarn> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
		
	}
}

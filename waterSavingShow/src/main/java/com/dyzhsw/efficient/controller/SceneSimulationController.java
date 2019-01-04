package com.dyzhsw.efficient.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.FkqValveInfoVo;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.FkqValveInfoService;
import com.dyzhsw.efficient.service.SceneSimulationService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
* 万亩猕猴桃园·高效节水 场景模拟
* @author: hw 
* @date: 2018-12-3
 */
@Controller
@RequestMapping("/sceneSimulation")
@Api(value = "高效节水 场景模拟")
public class SceneSimulationController {


	public final static Logger logger = Logger.getLogger(CloudPlatformController.class);

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private SceneSimulationService sceneSimulationService;
	
	@Autowired
	private EquipmentInfoService equipmentInfoService;
	
	@Autowired
	private FkqValveInfoService fkqValveInfoService;
	
	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	* 根据片区id查询片区内的阀控器
	* @param officeId
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/fkqListByOfficeId", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据片区id查询片区内的阀控器", notes="根据片区id查询片区内的阀控器")    
    @ApiImplicitParam(name = "officeId", value = "设备id", required = true, dataType = "String")
	public BaseResponse fkqListByOfficeId(HttpServletRequest request, String officeId) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取过滤器历史数据");
		List<FkqValveInfoVo> list = fkqValveInfoService.selectByOfficeId(officeId);
		return BaseResponseUtil.success(list);
	}
	
	/**
	 * 
	* 根据设备名称模糊搜索
	* @param equipmentId
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/equipmentSearch", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据设备id检查设备是否在线(在线状态0-离线1-在线)", 
    			notes="根据设备id检查设备是否在线(在线状态0-离线1-在线)")    
    @ApiImplicitParam(name = "equipmentId", value = "设备id", required = true, dataType = "String")
	public BaseResponse equipmentSearch(HttpServletRequest request, String equipmentId) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取过滤器历史数据");
		FkqValveInfo info = fkqValveInfoService.selectByEquId(equipmentId);
		return BaseResponseUtil.success(info);
	}
	
	/**
	 * 
	* 根据传入的状态码进行阀控器控制
	* @param equipmentId
	* @param control1Status
	* @param control2Status
	 * @throws IOException 
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/microValveController", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据传入的状态码进行阀控器控制", notes="阀控器控制")    
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "equipmentId", value = "设备id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "control1Status", value = "一路执行开关状态（2为开,1为关）", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "control2Status", value = "二路执行开关状态（2为开,1为关）", required = true, dataType = "String")})
	public BaseResponse microValveController(HttpServletRequest request, String equipmentId, String control1Status, String control2Status) throws IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据传入的状态码进行阀控器控制");
		BaseResponse result = sceneSimulationService.microValveController(equipmentId, control1Status,control2Status);
		return result;
	}
	
	/**
	 * 
	* 根据传入的状态码进行水肥机远程控制
	* @param equipmentId
	* @param switchState
	 * @throws IOException 
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/watermanureController", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据传入的状态码进行水肥机远程控制", notes="水肥机控制")    
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "equipmentId", value = "设备编号", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "switchState", value = "开关状态，0-关1-开", required = true, dataType = "String")})
	public BaseResponse watermanureController(HttpServletRequest request, String equipmentId, String switchState) throws IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据传入的状态码进行水肥机远程控制");
		BaseResponse result = sceneSimulationService.watermanureController(equipmentId, switchState);
		return result;
	}
}

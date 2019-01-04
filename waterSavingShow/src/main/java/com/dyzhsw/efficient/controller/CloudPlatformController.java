package com.dyzhsw.efficient.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.dyzhsw.efficient.service.CloudPlatformService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;

/**
 * 
* 万亩猕猴桃园·高效节水 展示云平台
* @author: hw 
* @date: 2018-11-30
 */
@Controller
@RequestMapping("/cloudPlatform")
@Api(value = "高效节水 展示云平台")
public class CloudPlatformController {

	public final static Logger logger = Logger.getLogger(CloudPlatformController.class);

    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    
	@Autowired
	private CloudPlatformService cloudPlatformService;
	
	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	* 根据选择类型返回对应列表
	* @param equipmentType 1 水肥机，2	过滤器，3	阀门控制器，4	流量计，5	压力计
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/switchedSystem", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取列表", notes="根据选择类型返回对应列表")    
    @ApiImplicitParam(name = "equipmentType", 
    	value = "设备类型1 水肥机，2	过滤器，3	阀门控制器，4	流量计，5	压力计", required = true, dataType = "Integer")
	public BaseResponse switchedSystem(HttpServletRequest request, Integer equipmentType) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----切换系统");
		Map<String, Object> res = cloudPlatformService.switchedSystem(equipmentType);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据id获取阀门控制器信息
	* @param id
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/fkqValveInfo", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取阀控器详情", notes="根据id获取阀门控制器信息")    
    @ApiImplicitParam(name = "id", value = "阀控器id", required = true, dataType = "String")
	public BaseResponse fkqValveInfo(HttpServletRequest request, String id) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取阀门控制器信息");
		Map<String, Object> res = cloudPlatformService.fkqValveInfo(id);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据id获取水肥机信息
	* @param id
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/fertilizerMachineInfo", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取水肥机详情", notes="根据id获取水肥机信息")    
    @ApiImplicitParam(name = "id", value = "水肥机id", required = true, dataType = "String")
	public BaseResponse fertilizerMachineInfo(HttpServletRequest request, String id) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取水肥机信息");
		Map<String, Object> res = cloudPlatformService.fertilizerMachineInfo(id);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据id获取过滤器信息
	* @param id
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/flowmeterInfo", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取过滤器详情", notes="根据id获取过滤器信息")    
    @ApiImplicitParam(name = "id", value = "过滤器id", required = true, dataType = "String")
	public BaseResponse flowmeterInfo(HttpServletRequest request, String id) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取过滤器信息");
		Map<String, Object> res = cloudPlatformService.flowmeterInfo(id);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据id获取过滤器信息
	* @param id
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/flowmeterHistory", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取过滤器历史数据", notes="根据过滤器id获取过滤器历史数据")    
    @ApiImplicitParam(name = "id", value = "过滤器id", required = true, dataType = "String")
	public BaseResponse flowmeterHistory(HttpServletRequest request, String id) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取过滤器历史数据");
		Map<String, Object> res = cloudPlatformService.flowmeterHistory(id);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据设备名称模糊搜索
	* @param id
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/equipmentSearch", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据设备名称模糊搜索", notes="根据设备名进行模糊搜索")    
    @ApiImplicitParam(name = "equipmentName", value = "设备名称", required = true, dataType = "String")
	public BaseResponse equipmentSearch(HttpServletRequest request, String equipmentName) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据id获取过滤器历史数据");
		Map<String, Object> res = cloudPlatformService.equipmentSearch(equipmentName);
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* 根据传入的状态码进行阀控器控制
	* @param id
	 * @throws IOException 
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/microValveController", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="根据传入的状态码进行阀控器控制", notes="阀控器控制")    
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "equipmentId", value = "设备编号", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "control1Status", value = "一路执行开关状态（2为开,1为关）", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "control2Status", value = "二路执行开关状态（2为开,1为关）", required = true, dataType = "String")})
	public BaseResponse microValveController(HttpServletRequest request, String equipmentId, String control1Status, String control2Status) throws IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----根据传入的状态码进行阀控器控制");
		BaseResponse result = cloudPlatformService.microValveController(equipmentId, control1Status,control2Status);
		return result;
	}
	
	/**
	 * 
	* 根据传入的状态码进行水肥机远程控制
	* @param id
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
		BaseResponse result = cloudPlatformService.watermanureController(equipmentId, switchState);
		return result;
	}
}

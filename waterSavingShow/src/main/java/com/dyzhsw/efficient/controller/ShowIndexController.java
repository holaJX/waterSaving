package com.dyzhsw.efficient.controller;

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
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.service.ShowIndexService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
* 万亩猕猴桃园·高效节水 展示首页
* @author: hw 
* @date: 2018-11-28
 */
@Controller
@RequestMapping("/showIndex")
@Api(value = "高效节水展示首页")
public class ShowIndexController {

	public final static Logger logger = Logger.getLogger(ShowIndexController.class);

    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    
	@Autowired
	private ShowIndexService showIndexService;
	
	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ResponseBody
	@RequestMapping(value="/pianquList", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取系统片区list", notes="获取系统片区list")
	public BaseResponse pianquList(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----获取系统片区list");
		List<SysOffice> list = showIndexService.pianquList();
		return BaseResponseUtil.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/shoubuList", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="获取首部list", notes="获取首部list")
	public BaseResponse shoubuList(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----获取首部list");
		List<SysOffice> list = showIndexService.shoubuList();
		return BaseResponseUtil.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/tabOne", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="系统运行情况", notes="获取四个首部系统的瞬时流量")
	public BaseResponse tabOne(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----系统运行情况-首部系统瞬时流量");
		Map<String, Object> res = showIndexService.tabOne();
		return BaseResponseUtil.success(res);
	}
	
	@ResponseBody
	@RequestMapping(value="/tabTwo", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="用水量及状态监控", notes="上月用水量、总用水量、总节水量（总用水量*30%）及状态监控")
	public BaseResponse tabTwo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----上月用水量、总用水量、总节水量（总用水量*30%）及状态监控");
		Map<String, Object> res = showIndexService.tabTwo();
		return BaseResponseUtil.success(res);
	}
	
	/**
	 * 
	* @TODO:	获取用水量统计
	* @param dataType 0:天 1:月 2:年
	* @return（展示方法参数和返回值）
	 */
	@ResponseBody
	@RequestMapping(value="/tabThree", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="用水量统计", notes="用水量统计")
    @ApiImplicitParam(name = "dataType", value = "dataType 0:天 1:月 2:年", required = true, dataType = "Integer")
	public BaseResponse tabThree(HttpServletRequest request, Integer dataType) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----用水量统计");
		if(null == dataType) {
			dataType =0;
		}
		Map<String, Object> res = showIndexService.tabThree(dataType);
		return BaseResponseUtil.success(res);
	}
	
	@ResponseBody
	@RequestMapping(value="/tabFour", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="罐区总览-场景模拟", notes="暂无")
	public BaseResponse tabFour() {
		logger.info(time.format(new Date()) + "----罐区总览");
		Map<String, Object> res = showIndexService.tabFour();
		return BaseResponseUtil.success(res);
	}
	
	@ResponseBody
	@RequestMapping(value="/tabFive", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="过滤器监控", notes="过滤器对应的流量计记录的数据")    
    @ApiImplicitParam(name = "equipmentNo", value = "设备编号", required = false, dataType = "String")
	public BaseResponse tabFive(HttpServletRequest request, String equipmentNo) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----过滤器监控");
		Map<String, Object> res = showIndexService.tabFive(equipmentNo);
		return BaseResponseUtil.success(res);
	}
	
	@ResponseBody
	@RequestMapping(value="/tabSix", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value="施肥机监控", notes="水肥机记录的数据")    
    @ApiImplicitParam(name = "terminaId", value = "设备编号", required = false, dataType = "String")
	public BaseResponse tabSix(HttpServletRequest request, String terminaId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
		logger.info(time.format(new Date()) + "----施肥机监控");
		Map<String, Object> res = showIndexService.tabSix(terminaId);
		return BaseResponseUtil.success(res);
	}
	
}

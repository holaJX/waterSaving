package com.dyzhsw.efficient.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.service.ControlLogService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;

/**
 * @author: yaorui
 * @date: 2018年10月9日 上午10:53:33 
 */

@Controller
@RequestMapping("/controlLog")
@Api(value = "操作日志接口")
public class ControlLogController {
	
	@Autowired
	private ControlLogService controlLogService;
	
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

	
	/**
	 * 查找操作日志(分页)
	 */
	@ResponseBody
	@RequestMapping(value="/selectList",method=RequestMethod.POST)
	public BaseResponse<PageInfo<ControlLog>> selectList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String name,String type,String end ,String start,HttpServletRequest request) {
		PageHelper.startPage(pageNum,pageSize);
		 String authHeader = request.getHeader("Authorization");
	        if (authHeader == null) {
	            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
	     }
	
	            String token = authHeader.substring(11);
	            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);	           
	            String officeId = String.valueOf(map.get("officeId"));

		if (end!=null) {
			end = end+" "+"23:59:59";
		}
		List<ControlLog> list = controlLogService.selectAll(name,type,start,end,officeId);
	
		PageInfo<ControlLog> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	/**
	 * 依据id查找操作日志
	 */
	@ResponseBody
	@RequestMapping(value="/selectById",method=RequestMethod.POST)
	public BaseResponse<ControlLog> waterHouseById(int id) {
		ControlLog con = controlLogService.selectById(id);
		return BaseResponseUtil.success(200, "查询成功", con);
	}
	
	/**
	 * 增加操作日志
	 */
	@ResponseBody
	@RequestMapping("/addInfo")
	public BaseResponse addHouse(ControlLog con) {
		controlLogService.insertInfo(con);
		return BaseResponseUtil.success();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value="/deleteById",method=RequestMethod.POST)
	public BaseResponse deleteById(String id[]) {
		int result = 0;
		result = controlLogService.deleteById(id);
		if(result > 0) {
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "删除失败");
	}
	
	
}

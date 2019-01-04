package com.dyzhsw.efficient.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.config.enums.ResultEnum;
import com.dyzhsw.efficient.entity.EquipmentTask;
import com.dyzhsw.efficient.entity.TaskEquList;
import com.dyzhsw.efficient.service.EquipmentInfoService;
import com.dyzhsw.efficient.service.EquipmentTaskService;
import com.dyzhsw.efficient.service.SysDictService;
import com.dyzhsw.efficient.service.SysOfficeService;
import com.dyzhsw.efficient.service.TaskEquListService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.BaseResponseUtil;
import com.dyzhsw.efficient.utils.IDUtils;
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
@RequestMapping("/task")
@Api(value = "定时任务相关接口")
public class EquipmentTaskController {
	
	@Autowired
	private EquipmentTaskService taskService;
	
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    
    @Autowired
	private SysDictService sysDictService;
    
    @Autowired
 	private SysOfficeService sysOfficeService;
    
    @Autowired
	private TaskEquListService taskEquListService;
    
    @Autowired
	private EquipmentInfoService equipmentInfoService;
   

	
	/**
	 * 查找定时任务(分页)
	 */
	@ResponseBody
    @ApiOperation(value=" 查找定时任务(分页)", notes="查找定时任务(分页)")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "name", value = "任务名称", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "officeId", value = "片区id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "type", value = "任务类型", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/selectList", method = RequestMethod.POST) 
	public BaseResponse<PageInfo<Map<String,Object>>> valveInfoList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String name,String officeId,String type ,HttpServletRequest request) {
		PageHelper.startPage(pageNum,pageSize); 
		 String authHeader = request.getHeader("Authorization");
	        if (authHeader == null) {
	            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
	     }
	        if(officeId==null) {
	            String token = authHeader.substring(11);
	            Map<Object,Object> map = redisTemplate.opsForHash().entries(token);	           
	            officeId = String.valueOf(map.get("officeId"));
	        }
		List<Map<String,Object>> list = taskService.selectAll(name,officeId,type);
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	/**
	 * 依据id定时任务
	 */
	@ResponseBody
	@RequestMapping(value="/selectById",method=RequestMethod.POST)
	public BaseResponse<EquipmentTask> waterHouseById(String id) {
		Map<String,Object> task = taskService.selectById(id);
		return BaseResponseUtil.success(200, "查询成功", task);
	}
	
	/**
	 * 增加定时任务
	 */
	@ResponseBody
    @ApiOperation(value="增加定时任务", notes="增加定时任务")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "task", value = "定时任务实体类", required = true, dataType = "实体类"),
    	@ApiImplicitParam(name = "equId", value = "设备id字符串", required = true, dataType = "String")
    })
    @ApiImplicitParam(name = "task", value = "定时任务实体类", required = true, dataType = "实体类")  
    @RequestMapping(value = "/addInfo", method = RequestMethod.POST)   
	public BaseResponse addInfo(EquipmentTask task,String equId, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
        }
        String token = authHeader.substring(11);
        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
       
        if(task.getType().equals("2")) {
        	List<Map<String,String>> equMap = taskService.selectByEquId(equId);
        	if(equMap.size()>0) {
        		for(int n = 0;n<equMap.size();n++) {
        			boolean has = false;
			        String[] strOne= task.getReturnTime().split(",");
			        String[] strTwo= equMap.get(n).get("returnTime").split(",");
			        for (int i = 0; i < strOne.length; i++) {
			            if (ArrayUtils.contains(strTwo, strOne[i])) {
			                has = true;
			                break;
			             }
			         }
			        if(has==true) {
        		        Date  openTime = this.change(equMap.get(n).get("openhour"), equMap.get(n).get("openmin"));
        				Calendar cal = Calendar.getInstance();  
        				cal.setTime(openTime);  
        		        cal.add(Calendar.MINUTE, Integer.valueOf(equMap.get(n).get("fertilizingTime")));
        		        Date closeTime = cal.getTime(); 
        		        Date  newTime = this.change(task.getOpenhour(), task.getOpenmin());
        				if(newTime.after(openTime)&&newTime.before(closeTime)) {
        					return BaseResponseUtil.error(201, "该设备在相同时间段已有操作");
        				}
        			}
        		}
        	}
        }
        String currUserId = String.valueOf(map.get("id"));
		task.setId(IDUtils.createUUID());
		task.setCreateBy(currUserId);
		task.setCreateTime(new Date());
		taskService.insertInfo(task);
		 //更新设备列表
		
		String s[]=equId.split(","); 
		 for (int i = 0; i < s.length; i++) { 
			 TaskEquList te = new TaskEquList();
				te.setTaskId(task.getId());
				te.setId(IDUtils.createUUID()); 
				te.setCreateTime(new Date());
				te.setEquId(s[i]);
				taskEquListService.addTaskEqu(te);
		 }		 
		return BaseResponseUtil.success();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
    @ApiOperation(value="删除定时任务", notes="删除定时任务")
    @ApiImplicitParam(name = "id", value = "定时任务id", required = true, dataType = "id")  
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST) 
	public BaseResponse deleteById(String[]id, HttpServletRequest request) {
		 String authHeader = request.getHeader("Authorization");
	        if (authHeader == null) {
	            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
	     }
		int result = 0;
		result = taskService.deleteById(id);
		taskEquListService.deleteByTaskId(id);
		if(result > 0) {
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "删除失败");
	}
	
	/**
	 * 修改定时任务
	 */
	@ResponseBody
    @ApiOperation(value="修改定时任务", notes="定时任务类型获取")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "task", value = "定时任务实体类", required = true, dataType = "实体类"),
    	@ApiImplicitParam(name = "equId", value = "设备id字符串", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST) 
	public BaseResponse updateWater(EquipmentTask task,String equId, HttpServletRequest request) {
		 String authHeader = request.getHeader("Authorization");
	        if (authHeader == null) {
	            return BaseResponseUtil.error(ResultEnum.LOGIN_TOKEN_ERROR.getStateCode(), ResultEnum.LOGIN_TOKEN_ERROR.getMessage());
	    }
	        String token = authHeader.substring(11);
	        Map<Object,Object> map = redisTemplate.opsForHash().entries(token);
	       
	        if(task.getType().equals("2")) {
	        	List<Map<String,String>> equMap = taskService.selectByEquId(equId);
	        	if(equMap.size()>1) {
	        		for(int n = 0;n<equMap.size();n++) {
	        			if(!equMap.get(n).get("id").equals(task.getId())) {
	        			boolean has = false;
	        			        String[] strOne= task.getReturnTime().split(",");
	        			        String[] strTwo= equMap.get(n).get("returnTime").split(",");
	        			        for (int i = 0; i < strOne.length; i++) {
	        			            if (ArrayUtils.contains(strTwo, strOne[i])) {
	        			                has = true;
	        			                break;
	        			             }
	        			         }
	        			if(has==true) {
	        		        Date  openTime = this.change(equMap.get(n).get("openhour"), equMap.get(n).get("openmin"));
	        				Calendar cal = Calendar.getInstance();  
	        				cal.setTime(openTime);  
	        		        cal.add(Calendar.MINUTE, Integer.valueOf(equMap.get(n).get("fertilizingTime")));
	        		        Date closeTime = cal.getTime(); 
	        		        Date  newTime = this.change(task.getOpenhour(), task.getOpenmin());
	        				if(newTime.after(openTime)&&newTime.before(closeTime)) {
	        					return BaseResponseUtil.error(201, "该设备在相同时间段已有操作");
	        				}
	        			}
	        		  }
	        		}
	        	}
	        }
	    String currUserId = String.valueOf(map.get("id"));
	    task.setCreateBy(currUserId);
		int result = 0;
		task.setUpdateTime(new Date());
		result = taskService.updateById(task);
		if(result > 0) {
			if(equId!=null) {
			taskEquListService.deleteByTaskIdone(task.getId());
			String s[]=equId.split(","); 
			 for (int i = 0; i < s.length; i++) { 
				 TaskEquList te = new TaskEquList();
					te.setTaskId(task.getId());
					te.setId(IDUtils.createUUID()); 
					te.setCreateTime(new Date());
					te.setEquId(s[i]);
					taskEquListService.addTaskEqu(te);
			 }		 
			}
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "修改失败");
	}
	
	/**
	 * 定时任务类型获取
	 * @param valveInfo
	 * @return
	 */
	@ResponseBody
    @ApiOperation(value="定时任务类型获取", notes="定时任务类型获取")
    @RequestMapping(value = "/getTask", method = RequestMethod.GET)   
	public BaseResponse<Map<String,String>> taskType() {
		Map<String,String> map = new HashMap<String,String>();
		String type = "task_type";
		map = sysDictService.getMap(type);
		return BaseResponseUtil.success(200, "查询成功", map);
	}
	
	/**
	 * 定时任务获取设备列表//只有阀控器
	 * @param 
	 * @return
	 */
	@ResponseBody
    @ApiOperation(value="定时任务获取设备列表", notes="定时任务获取设备列表")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "taskId", value = "定时任务id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "officeId", value = "片区id", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "type", value = "设备类型 施肥机1，灌溉4", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getEqu", method = RequestMethod.POST)   
	public BaseResponse<Map<String,String>> getEqu(String taskId,String officeId,int type) {
        Map<String,Object>map = new HashMap<>();     
        if(taskId !=null) {
		List<Map<String,Object>> listr =  taskEquListService.selectByTaskId(taskId);
            if(listr.size()>0) {
            	map.put("right", listr);
            }else {
            	map.put("right", null);
            }
		      List<Map<String,Object>> listl = equipmentInfoService.getEquListByOfficeId(officeId,type);
		      map.put("left", listl);		    
		 		
        }else {
         List<Map<String,Object>> listl = equipmentInfoService.getEquListByOfficeId(officeId,type);
         map.put("left", listl);
         map.put("right", null);
        }
		return BaseResponseUtil.success(200, "查询成功", map);
	}
	
	/**
	 * 定时任务获取设备列表//只有施肥机
	 * @param 
	 * @return
	 */
	@ResponseBody
    @ApiOperation(value="定时任务获取设备列表", notes="定时任务获取设备列表")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "officeId", value = "片区id", required = true, dataType = "String")    
    })
    @RequestMapping(value = "/getWmEqu", method = RequestMethod.POST)   
	public BaseResponse  getWmEqu(String officeId) {
		List<Map<String,Object>> listr =  equipmentInfoService.getWmByOfficeId(officeId);   
		return BaseResponseUtil.success(200, "查询成功", listr);
	}


	
	private Date change( String openhour, String openmin) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null; 
		try {
		   date = format.parse(openhour+":"+openmin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return date;
	}
}

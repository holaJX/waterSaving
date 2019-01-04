package com.dyzhsw.efficient.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dyzhsw.efficient.crontab.Task;
import com.dyzhsw.efficient.entity.EquipmentTask;
import com.dyzhsw.efficient.entity.TaskEquList;
import com.dyzhsw.efficient.service.EquipmentTaskService;
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
 * @author: lhl
 * @date: 2018年12月6日 上午14:50
 */

@Controller
@RequestMapping("/task")
@Api(value = "定时任务app接口")
public class EquipmentTaskController {
	
	@Autowired
	private EquipmentTaskService taskService;
	@Autowired
	private TaskEquListService taskEquListService;
	
	
	Task tasknew = new Task();
	
	/**
	 * 查找定时任务(分页)
	 */
	@ResponseBody
	@RequestMapping(value="/selectList",method=RequestMethod.POST)
	@ApiOperation(value="获取定时任务列表信息", notes="根据输入信息获取信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "name", value = "定时任务名称",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "officeId", value = "片区id",required = false,paramType = "query" , dataType = "String"),
						@ApiImplicitParam(name = "type", value = "任务类型",required = false,paramType = "query" , dataType = "String")})
	public BaseResponse valveInfoList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,String name,String officeId,String type) {
		PageHelper.startPage(pageNum,pageSize); 
		List<Map<String, Object>> list = taskService.selectAll(name,officeId,type);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list); 
		return BaseResponseUtil.success(200, "查询成功", pageInfo);
	}
	
	
	
	/**
	 * 依据id查询定时任务详细
	 */
	@ResponseBody
	@RequestMapping(value="/selectById",method=RequestMethod.POST)
	@ApiOperation(value="获取定时任务详细信息", notes="依据id查询定时任务详细")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "定时任务id",required = true,paramType = "query" ,dataType = "String")})
	public BaseResponse waterHouseById(String id) {
		
		EquipmentTask task = taskService.selectByIdApp(id);
		
		List<TaskEquList> taskqulist=taskEquListService.selectAllApp(id);
		task.setTaskequlist(taskqulist);
		return BaseResponseUtil.success(200, "查询成功", task);
	}
	
	/**
	 * 增加定时任务
	 * @throws SchedulerException 
	 */
	@ResponseBody
	@RequestMapping(value="/addInfo",method=RequestMethod.POST)
	@ApiOperation(value="创建定时任务信息")
	public BaseResponse addHouse(@RequestBody EquipmentTask task) throws SchedulerException {
		task.setId(IDUtils.createUUID());
		task.setCreateTime(new Date());
		task.setUpdateTime(new Date());
		taskService.insertInfo(task);	
		if(task.getTaskequlist()!=null){
			for(int i=0;i<task.getTaskequlist().size();i++){
				TaskEquList taskEquList=task.getTaskequlist().get(i);
					taskEquList.setId(IDUtils.createUUID());
					taskEquList.setTaskId(task.getId());
					taskEquList.setCreateTime(new Date());
					taskEquListService.addTaskEqu(taskEquList);
				}
		}
		
		//创建定时任务 并且启动定时任务		
		/*if(task.getStatus()==0){
			
			BaseResponse openbr=tasknew.delete(task.getId()+"_open");
			BaseResponse closebr=tasknew.delete(task.getId()+"_close");
			String opencron="0 "+task.getOpenmin()+" "+task.getOpenhour()+" ? * "+task.getReturnTime();
			String closecron="0 "+task.getClosemin()+" "+task.getClosehour()+" ? * "+task.getReturnTime();
			BaseResponse openbradd=tasknew.add(opencron, task.getId()+"_open", "测试1", "1");
			BaseResponse closebradd=tasknew.add(closecron, task.getId()+"_close","测试2",  "2");
			
			System.out.println(openbr.getMessage()+"===插入=="+closebr.getMessage()+"==="+openbradd.getMessage()+"===="+closebradd.getMessage());
		}
		*/
		return BaseResponseUtil.success();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value="/deleteById",method=RequestMethod.POST)
	@ApiOperation(value="删除定时任务信息", notes="依据id删除定时任务")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "定时任务id",required = false, dataType = "String",paramType = "query")})
	public BaseResponse deleteById(String id) {
		int result = 0;
		/*EquipmentTask task=new EquipmentTask();
		task.setId(id);*/
		String[] ids={};
		if(id.indexOf(",")!=-1){
			ids=id.split(",");
		}else{
			
			ids=new String[]{id};
		}
		
			result = taskService.deleteById(ids);
		if(result > 0) {
			taskEquListService.deleteByTaskId(ids);
		/*	try {
							
				BaseResponse openbr=tasknew.delete(id+"_open");
				BaseResponse closebr=tasknew.delete(id+"_close");
				System.out.println(openbr.getMessage()+"===删除=="+closebr.getMessage());
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println("接口删除定时任务出错了");
				}*/
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "删除失败");
	}
	
	/**
	 * 修改定时任务
	 */
	@ResponseBody
	@RequestMapping(value="/updateInfo",method=RequestMethod.POST)
	@ApiOperation(value="修改定时任务信息")
	public BaseResponse updateWater(@RequestBody EquipmentTask task) {
		int result = 0;
		task.setUpdateTime(new Date());
		result = taskService.updateById(task);
		if(result > 0) {
			taskEquListService.deleteByTaskIdone(task.getId());
			if(task.getTaskequlist()!=null){
				for(int i=0;i<task.getTaskequlist().size();i++){
					TaskEquList taskEquList=task.getTaskequlist().get(i);
						taskEquList.setId(IDUtils.createUUID());
						taskEquList.setTaskId(task.getId());
						taskEquList.setCreateTime(new Date());
						taskEquListService.addTaskEqu(taskEquList);
					}
			}
			/*if(task.getStatus()==0){
				try {
							
					BaseResponse openbr=tasknew.delete(task.getId()+"_open");
					BaseResponse closebr=tasknew.delete(task.getId()+"_close");
					String opencron="0 "+task.getOpenmin()+" "+task.getOpenhour()+" ? * "+task.getReturnTime();
					String closecron="0 "+task.getClosemin()+" "+task.getClosehour()+" ? * "+task.getReturnTime();
					BaseResponse openbradd=tasknew.add(opencron, task.getId()+"_open", "修改测试1", "1");
					BaseResponse closebradd=tasknew.add(closecron, task.getId()+"_close","修改测试2",  "2");
					System.out.println(openbr.getMessage()+"==修改==="+closebr.getMessage()+"==="+openbradd.getMessage()+"===="+closebradd.getMessage());
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					System.out.println("接口修改定时任务出错了");
				}
				
			}
			*/
			
			return BaseResponseUtil.success();
		}
		return BaseResponseUtil.error(500, "修改失败");
	}
	
}

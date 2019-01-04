package com.dyzhsw.efficient.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyzhsw.efficient.BaseJunit;
import com.dyzhsw.efficient.entity.EquipmentTask;
import com.dyzhsw.efficient.entity.TaskEquList;
import com.dyzhsw.efficient.service.EquipmentTaskService;
import com.dyzhsw.efficient.service.TaskEquListService;
import com.dyzhsw.efficient.utils.IDUtils;

public class EquipmentTaskControllerTest  extends BaseJunit {

	@Autowired
	private EquipmentTaskService taskService;
	@Autowired
	private TaskEquListService taskEquListService;
	
	@Test
	public void testValveInfoList() {
		String name="";
		String officeId="";
		String type="";
		List<Map<String, Object>> list = taskService.selectAll(name,officeId,type);
		System.out.println(list.size());
	}

	@Test
	public void testWaterHouseById() {

		String id="bd74d8a9e13d46a9a0060632a57c0883";
		EquipmentTask task = taskService.selectByIdApp(id);
		
		List<TaskEquList> taskqulist=taskEquListService.selectAllApp(id);
		task.setTaskequlist(taskqulist);
		
		System.out.println(taskqulist.size()+"==="+task.getId());
	}

	
	@Test
	public void testAddHouse() {
		
		EquipmentTask task=new EquipmentTask();
		task.setId(IDUtils.createUUID());
		task.setCreateTime(new Date());
		taskService.insertInfo(task);	
		if(task.getTaskequlist()!=null){
			for(int i=0;i<task.getTaskequlist().size();i++){
				TaskEquList taskEquList=task.getTaskequlist().get(i);
					taskEquList.setTaskId(task.getId());
					taskEquList.setCreateTime(new Date());
					taskEquListService.addTaskEqu(taskEquList);
				}
		}
	}

	
	@Test
	public void testDeleteById() {
		int result = 0;
		/*EquipmentTask task=new EquipmentTask();
		task.setId(id);*/
		String id="bd74d8a9e13d46a9a0060632a57c0883";
		String[] ids={};
		if(id.indexOf(",")!=-1){
			ids=id.split(",");
		}else{
			
			ids=new String[]{id};
		}
			result = taskService.deleteById(ids);
		if(result > 0) {
			taskEquListService.deleteByTaskId(ids);
			}
		
		
	}
	

	@Test
	public void testUpdateWater() {
		
		EquipmentTask task=new EquipmentTask();
		int result = 0;
		result = taskService.updateById(task);
		if(result > 0) {
			taskEquListService.deleteByTaskIdone(task.getId());
			if(task.getTaskequlist()!=null){
				for(int i=0;i<task.getTaskequlist().size();i++){
					TaskEquList taskEquList=task.getTaskequlist().get(i);
						taskEquList.setTaskId(task.getId());
						taskEquList.setCreateTime(new Date());
						taskEquListService.addTaskEqu(taskEquList);
					}
			}
		}
			
	}

}

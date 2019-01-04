package com.dyzhsw.efficient.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.TaskEquListDao;
import com.dyzhsw.efficient.entity.TaskEquList;
import com.dyzhsw.efficient.service.TaskEquListService;


/**
 * @author: yaorui 
 * @date: 2018年12月12日 上午10:34:12 
 */

@Service
public class TaskEquListServiceImpl implements TaskEquListService {

	@Autowired
	TaskEquListDao taskEquListDao;

	
	@Override
	public List<Map<String,Object>> selectByTaskId(String id) {
		return taskEquListDao.selectByTaskId(id);
	}

	@Override
	public void addTaskEqu(TaskEquList task) {
		int i = taskEquListDao.addTaskEqu(task);	
	}
	@Override
	public void deleteByTaskId(String[]id) {
		taskEquListDao.deleteBytaskId(id);
	}
	
	@Override
	public void deleteByTaskIdone(String id) {
		taskEquListDao.deleteByTaskIdone(id);
	}

	@Override
	public List<TaskEquList> selectAllApp(String taskId) {
		// TODO Auto-generated method stub
		return taskEquListDao.selectAllApp(taskId);
	}
}

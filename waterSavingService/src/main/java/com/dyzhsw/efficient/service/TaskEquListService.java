package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.TaskEquList;

/**
 * 操作日志相关service
 * create by yr
 */
public interface TaskEquListService {

	List<Map<String,Object>> selectByTaskId(String id);

	void addTaskEqu(TaskEquList te);

	void deleteByTaskId(String[] id);

	void deleteByTaskIdone(String id);

	List<TaskEquList> selectAllApp(String taskId);
}                               
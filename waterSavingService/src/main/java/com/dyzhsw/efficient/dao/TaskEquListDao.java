package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.TaskEquList;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface TaskEquListDao {

	//根据taskid查找
    List<Map<String,Object>> selectByTaskId(String taskId);

	int addTaskEqu(TaskEquList task);

	void deleteBytaskId(String[] id);

	void deleteByTaskIdone(String id);

	List<TaskEquList> selectAllApp(String taskId);

	void deletedByEquId(String equId);
}

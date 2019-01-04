package com.dyzhsw.efficient.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dyzhsw.efficient.entity.WmTerminalinfo;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface WmTerminalinfoDao {
	//查找全部
    List<WmTerminalinfo> selectAll(WmTerminalinfo wm);
	//根据id查找
    /**
     * huangwei
     * modify huangwei
    * @param id
    * @return（展示方法参数和返回值）
     */
    WmTerminalinfo selectById(@Param("equipmentId")String id);
	//增加
	int insertInfo(WmTerminalinfo wm);
	//删除
	int deleteById(int id);
	//修改
	int updateById(WmTerminalinfo wm);

	WmTerminalinfo selectByEquNo(String equNo);
	
}

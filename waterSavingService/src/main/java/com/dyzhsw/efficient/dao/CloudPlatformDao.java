package com.dyzhsw.efficient.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.LineChartVo;

public interface CloudPlatformDao {

	List<EquipmentInfo> switchedSystem(Integer equipmentType);

	List<LineChartVo> queryFilterValue(@Param("equipmentId")String equipmentId);

	String queryInstantaneousflowCopyTimeById(@Param("equipmentId")String equipmentId);

	List<LineChartVo> queryFilterHistory(@Param("equipmentId")String equipmentId);

	String queryCumulativeflow(@Param("terminalId")String terminalId);

	String queryWmState(@Param("terminalId")String terminalId);
}

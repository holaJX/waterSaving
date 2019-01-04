package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.FlowmeterHomeVo;
import com.dyzhsw.efficient.entity.LineChartVo;
import com.dyzhsw.efficient.entity.MetercalcFlowVo;
import com.dyzhsw.efficient.entity.SysOffice;
import com.dyzhsw.efficient.entity.WaterConsumption;


/**
 * 
* 展示首页
* @author: hw 
* @date: 2018-12-4
 */
public interface ShowIndexDao {

	//获取首部系统流量计的设备信息
	List<EquipmentInfo> queryMeterEquipmentNo();

	//获取首部系统流量计的用水量
	Map<String, Object> queryValueByNo(@Param("equipmentNo")String equipmentNo);

	//获取用水量相关
	Map<String, Object> queryWaterCost();

	//获取状态监控
	Map<String, Object> querySolenoidValve();

	//获取片区信息
	List<SysOffice> queryPianQu();

	//获取指定园区id下的所有水表信息
	List<String> queryEquipmentList(@Param("officeId")String officeId);
	
	//按天统计 用水量
	WaterConsumption waterConsumptionByDay(@Param("equipmentNo")String equipmentNo);

	//按月统计 用水量
	WaterConsumption waterConsumptionByMonth(@Param("equipmentNo")String equipmentNo);

	//按年统计 用水量
	WaterConsumption waterConsumptionByYear(@Param("equipmentNo")String equipmentNo);

	//过滤器对应的流量计设备id列表
	List<FlowmeterHomeVo> queryFilter();

	//根据流量计编号获取累计流量
	List<LineChartVo> queryFilterValue(@Param("equipmentId")String equipmentId);
	
	//根据流量计编号获取最新瞬时流量
	String queryInstantaneousflow(@Param("equipmentId")String equipmentId);

	//根据流量计编号获取最新最大瞬时流量
	String queryInstantaneousflowMax(@Param("equipmentId")String equipmentId);

	//根据ids获取数据采集时间
	String queryInstantaneousflowCopyTimeByIds(@Param("shoubuIds")List<String> shoubuIds);

	//根据id获取数据采集时间
	String queryInstantaneousflowCopyTimeById(@Param("equipmentId")String equipmentId);

	//施肥机设备id列表
	List<MetercalcFlowVo> queryFertilizer();

	//施肥机根据编号获取累计流量记录
	List<LineChartVo> queryFertilizerValue(@Param("metercalcFlowId")String metercalcFlowId);

	//施肥机根据编号获取最新瞬时流量 
	String queryWmInstantaneousflow(@Param("metercalcFlowId")String metercalcFlowId);

	//施肥机根据编号获取最新出口压力
	String queryAmount(@Param("metercalcFlowId")String metercalcFlowId);

	//施肥机根据编号获取最新最大出口压力
	String queryAmountMax(@Param("metercalcFlowId")String metercalcFlowId);

	//施肥机根据编号获取最新记录时间
	String queryCopyTimeById(@Param("metercalcFlowId")String metercalcFlowId);

	//equipmentType设备类型1 水肥机 2 过滤器 3 阀控器 4 流量计
	List<EquipmentInfo> getEquipmentListByTypeOfficeId(@Param("equipmentType")Integer equipmentType,@Param("officeId")String officeId);
}

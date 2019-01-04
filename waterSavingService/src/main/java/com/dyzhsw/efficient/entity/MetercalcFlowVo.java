package com.dyzhsw.efficient.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 水肥机首页显示VO类
 */
@ApiModel(value = "水肥机首页显示VO类")
public class MetercalcFlowVo {

    @ApiModelProperty(value = "水肥机id")
	private String metercalcFlowId;

    @ApiModelProperty(value = "水肥机设备编号")
	private String metercalcFlowNo;

    @ApiModelProperty(value = "水肥机名称")
	private String metercalcFlowName;

    @ApiModelProperty(value = "水肥机运行状态")
	private String state;

    @ApiModelProperty(value = "水肥机的累计流量")
	private List<LineChartVo> cumulativeflow ;

    @ApiModelProperty(value = "水肥机的累计流量增加")
	private String cumulativeflowIncrementSpeed;

    @ApiModelProperty(value = "水肥机的出口压力")
	private String amount; 

    @ApiModelProperty(value = "水肥机的最大出口压力")
	private String amountMax; 

    @ApiModelProperty(value = "抄录时间")
	private String copyTime;

	public String getMetercalcFlowId() {
		return metercalcFlowId;
	}

	public void setMetercalcFlowId(String metercalcFlowId) {
		this.metercalcFlowId = metercalcFlowId;
	}

	public String getMetercalcFlowNo() {
		return metercalcFlowNo;
	}

	public void setMetercalcFlowNo(String metercalcFlowNo) {
		this.metercalcFlowNo = metercalcFlowNo;
	}

	public String getMetercalcFlowName() {
		return metercalcFlowName;
	}

	public void setMetercalcFlowName(String metercalcFlowName) {
		this.metercalcFlowName = metercalcFlowName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<LineChartVo> getCumulativeflow() {
		return cumulativeflow;
	}

	public void setCumulativeflow(List<LineChartVo> cumulativeflow) {
		this.cumulativeflow = cumulativeflow;
	}

	public String getCumulativeflowIncrementSpeed() {
		return cumulativeflowIncrementSpeed;
	}

	public void setCumulativeflowIncrementSpeed(String cumulativeflowIncrementSpeed) {
		this.cumulativeflowIncrementSpeed = cumulativeflowIncrementSpeed;
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(String amountMax) {
		this.amountMax = amountMax;
	}

	public String getCopyTime() {
		return copyTime;
	}

	public void setCopyTime(String copyTime) {
		this.copyTime = copyTime;
	}
	
}

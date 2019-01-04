package com.dyzhsw.efficient.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 过滤器首页显示VO类
 */
@ApiModel(value = "过滤器首页显示VO类")
public class FlowmeterHomeVo {

    @ApiModelProperty(value = "过滤器id")
	private String percolatorId; 

    @ApiModelProperty(value = "过滤器名称")
	private String percolatorName; 

    @ApiModelProperty(value = "流量计id")
	private String flowmeterId; 

    @ApiModelProperty(value = "过滤器对应的流量计的累计流量")
	private List<LineChartVo> flowmeterCumulativeflow ;

    @ApiModelProperty(value = "过滤器对应的流量计的瞬时流量")
	private String instantaneousflow; 

    @ApiModelProperty(value = "过滤器对应的流量计的最大瞬时流量")
	private String instantaneousflowMax; 

    @ApiModelProperty(value = "抄录时间")
	private String copyTime;

	public String getPercolatorId() {
		return percolatorId;
	}

	public void setPercolatorId(String percolatorId) {
		this.percolatorId = percolatorId;
	}

	public String getPercolatorName() {
		return percolatorName;
	}

	public void setPercolatorName(String percolatorName) {
		this.percolatorName = percolatorName;
	}

	public String getFlowmeterId() {
		return flowmeterId;
	}

	public void setFlowmeterId(String flowmeterId) {
		this.flowmeterId = flowmeterId;
	}

	public List<LineChartVo> getFlowmeterCumulativeflow() {
		return flowmeterCumulativeflow;
	}

	public void setFlowmeterCumulativeflow(List<LineChartVo> flowmeterCumulativeflow) {
		this.flowmeterCumulativeflow = flowmeterCumulativeflow;
	}

	public String getInstantaneousflow() {
		return instantaneousflow;
	}

	public void setInstantaneousflow(String instantaneousflow) {
		this.instantaneousflow = instantaneousflow;
	}

	public String getInstantaneousflowMax() {
		return instantaneousflowMax;
	}

	public void setInstantaneousflowMax(String instantaneousflowMax) {
		this.instantaneousflowMax = instantaneousflowMax;
	}

	public String getCopyTime() {
		return copyTime;
	}

	public void setCopyTime(String copyTime) {
		this.copyTime = copyTime;
	} 
	
	
}

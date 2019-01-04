package com.dyzhsw.efficient.entity;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "折线图首页显示VO类")
public class LineChartVo {

    @ApiModelProperty(value = "值1")
	private String value1;

    @ApiModelProperty(value = "抄录时间")
	private String copyTime;

    @ApiModelProperty(value = "星期几")
	private String weekday;

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getCopyTime() {
		return copyTime;
	}

	public void setCopyTime(String copyTime) {
		this.copyTime = copyTime;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
}

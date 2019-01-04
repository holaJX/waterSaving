package com.dyzhsw.efficient.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月11日
 * @description 压力计历史数据
 */
@ApiModel(value = "压力表历史数据")
public class PressureHistory {

	@ApiModelProperty(value = "数据id")
	private String id;

	@ApiModelProperty(value = "设备编号")
	private String address;

	@ApiModelProperty(value = "主通道上报数据")
	private String mainchannel;

	@ApiModelProperty(value = "辅通道上报数据")
	private String assistchannel;

	@ApiModelProperty(value = "主通道变量单位")
	private String mainchannelunits;

	@ApiModelProperty(value = "辅通道变量单位")
	private String assistchannelunits;

	@ApiModelProperty(value = "电池电压")
	private String batteryvoltage;

	@ApiModelProperty(value = "信号强度")
	private String signals;

	@ApiModelProperty(value = "数据上报时间")
	private String uploadtime;

	@ApiModelProperty(value = "主通道报警最高阀值")
	private String mainchannelhigh;

	@ApiModelProperty(value = "主通道报警最低阀值")
	private String mainchannellower;

	@ApiModelProperty(value = "压力计安全状态标识：0正常 1异常")
	private String pressuresafey;

	@ApiModelProperty(value = "设备名称")
	private String equipname;//设备名称



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMainchannel() {
		return mainchannel;
	}

	public void setMainchannel(String mainchannel) {
		this.mainchannel = mainchannel;
	}

	public String getAssistchannel() {
		return assistchannel;
	}

	public void setAssistchannel(String assistchannel) {
		this.assistchannel = assistchannel;
	}

	public String getMainchannelunits() {
		return mainchannelunits;
	}

	public void setMainchannelunits(String mainchannelunits) {
		this.mainchannelunits = mainchannelunits;
	}

	public String getAssistchannelunits() {
		return assistchannelunits;
	}

	public void setAssistchannelunits(String assistchannelunits) {
		this.assistchannelunits = assistchannelunits;
	}

	public String getBatteryvoltage() {
		return batteryvoltage;
	}

	public void setBatteryvoltage(String batteryvoltage) {
		this.batteryvoltage = batteryvoltage;
	}

	public String getSignals() {
		return signals;
	}

	public void setSignals(String signals) {
		this.signals = signals;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getMainchannelhigh() {
		return mainchannelhigh;
	}

	public void setMainchannelhigh(String mainchannelhigh) {
		this.mainchannelhigh = mainchannelhigh;
	}

	public String getMainchannellower() {
		return mainchannellower;
	}

	public void setMainchannellower(String mainchannellower) {
		this.mainchannellower = mainchannellower;
	}

	public String getPressuresafey() {
		return pressuresafey;
	}

	public void setPressuresafey(String pressuresafey) {
		this.pressuresafey = pressuresafey;
	}

	public String getEquipname() {
		return equipname;
	}

	public void setEquipname(String equipname) {
		this.equipname = equipname;
	}
	   
	   
	   
}

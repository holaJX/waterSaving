package com.dyzhsw.efficient.entity;

public class WaterConsumptionEchartsVo {

	private String pianquName ;
	
	private Double value[];
	
	private String date[];

	public String getPianquName() {
		return pianquName;
	}

	public void setPianquName(String pianquName) {
		this.pianquName = pianquName;
	}

	public Double[] getValue() {
		return value;
	}

	public void setValue(Double[] value) {
		this.value = value;
	}

	public String[] getDate() {
		return date;
	}

	public void setDate(String[] date) {
		this.date = date;
	}
	
}

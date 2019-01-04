package com.dyzhsw.efficient.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 流量计信息实体类
 */
public class GsmMeterCumulativeWater implements Serializable {


    private String id;
    
    private Date terminalTime;

    private String terminalVoltage;
   
    private String csq;

    private String version;

    private String address;
    
    private Date createTime;    
    
    private Date updateTime;
   
    private String instantaneousflow;
    
    private String pressure;
    
    private Date copyDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTerminalVoltage() {
        return terminalVoltage;
    }

    public void setTerminalVoltage(String terminalVoltage) {
        this.terminalVoltage = terminalVoltage;
    }

    public String getCsq() {
        return csq;
    }

    public void setCsq(String csq) {
        this.csq = csq;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


	public Date getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(Date copyDate) {
		this.copyDate = copyDate;
	}

	public Date getTerminalTime() {
		return terminalTime;
	}

	public void setTerminalTime(Date terminalTime) {
		this.terminalTime = terminalTime;
	}
	
	public String getInstantaneousflow() {
		return instantaneousflow;
	}

	public void setInstantaneousflow(String instantaneousflow) {
		this.instantaneousflow = instantaneousflow;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

}

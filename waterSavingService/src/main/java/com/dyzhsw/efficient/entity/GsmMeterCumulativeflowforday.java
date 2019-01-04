package com.dyzhsw.efficient.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 流量计信息实体类
 */
public class GsmMeterCumulativeflowforday implements Serializable {


    private String id;
    
    private String address;

    private Date copyDate;
   
    private String warnData;

    private String cumulativeflow;

    private String forwardCount;

    private String backwardCount;

    private Date terminalTime;
    
    private String terminalVoltage;
    
    private String csq;
    
    private String version;
    
    private Date createTime;    
    
    private Date updateTime;
    
    private Integer num;

    private Date startTime;
    
    private Date endTime;
    
    //设备名称
    private String equipmentName;
    
    //设备编号
    private String equipmentNo;
    
    //归属
    private String officeName;
    
    //用水量
    private String sumWaterDay;


    
    public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getSumWaterDay() {
		return sumWaterDay;
	}

	public void setSumWaterDay(String sumWaterDay) {
		this.sumWaterDay = sumWaterDay;
	}

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

	public String getWarnData() {
		return warnData;
	}

	public void setWarnData(String warnData) {
		this.warnData = warnData;
	}

	public String getCumulativeflow() {
		return cumulativeflow;
	}

	public void setCumulativeflow(String cumulativeflow) {
		this.cumulativeflow = cumulativeflow;
	}

	public String getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(String forwardCount) {
		this.forwardCount = forwardCount;
	}

	public String getBackwardCount() {
		return backwardCount;
	}

	public void setBackwardCount(String backwardCount) {
		this.backwardCount = backwardCount;
	}

	public Date getTerminalTime() {
		return terminalTime;
	}

	public void setTerminalTime(Date terminalTime) {
		this.terminalTime = terminalTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}

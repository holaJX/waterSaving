package com.dyzhsw.efficient.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 流量计信息实体类
 */
public class WmMetercalcFlowdetail implements Serializable {


    private String id;
    
    private String msgId;
    
    private String terminalId;
    
    private String serialNoServer;
    
    private String serialNoTerminal;
    
    private Date collectDate;
   
    private BigDecimal cumulativeflowIncrement;

    private BigDecimal instantaneousflow;

    private BigDecimal cumulativeflow;
    
    private Integer state;
    
    private Date createTime;    
    
    private Date updateTime;
   
    //设备名称
    private String equipmentName;
    
    //设备编号
    private String equipmentNo;
    
    //设备归属
    private String officeName;
    
    //用水量
    private String sfDay;


    
    public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

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
	
	public String getSfDay() {
		return sfDay;
	}

	public void setSfDay(String sfDay) {
		this.sfDay = sfDay;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getCumulativeflow() {
		return cumulativeflow;
	}

	public void setCumulativeflow(BigDecimal cumulativeflow) {
		this.cumulativeflow = cumulativeflow;
	}

	public BigDecimal getInstantaneousflow() {
		return instantaneousflow;
	}

	public void setInstantaneousflow(BigDecimal instantaneousflow) {
		this.instantaneousflow = instantaneousflow;
	}

	public BigDecimal getCumulativeflowIncrement() {
		return cumulativeflowIncrement;
	}

	public void setCumulativeflowIncrement(BigDecimal cumulativeflowIncrement) {
		this.cumulativeflowIncrement = cumulativeflowIncrement;
	}

	public String getSerialNoTerminal() {
		return serialNoTerminal;
	}

	public void setSerialNoTerminal(String serialNoTerminal) {
		this.serialNoTerminal = serialNoTerminal;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getSerialNoServer() {
		return serialNoServer;
	}

	public void setSerialNoServer(String serialNoServer) {
		this.serialNoServer = serialNoServer;
	}


	

}

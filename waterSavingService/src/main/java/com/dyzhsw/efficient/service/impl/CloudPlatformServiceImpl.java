package com.dyzhsw.efficient.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.ShowIndexDao;
import com.alibaba.fastjson.JSONObject;
import com.dyzhsw.efficient.dao.CloudPlatformDao;
import com.dyzhsw.efficient.dao.EquipmentInfoDao;
import com.dyzhsw.efficient.dao.EquipmentLogDao;
import com.dyzhsw.efficient.dao.FkqValveInfoDao;
import com.dyzhsw.efficient.dao.WmTerminalinfoDao;
import com.dyzhsw.efficient.entity.EquipmentInfo;
import com.dyzhsw.efficient.entity.EquipmentLog;
import com.dyzhsw.efficient.entity.FkqValveInfo;
import com.dyzhsw.efficient.entity.LineChartVo;
import com.dyzhsw.efficient.entity.WmTerminalinfo;
import com.dyzhsw.efficient.service.CloudPlatformService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.dyzhsw.efficient.utils.IDUtils;

@Service
public class CloudPlatformServiceImpl implements CloudPlatformService {

	@Autowired
	private ShowIndexDao showIndexDao;
	
	@Autowired
	private CloudPlatformDao cloudPlatformDao;
	
	@Autowired
	private EquipmentInfoDao equipmentInfoDao;
	
	@Autowired
	private FkqValveInfoDao fkqValveInfoDao;
	
	@Autowired
	private WmTerminalinfoDao wmTerminalinfoDao;
	
	@Autowired
	private EquipmentLogDao equipmentLogDao;
	
	//阀控器远程控制请求地址
	@Value("${webServiceUrl}")
	private String webServiceUrl;
	
	//水肥机远程控制请求地址
	@Value("${watermanureUrl}")
	private String watermanureUrl;
	
	@Override
	public Map<String, Object> switchedSystem(Integer equipmentType) {
		Map<String, Object> res = new HashMap<String, Object>();
		List<EquipmentInfo> list = cloudPlatformDao.switchedSystem(equipmentType);
		res.put("list", list);
		return res;
	}

	@Override
	public Map<String, Object> fkqValveInfo(String id) {
		Map<String, Object> res = new HashMap<String, Object>();
		EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoById(id);
		FkqValveInfo fkqValveInfo = fkqValveInfoDao.selectByEquipmentId(id);
		res.put("equipmentInfo", equipmentInfo);
		res.put("fakValveInfo", fkqValveInfo);
		return res;
	}

	@Override
	public Map<String, Object> fertilizerMachineInfo(String EquNo) {
		Map<String, Object> res = new HashMap<String, Object>();
		EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoByEquNo(EquNo);
		WmTerminalinfo wmTerminalinfo = new WmTerminalinfo();
		List<LineChartVo> dataList = new ArrayList<LineChartVo>();
		//最新出口压力值
		String equAmount = null;
		//瞬时流量
		String instantaneousflow = null;
		//累计施肥量
		String cumulativeflow = null; 
		int wm_state = 0;
		if(equipmentInfo!=null) {
			wmTerminalinfo = wmTerminalinfoDao.selectById(equipmentInfo.getEquipmentNo());
			if(wmTerminalinfo!=null) {
				cumulativeflow = cloudPlatformDao.queryCumulativeflow(wmTerminalinfo.getTerminalId());
				//获取水肥机当前开关状态
				String state = cloudPlatformDao.queryWmState(wmTerminalinfo.getTerminalId()); 
				if(state!=null&&"null".equals(state)) {
					wm_state = Integer.parseInt(state);
				}
				wmTerminalinfo.setState(wm_state);
				dataList = showIndexDao.queryFertilizerValue(wmTerminalinfo.getTerminalId());
				equAmount = showIndexDao.queryAmount(wmTerminalinfo.getTerminalId());
				instantaneousflow = showIndexDao.queryWmInstantaneousflow(wmTerminalinfo.getTerminalId());
			} 
		}
		res.put("equAmount", equAmount);
		res.put("instantaneousflow", instantaneousflow);
		res.put("cumulativeflow", cumulativeflow);
		res.put("equipmentInfo", equipmentInfo);
		res.put("wmTerminalinfo", wmTerminalinfo);
		res.put("dataList", dataList);
		return res;
	}

	@Override
	public Map<String, Object> flowmeterInfo(String id) {
		Map<String, Object> res = new HashMap<String, Object>();
		EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoById(id);
		List<LineChartVo> dataList = cloudPlatformDao.queryFilterValue(equipmentInfo.getId());
		//数据采集最新时间
		String copyTime = cloudPlatformDao.queryInstantaneousflowCopyTimeById(equipmentInfo.getId());
		res.put("equipmentInfo", equipmentInfo);
		res.put("dataList", dataList);
		res.put("copyTime", copyTime);
		return res;
	}

	@Override
	public Map<String, Object> flowmeterHistory(String id) {
		Map<String, Object> res = new HashMap<String, Object>();
		EquipmentInfo equipmentInfo = equipmentInfoDao.getEquipmentInfoById(id);
		List<LineChartVo> dataList = cloudPlatformDao.queryFilterHistory(equipmentInfo.getId());
		res.put("equipmentInfo", equipmentInfo);
		res.put("dataList", dataList);
		return res;
	}

	@Override
	public Map<String, Object> equipmentSearch(String equipmentName) {
		Map<String, Object> res = new HashMap<String, Object>();
		List<EquipmentInfo> list = equipmentInfoDao.getEquipmentListByEquName(equipmentName);
		res.put("list", list);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResponse microValveController(String equipmentId, String control1Status, String control2Status) throws IOException {
//		String[] equipmentId = equipmentIds.split(",");
		/*远程控制*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//创建请求对象
		String url = this.webServiceUrl + "control";
		paramMap.put("equipmentId", equipmentId);
		paramMap.put("control1Status",control1Status);
		paramMap.put("control2Status",control2Status);
		String result =HttpClientTools.doPost(url, paramMap);
		JSONObject obj = JSONObject.parseObject(result);
		@SuppressWarnings("rawtypes")
		BaseResponse res = new BaseResponse();
		res.setStateCode(obj.getInteger("stateCode"));
		res.setMessage(obj.getString("message"));
		res.setObject(obj);
		//保存手动控制日志
		EquipmentLog equLog = new EquipmentLog();
		equLog.setId(IDUtils.createUUID());
		if("200".equals(obj.getString("stateCode"))) {
			equLog.setType(1);
		} else {
			equLog.setType(2);
		}
		equLog.setTitle(control1Status+","+control2Status);
		equLog.setEquipmentNo(equipmentId);
		equLog.setCreateTime(new Date());
		equLog.setRemarks(obj.toJSONString());
		equipmentLogDao.insertInfo(equLog);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResponse watermanureController(String equipmentId, String switchState) throws IOException {
		/*远程控制*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//创建请求对象
		String url = this.watermanureUrl + "terminalControlReq";
		paramMap.put("switchState", switchState);
		paramMap.put("terminalId",equipmentId);
		String result =HttpClientTools.doPost(url, paramMap);
		JSONObject obj = JSONObject.parseObject(result);
		@SuppressWarnings("rawtypes")
		BaseResponse res = new BaseResponse();
		res.setStateCode(obj.getInteger("stateCode"));
		res.setMessage(obj.getString("message"));
		res.setObject(obj);
		//保存手动控制日志
		EquipmentLog equLog = new EquipmentLog();
		equLog.setId(IDUtils.createUUID());
		if("200".equals(obj.getString("stateCode"))) {
			equLog.setType(1);
		} else {
			equLog.setType(2);
		}
		equLog.setTitle(switchState+"	"+"（开关状态，0-关1-开）");
		equLog.setEquipmentNo(equipmentId);
		equLog.setCreateTime(new Date());
		equLog.setRemarks(obj.toJSONString());
		equipmentLogDao.insertInfo(equLog);
		return res;
	}

}

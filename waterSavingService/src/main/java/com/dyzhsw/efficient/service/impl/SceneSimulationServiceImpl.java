package com.dyzhsw.efficient.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dyzhsw.efficient.dao.EquipmentLogDao;
import com.dyzhsw.efficient.entity.EquipmentLog;
import com.dyzhsw.efficient.service.SceneSimulationService;
import com.dyzhsw.efficient.utils.BaseResponse;
import com.dyzhsw.efficient.utils.HttpClientTools;
import com.dyzhsw.efficient.utils.IDUtils;

@Service
public class SceneSimulationServiceImpl implements SceneSimulationService{

	@Autowired
	private EquipmentLogDao equipmentLogDao;
	
	//阀控器远程控制请求地址
	@Value("${webServiceUrl}")
	private String webServiceUrl;
	
	//水肥机远程控制请求地址
	@Value("${watermanureUrl}")
	private String watermanureUrl;

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
		String url = this.watermanureUrl + "remoteControlReq";
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

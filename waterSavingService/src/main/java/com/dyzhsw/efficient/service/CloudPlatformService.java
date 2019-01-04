package com.dyzhsw.efficient.service;

import java.io.IOException;
import java.util.Map;

import com.dyzhsw.efficient.utils.BaseResponse;

public interface CloudPlatformService {

	Map<String, Object> switchedSystem(Integer equipmentType);

	Map<String, Object> fkqValveInfo(String id);

	Map<String, Object> fertilizerMachineInfo(String id);

	Map<String, Object> flowmeterInfo(String id);

	Map<String, Object> flowmeterHistory(String id);

	Map<String, Object> equipmentSearch(String equipmentName);

	@SuppressWarnings("rawtypes")
	BaseResponse microValveController(String equipmentId, String control1Status, String control2Status) throws IOException;

	@SuppressWarnings("rawtypes")
	BaseResponse watermanureController(String equipmentId, String switchState) throws IOException;

}

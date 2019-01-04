package com.dyzhsw.efficient.service;

import java.io.IOException;

import com.dyzhsw.efficient.utils.BaseResponse;

public interface SceneSimulationService {

	BaseResponse microValveController(String equipmentId, String control1Status, String control2Status)
			throws IOException;

	BaseResponse watermanureController(String equipmentId, String switchState) throws IOException;

}

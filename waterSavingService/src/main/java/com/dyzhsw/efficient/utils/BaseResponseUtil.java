package com.dyzhsw.efficient.utils;

import com.dyzhsw.efficient.config.enums.ResultEnum;

/**
 * @author zhaoqingjie
 */
public class BaseResponseUtil {

	public static BaseResponse success(){
		return success(null);
	}

	public static BaseResponse success(Object object){
		BaseResponse response=new BaseResponse();
		response.setObject(object);
		response.setStateCode(ResultEnum.SUCCESS.getStateCode());
		response.setMessage(ResultEnum.SUCCESS.getMessage());
		return response;
	}

	public static BaseResponse success(Integer stateCode, String message, Object object){
		BaseResponse response=new BaseResponse();
		response.setObject(object);
		response.setStateCode(stateCode);
		response.setMessage(message);
		return response;
	}
	
	public static BaseResponse error(Integer stateCode,String message){
		BaseResponse response=new BaseResponse();
		response.setStateCode(stateCode);
		response.setMessage(message);
		return response;
	}


}


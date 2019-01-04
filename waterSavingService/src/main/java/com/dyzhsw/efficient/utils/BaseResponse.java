package com.dyzhsw.efficient.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author LiHD
 */
@ApiModel(value = "返回类")
public class BaseResponse<T> {
	/**错误码**/
	@ApiModelProperty(value = "状态码")
	private Integer stateCode;
	/**提示信息**/
	@ApiModelProperty(value = "提示信息")
	private String message;
	/** 返回数据 **/
	@ApiModelProperty(value = "返回的数据")
	private T object;
	
	public BaseResponse(Integer stateCode, String message, T object) {
		super();
		this.stateCode = stateCode;
		this.message = message;
		this.object = object;
	}

	public BaseResponse() {
		super();
	}


	public Integer getStateCode() {
		return stateCode;
	}

	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}


}

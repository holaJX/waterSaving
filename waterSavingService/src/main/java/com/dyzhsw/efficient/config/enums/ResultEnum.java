package com.dyzhsw.efficient.config.enums;


/**
 * @author lihd
 */
public enum ResultEnum {
	LOGIN_TOKEN_ERROR(401,"身份认证异常，请检查并重新登陆"),//登陆token异常
	LOGIN_TOKEN_INVALID(400,"身份认证过期，请重新登陆"),//登陆token过期,请重新登陆
	USER_NOT_EXIST(201,"用户不存在"),
	LOGIN_PASSWORD_ERROR(202,"登陆密码错误"),
	USER_NO_JURISDICTION(403,"用户无操作该功能权限"),
	SUCCESS(200, "请求成功"),
	ERROR(500,"后台处理异常"),
	NO_FARMER_LOGIN(5,"非农户账户不能登录APP"),
	NO_FARMER_CHANGEPASSWORD(6,"非农户账户不能修改密码"),
	OLD_PASSWORD_ERROR(7,"原密码错误"),
	CHANGE_PASSWORD_SUCCESS(8,"密码修改成功"),
	CACHE_SERVER_EXCEPTION(9, "缓存服务器异常"),
	CATCHA_TIMEOUT(10,"验证码过期"),
	CATCHA_ERROR(11,"验证码错误"),
	MSGERROR(12,"缺少客户端唯一标识"),
	INVALID_PARAMETER(13,"缺少参数"),
	INVALID_LOGINNAME_PASSWORD(14,"缺少用户名或密码"),
	INVALID_ROLEINFO(15,"用户未绑定角色信息"),
	PROHIBIT_LOGIN(16,"此用户被管理员禁止登录")
	;
	
	private Integer stateCode;
	private String message;

	ResultEnum(Integer stateCode, String message) {
		this.stateCode = stateCode;
		this.message = message;
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
}



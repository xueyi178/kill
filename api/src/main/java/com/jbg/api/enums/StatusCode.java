package com.jbg.api.enums;
/**
 * 1.通用状态码
 * 项目名称：api 
 * 类名称：StatusCode
 * 开发者：Lenovo
 * 开发时间：2019年10月5日上午10:55:40
 */
public enum StatusCode {

	Success(0,"成功"),
	Fail(-1,"失败"),
	InvalidParams(201,"非法的参数!"),
	UserNotLogin(202,"用户没登录"),;

	private Integer code;
	private String msg;

	StatusCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

package com.jbg.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 1.基础的Controller
 * 项目名称：server 
 * 类名称：BaseController
 * 开发者：Lenovo
 * 开发时间：2019年10月5日下午3:11:27
 */
@Controller
@RequestMapping(value = "/base")
public class BaseController {

	private final static Logger log = LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * 1.跳转欢迎页面
	 * @return
	 */
	@RequestMapping(value ="/welcome", method = RequestMethod.GET)
	public String welcome() {
		log.info("进入到了BaseController中的welcome方法!");
		return "welcome";
	}
	
	/**
	 * 2.跳转错误页面
	 * @return
	 */
	@RequestMapping(value ="/error", method = RequestMethod.GET)
	public String error() {
		log.info("进入到了BaseController中的welcome方法!");
		return "error";
	}
	
}

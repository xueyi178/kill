package com.jbg.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbg.api.enums.StatusCode;
import com.jbg.api.response.BaseResponse;
import com.jbg.server.dto.KillDto;
import com.jbg.server.service.IKillService;

/**
 * 1.秒杀的controller
 * 项目名称：server 
 * 类名称：KillController
 * 开发者：Lenovo
 * 开发时间：2019年10月6日下午4:49:40
 */
@Controller
@RequestMapping(value = "/kill")
public class KillController {

	private static final  Logger log = LoggerFactory.getLogger(KillController.class);

	@Autowired
	private IKillService killService;

	/**
	 * 1.秒杀抢购的controller
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/execute", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BaseResponse execute(@RequestBody KillDto killDto,  BindingResult result) {
		if(result.hasErrors() || killDto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}

		BaseResponse response = new BaseResponse<>(StatusCode.Success);

		//是否抢购成功
		Boolean falg = killService.killItem(killDto.getKillId(), killDto.getUserId());
		if(!falg) {
			return new BaseResponse<>(StatusCode.Fail.getCode(), "哈哈~~~商品抢购完毕或者不在抢购的时间段!");
		}
		return response;
	}
	
	/**
	 * 2.抢购成功跳转的页面
	 * @return
	 */
	@RequestMapping(value = "/execute/success", method = RequestMethod.GET)
	public String executeSuccess() {
		System.out.println("抢购成功了.....");
		return "executeSuccess";
	}
	
	/**
	 * 3.抢购失败跳转的页面
	 * @return
	 */
	@RequestMapping(value = "/execute/fail", method = RequestMethod.GET)
	public String executeFail() {
		return "executeFail";
	}
	
	/**
	 * 1.秒杀抢购的controller
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/executeLock", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BaseResponse executeLock(@RequestBody KillDto killDto,  BindingResult result) throws Exception {
		if(result.hasErrors() || killDto.getKillId() <= 0) {
			return new BaseResponse(StatusCode.InvalidParams);
		}

		BaseResponse response = new BaseResponse<>(StatusCode.Success);

		//是否抢购成功基于mysql的优化
		///Boolean falg = killService.killItemV2(killDto.getKillId(), killDto.getUserId());
		
		//基于redis分布式锁的判断优化
		//Boolean falg = killService.killItemV3(killDto.getKillId(), killDto.getUserId());
		
		//使用redisson的分布式锁来优化
		Boolean falg = killService.killItemV4(killDto.getKillId(), killDto.getUserId());
		if(!falg) {
			return new BaseResponse<>(StatusCode.Fail.getCode(), "哈哈~~~商品抢购完毕或者不在抢购的时间段!");
		}
		return response;
	}
	
}

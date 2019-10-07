package com.jbg.server.service;
/**
 * 1.Rabbit发送消息的服务
 * 项目名称：server 
 * 类名称：RabbitSenderService
 * 开发者：Lenovo
 * 开发时间：2019年10月7日下午2:13:55
 */
public interface RabbitSenderService {

	/**
	 * 1.秒杀成功异步发送邮件通知的消息
	 * @return
	 */
	public Boolean sendKillSuccessEmailMsg(String orderNo);
}

package com.jbg.server.service.impl;

import javax.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import com.jbg.server.service.RabbitSenderService;

/**
 * 1.Rabbit发送消息的业务逻辑层实现类
 * 项目名称：server 
 * 类名称：RabbitSenderServiceImpl
 * 开发者：Lenovo
 * 开发时间：2019年10月7日下午2:16:08
 */
@Service
public class RabbitSenderServiceImpl implements RabbitSenderService {

	private static final  Logger log = LoggerFactory.getLogger(RabbitSenderServiceImpl.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Environment env;
	/**
	 * 1..秒杀成功异步发送邮件通知的消息
	 */
	@Override
	public Boolean sendKillSuccessEmailMsg(String orderNo) {
		log.info("秒杀成功异步发送邮件通知的消息---准备发送消息:{}", orderNo);
		//rabbit:mq发送消息的逻辑
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
		rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));
		rabbitTemplate.convertAndSend(MessageBuilder.withBody(orderNo.getBytes()));
		return true;
	}

}

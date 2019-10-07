package com.jbg.server.utils;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

import org.joda.time.DateTime;

/**
 * 1.生成订单编号
 * 项目名称：server 
 * 类名称：RandomUtils
 * 开发者：Lenovo
 * 开发时间：2019年10月6日下午4:04:04
 */
public class RandomUtils {
	
	private static final  SimpleDateFormat dataformatOne = new SimpleDateFormat("yyyyMMddHHmmss");

	//高并发的情况下保证线程是安全的,是单列模式
	private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
	
	/**
	 * 1.生成订单编号  时间戳+N为随机流水号
	 * @return
	 */
	public static String generateOrderCode() {
		return dataformatOne.format(DateTime.now().toDate())+generateNumber(4);
	}
	
	/**
	 * 生成随机数
	 * @param num
	 * @return
	 */
	public static String generateNumber(final int num) {
		StringBuffer stringBuffer = new StringBuffer();
		for(int i=1; i<= num; i++) {
			stringBuffer.append(RANDOM.nextInt(9));
		}
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			System.out.println(generateOrderCode());
		}
	}
}

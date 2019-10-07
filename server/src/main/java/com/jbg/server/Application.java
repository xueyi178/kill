package com.jbg.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.server模块的启动类
 * 项目名称：server 
 * 类名称：Application
 * 开发者：Lenovo
 * 开发时间：2019年10月5日上午10:58:54
 */
@SpringBootApplication
@MapperScan("com.jbg.model.mapper")
public class Application{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

package com.jbg.model.vo;

import com.jbg.model.entity.ItemKill;

import lombok.Data;
/**
 * 1.返回的实体类
 * 项目名称：model 
 * 类名称：ItemKillVo
 * 开发者：Lenovo
 * 开发时间：2019年10月6日下午3:04:30
 */
@Data
public class ItemKillVo extends ItemKill{

	private String itemName;
	
	//采用服务器的时间,控制是否可以进行抢购
	private Integer canKill;
}

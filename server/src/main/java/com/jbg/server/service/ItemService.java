package com.jbg.server.service;

import java.util.List;

import com.jbg.model.vo.ItemKillVo;
/**
 * 1.商品业务逻辑层的接口
 * 项目名称：server 
 * 类名称：ItemService
 * 开发者：Lenovo
 * 开发时间：2019年10月6日上午11:48:11
 */
public interface ItemService {

	/**
	 * 1.获取待秒杀的商品列表
	 * @return
	 */
	List<ItemKillVo> getKillItems(); 
	
	/**
	 * 2.获取商品详情列表
	 * @param id
	 * @return
	 */
	ItemKillVo getDetail(Integer id);
}

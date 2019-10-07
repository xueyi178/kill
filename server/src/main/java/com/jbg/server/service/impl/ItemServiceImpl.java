package com.jbg.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbg.model.mapper.ItemKillMapper;
import com.jbg.model.vo.ItemKillVo;
import com.jbg.server.service.ItemService;
/**
 * 1.商品的业务逻辑层实现类
 * 项目名称：server 
 * 类名称：ItemServiceImpl
 * 开发者：Lenovo
 * 开发时间：2019年10月6日上午11:47:54
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private ItemKillMapper itemKillMapper;
	
	/**
	 * 1.获取待秒杀的列表
	 */
	@Override
	public List<ItemKillVo> getKillItems() {
		return itemKillMapper.listItemKill();
	}

	/**
	 * 2.根据id获取商品详情列表
	 */
	@Override
	public ItemKillVo getDetail(Integer id) {
		return itemKillMapper.getItemKill(id);
	}
}

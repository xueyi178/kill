package com.jbg.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jbg.model.vo.ItemKillVo;
import com.jbg.server.service.ItemService;

/**
 * 1.商品详情的controller 
 * 项目名称：server 
 * 类名称：ItemController
 * 开发者：Lenovo
 * 开发时间：2019年10月6日上午11:08:53
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController {

	private static final  Logger log = LoggerFactory.getLogger(ItemController.class);

	private static final String prefix = "list";

	@Autowired
	private ItemService itemService;

	/**
	 * 1.跳转到list页面
	 * @return
	 */
	@RequestMapping(value = {"/", "/index", prefix+"/list", prefix+"index.html"}, method = RequestMethod.GET)
	public String list(ModelMap modelMap) {
		try {
			List<ItemKillVo> killItems = itemService.getKillItems();
			modelMap.put("list", killItems);
			log.info("获取的待秒杀的商品列表: {}", killItems);
		} catch (Exception e) {
			log.error("获取带秒杀列表时发生异常[{}]",e.getMessage());
			return "redirect:/base/error";
		}
		return "list";
	}
	
	/**
	 * 2.获取商品详情列表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Integer id, ModelMap modelMap) {
		if(null == id || id <= 0) {
			return "redirect:/base/error";
		}
		try {
			ItemKillVo detail = itemService.getDetail(id);
			modelMap.put("detail", detail);
		} catch (Exception e) {
			log.error("获取商品详情时发生异常[{}]",e.getMessage());
			return "redirect:/base/error";
		}
		
		return "info";
	}
}

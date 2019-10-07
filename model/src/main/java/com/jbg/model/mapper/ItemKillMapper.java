package com.jbg.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbg.model.entity.ItemKill;
import com.jbg.model.vo.ItemKillVo;

public interface ItemKillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemKill record);

    int insertSelective(ItemKill record);

    ItemKill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemKill record);

    int updateByPrimaryKey(ItemKill record);
    
    /**
     * 1.获取待抢购的商品列表
     * @return
     */
    List<ItemKillVo> listItemKill();
    
    /**
     * 2.根据id获取商品详情
     * @param id
     * @return
     */
    ItemKillVo getItemKill(Integer id);
    
    /**
     * 3.减去库存数量
     * @param killId
     * @return
     */
    int updateKillItem(@Param("killId") Integer killId);
    
    /**
     * 4.根据id获取商品详情(Mysql层面的优化)
     * @param id
     * @return
     */
    ItemKillVo getItemKill2(Integer id);
    
    /**
     * 5.减去库存数量
     * @param killId
     * @return
     */
    int updateKillItem2(@Param("killId") Integer killId);
}
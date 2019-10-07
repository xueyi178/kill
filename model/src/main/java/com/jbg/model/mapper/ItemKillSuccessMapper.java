package com.jbg.model.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbg.model.entity.ItemKillSuccess;

public interface ItemKillSuccessMapper {
    int deleteByPrimaryKey(String code);

    int insert(ItemKillSuccess record);

    int insertSelective(ItemKillSuccess record);

    ItemKillSuccess selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(ItemKillSuccess record);

    int updateByPrimaryKey(ItemKillSuccess record);
    
    /**
     * 1.根据商品id和用户id查询是否抢购该商品的数据
     * @param killId
     * @param userId
     * @return
     */
    int countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);
}
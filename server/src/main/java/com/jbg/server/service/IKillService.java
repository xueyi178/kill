package com.jbg.server.service;
/**
 * 1.秒杀的业务逻辑层接口
 * 项目名称：server 
 * 类名称：IKillService
 * 开发者：Lenovo
 * 开发时间：2019年10月6日下午4:19:08
 */
public interface IKillService {
	
	/**
	 * 1.秒杀抢购的方法
	 * @param killId
	 * @param userId
	 * @return
	 */
	Boolean killItem(Integer killId,Integer userId);

	/**
	 * 2.秒杀的抢购方法,Mysql进行优化
	 * @param killId
	 * @param userId
	 * @return
	 */
    Boolean killItemV2(Integer killId, Integer userId);

    /**
     * 3.秒杀的抢购方法,使用redis分布式锁来进行优化
     * @param killId
     * @param userId
     * @return
     */
    Boolean killItemV3(Integer killId, Integer userId);

    /**
     * 4.秒杀的抢购方法,使用redisson的分布式锁来优化
     * @param killId
     * @param userId
     * @return
     */
    Boolean killItemV4(Integer killId, Integer userId)throws Exception;

    Boolean killItemV5(Integer killId, Integer userId);

}

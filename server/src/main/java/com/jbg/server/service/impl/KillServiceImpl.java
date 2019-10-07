package com.jbg.server.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.jbg.model.entity.ItemKillSuccess;
import com.jbg.model.mapper.ItemKillMapper;
import com.jbg.model.mapper.ItemKillSuccessMapper;
import com.jbg.model.vo.ItemKillVo;
import com.jbg.server.enums.SysConstant;
import com.jbg.server.service.IKillService;
import com.jbg.server.service.RabbitSenderService;
import com.jbg.server.utils.RandomUtils;
import com.jbg.server.utils.SnowFlake;
/**
 * 1.描述的业务逻辑层实现类
 * 项目名称：server 
 * 类名称：KillServiceImpl
 * 开发者：Lenovo
 * 开发时间：2019年10月6日下午4:19:55
 */
@Service
public class KillServiceImpl implements IKillService {

	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	@Autowired
	private ItemKillMapper itemKillMapper;

	@Autowired
	private RabbitSenderService rabbitSenderService;

	@Autowired
	private RedisTemplate redisTemplate;

	private SnowFlake snowFlake = new SnowFlake(2, 3);

	/**
	 * 1.抢购的商品逻辑
	 * 出现的问题: 商品抢购出现负数
	 * 出现超卖的问题: 没有控制好多个线程对于共享的数据共享的代码进行控制,
	 * 原因在于: 查入的逻辑,晚于前面的判断
	 */
	@Override
	public Boolean killItem(Integer killId, Integer userId) {
		Boolean result=false;
		//判断当前用户是否已经抢购该商品
		if(itemKillSuccessMapper.countByKillUserId(killId,userId) <= 0) {
			//查询待秒杀的商品详情
			ItemKillVo itemKill = itemKillMapper.getItemKill(killId);
			//判断当前是否可以被秒杀
			if(null != itemKill && 1 == itemKill.getCanKill()) {
				//减去商品数量
				int falg = itemKillMapper.updateKillItem(killId);
				//TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
				if(falg > 0) {
					Boolean falgb = commonRecordKillSuccessInfo(itemKill, userId);
					if(falgb) {
						return result = falgb;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 2.使用异步进行通知
	 * @param kill
	 * @param userId
	 * @return
	 */
	private Boolean commonRecordKillSuccessInfo(ItemKillVo itemKill,Integer userId) {
		String orderNo = String.valueOf(snowFlake.nextId());
		ItemKillSuccess itemKillSuccess = ItemKillSuccess.builder()
				//.code(RandomUtils.generateOrderCode())   //传统时间戳+N位随机数
				.code(orderNo)  //使用雪花算法
				.itemId(itemKill.getItemId())
				.killId(itemKill.getId())
				.userId(userId.toString())
				.createTime(new Date())
				.status(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue()).build();

		int falg = itemKillSuccessMapper.insertSelective(itemKillSuccess);
		if(falg > 0) {
			/*Boolean falgm = rabbitSenderService.sendKillSuccessEmailMsg(orderNo);
			if(!falgm) {
				return false;
			}*/
			return true;
		}
		return false;
	}



	/**
	 * 2.秒杀的抢购方法,Mysql进行优化
	 * @param killId
	 * @param userId
	 * @return
	 */
	@Override
	public Boolean killItemV2(Integer killId, Integer userId)  {
		Boolean result=false;
		//判断当前用户是否已经抢购该商品
		if(itemKillSuccessMapper.countByKillUserId(killId,userId) <= 0) {
			//查询待秒杀的商品详情
			ItemKillVo itemKill = itemKillMapper.getItemKill2(killId);
			//判断当前是否可以被秒杀
			if(null != itemKill && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
				//减去商品数量
				int falg = itemKillMapper.updateKillItem2(killId);
				//TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
				if(falg > 0) {
					Boolean falgb = commonRecordKillSuccessInfo2(itemKill, userId);
					if(falgb) {
						return result = falgb;
					}
				}
			}
		}
		return result;
	}


	/***
	 * 4.使用异步进行通知,mysql的优化方法
	 * @param itemKill
	 * @param userId
	 * @return
	 */
	private Boolean commonRecordKillSuccessInfo2(ItemKillVo itemKill,Integer userId) {
		String orderNo = String.valueOf(snowFlake.nextId());
		ItemKillSuccess itemKillSuccess = ItemKillSuccess.builder()
				//.code(RandomUtils.generateOrderCode())   //传统时间戳+N位随机数
				.code(orderNo)  //使用雪花算法
				.itemId(itemKill.getItemId())
				.killId(itemKill.getId())
				.userId(userId.toString())
				.createTime(new Date())
				.status(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue()).build();

		//学以致用，举一反三 -> 仿照单例模式的双重检验锁写法,
		if (itemKillSuccessMapper.countByKillUserId(itemKill.getId(),userId) <= 0){

			int falg = itemKillSuccessMapper.insertSelective(itemKillSuccess);
			if(falg > 0) {
				/*Boolean falgm = rabbitSenderService.sendKillSuccessEmailMsg(orderNo);
			if(!falgm) {
				return false;
			}*/
				return true;
			}
		}
		return false;
	}


	/**
	 * 3.秒杀的抢购方法,使用redis分布式锁来进行优化
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	public Boolean killItemV3(Integer killId, Integer userId) {
		Boolean result=false;
		//判断当前用户是否已经抢购该商品
		if(itemKillSuccessMapper.countByKillUserId(killId,userId) <= 0) {

			//借助Redis的原子性操作实现分布式锁,对共享操作-资源进行控制
			ValueOperations<String, String> valueOpsForValue = redisTemplate.opsForValue();
			//创建的Key
			String key = new StringBuffer().append(killId).append(userId).append("-RedisLock").toString();

			//创建的value
			String value = RandomUtils.generateOrderCode();
			System.out.println("redis中的key=="+key+"redis中的value==="+ value);
			//setIfAbsent方法: 如果键不存在则新增,存在则不改变已经有的值。
			Boolean boolean1 = valueOpsForValue.setIfAbsent(key, value);
			//如果redis宕机了,会出现死锁的情况
			if(boolean1) {
				redisTemplate.expire(key, 40, TimeUnit.SECONDS);
				try {
					//查询待秒杀的商品详情
					ItemKillVo itemKill = itemKillMapper.getItemKill2(killId);
					//判断当前是否可以被秒杀
					if(null != itemKill && 1 == itemKill.getCanKill()) {
						//减去商品数量
						int falg = itemKillMapper.updateKillItem2(killId);
						//TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
						if(falg > 0) {
							Boolean falgb = commonRecordKillSuccessInfo(itemKill, userId);
							if(falgb) {
								return result = falgb;
							}
						}
					}
				} finally {
					//释放锁
					if(value.equals(valueOpsForValue.get(key).toString())) {
						redisTemplate.delete(key);
					}
				}

			}
		}
		return result;
	}


	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 4.秒杀的抢购方法,使用redisson的分布式锁来优化
	 * @throws InterruptedException 
	 */
	@Override
	public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
		Boolean result=false;

		//创建锁对象的key
		final String lockKey =	new StringBuffer().append(killId).append(userId).append("-RedissonLock").toString();
		//获取锁对象,RLock对象完全符合Java的Lock规范。也就是说只有拥有锁的进程才能解锁，其他进程解锁则会抛出IllegalMonitorStateException错误。
		RLock lock = redissonClient.getLock(lockKey);
		try {
			Boolean cacheRes = lock.tryLock(30,10,TimeUnit.SECONDS);
			if(cacheRes) {
				//判断当前用户是否已经抢购该商品
				if(itemKillSuccessMapper.countByKillUserId(killId,userId) <= 0) {
					//查询待秒杀的商品详情
					ItemKillVo itemKill = itemKillMapper.getItemKill2(killId);
					//判断当前是否可以被秒杀
					if(null != itemKill && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
						//减去商品数量
						int falg = itemKillMapper.updateKillItem2(killId);
						//TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
						if(falg > 0) {
							Boolean falgb = commonRecordKillSuccessInfo2(itemKill, userId);
							if(falgb) {
								return result = falgb;
							}
						}
					}
				}
			}
		} finally {
			lock.unlock();
			//强制释放锁
			//lock.forceUnlock();
			
		}
		return result;
	}

	@Override
	public Boolean killItemV5(Integer killId, Integer userId)  {
		// TODO Auto-generated method stub
		return null;
	}




}

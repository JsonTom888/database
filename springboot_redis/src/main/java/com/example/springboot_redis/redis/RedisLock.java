package com.example.springboot_redis.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author tom
 * @version V1.0
 * @date 2020/10/27 23:11
 */
@Service
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取redis分布式锁
     * 分布式锁主要使用redis中的setnx和getset命令，
     * 这两个方法在redisTemplate分别是setIfAbsent和getAndSet方法
     *
     * @param key
     * @param value
     * @return
     */
    public boolean getDistributedLock(String key, String value) {
        //setIfAbsent方法相当于jedis中的setnx，如果能赋值就返回true，如果已经有值了，就返回false
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //如果已经有值，获取当前的值
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(currentValue) &&
                Long.valueOf(currentValue) < System.currentTimeMillis()) {
            /*获取上一个锁的时间 如果高并发的情况可能会出现已经被修改的问题 ，
            所以多一次判断保证线程的安全*/
            //getAndSet表示将给定 key 的值设为 value ，并返回 key 的旧值(old value)
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.isNotEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 释放redis分布式锁
     *
     * @param key
     * @param value
     */
    public void releaseDistributedLock(String key, String value) {
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(currentValue) && currentValue.equals(value)) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
    }


}

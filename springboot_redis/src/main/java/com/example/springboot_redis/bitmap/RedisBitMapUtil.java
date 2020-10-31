package com.example.springboot_redis.bitmap;


import com.example.springboot_redis.common.SpringUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * bitmap工具类
 * @author tom
 * @version V1.0
 * @date 2020/10/31 22:02
 */
public  class RedisBitMapUtil {

    private static StringRedisTemplate redisTemplate = SpringUtils.getBean(StringRedisTemplate.class);

    /**
     * 设置key
     * @param key
     * @param offset 偏移量
     * @param value
     * @return
     */
    public static Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }


    public static Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    public static Long bitCount(String key) {
        return (long)redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }


    public static Long bitCount(String key, int start, int end) {
        return (long)redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes(), start, end));
    }

    public static Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        byte[][] bytes = new byte[desKey.length][];
        for (int i = 0; i < desKey.length; i++) {
            bytes[i] = desKey[i].getBytes();
        }
        return (long)redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
    }

    public static Long bitOpResult(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        bitOp(op, saveKey, desKey);
        return bitCount(saveKey);
    }




}

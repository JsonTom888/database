package com.example.springboot_redis.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * 配置主键的生成策略，如果不配置会默认使用参数作为主键
 * @author tom
 * @version V1.0
 * @date 2020/10/27 21:40
 */
//@Configuration
//@EnableCaching//此注解开启缓存
public class RedisConfig extends CachingConfigurerSupport {

//    @Bean
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(o.getClass().getName());
                stringBuilder.append(method.getName());
                for (Object object : objects){
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        };
    }

}

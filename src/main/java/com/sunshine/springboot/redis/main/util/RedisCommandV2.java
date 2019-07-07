package com.sunshine.springboot.redis.main.util;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 封装Redisoon客户端的一些redis指令
 */
@Component
public class RedisCommandV2 {
    @Autowired
    public RedisCommandV2(@Qualifier("redisson1") RedissonClient redissonClient){
        RedisCommandV2.redissonClient1 = redissonClient ;
    }
    private static RedissonClient redissonClient1 ;
    public static void set(String key, String value){
        RBucket rBucket = redissonClient1.getBucket(key) ;
        rBucket.set(value);
    }
    public static String get(String key){
        RBucket rBucket = redissonClient1.getBucket(key) ;
        return StringUtil.toString(rBucket.get()) ;
    }
    public static void hset(String key, String fieldKey, String fieldValue){
        RMap rMap = redissonClient1.getMap(key) ;
        rMap.put(fieldKey,fieldValue) ;
    }
    public static String hget(String key,String fieldKey){
        RMap rMap = redissonClient1.getMap(key) ;
        return StringUtil.toString(rMap.get(fieldKey)) ;
    }
}

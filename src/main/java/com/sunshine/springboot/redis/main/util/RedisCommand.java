package com.sunshine.springboot.redis.main.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCommand {

    /**
     * 由于spring的自动装配是在bean实例化的时候才会进行
     * 所以对于静态变量的自动装配要么用构造方法 要么用setter
     * @param stringRedisTemplate
     */
    @Autowired
    public RedisCommand(StringRedisTemplate stringRedisTemplate){
        RedisCommand.stringRedisTemplate = stringRedisTemplate ;
    }
    public static StringRedisTemplate stringRedisTemplate ;

    public static String get(String key){
        return stringRedisTemplate.opsForValue().get(key) ;
    }
    public static void set(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }
    public static void setEx(String key,String value,long timeOut){
        stringRedisTemplate.opsForValue().set(key,value,timeOut, TimeUnit.MILLISECONDS);
    }
    public static Boolean setNx(String key,String value){
        return stringRedisTemplate.opsForValue().setIfAbsent(key,value);
    }
    /**
     * 存储String类型的值 并且设置过期时间 如果key已经存在  则返回false
     * @param key
     * @param value
     * @return
     */
    public static Boolean setNx(String key,String value,long timeOut){
        return stringRedisTemplate.opsForValue().setIfAbsent(key,value,timeOut,TimeUnit.MILLISECONDS) ;
    }
    public static Long strlen(String key){
        return stringRedisTemplate.opsForValue().size(key) ;
    }
    public static Integer append(String key,String value){
        return stringRedisTemplate.opsForValue().append(key, value) ;
    }
    public static Long incr(String key){
        return stringRedisTemplate.opsForValue().increment(key) ;
    }
    public static Long incrBy(String key,long value){
        return stringRedisTemplate.opsForValue().increment(key,value) ;
    }
    public static Double incrBy(String key,double value){
        return stringRedisTemplate.opsForValue().increment(key,value) ;
    }
    public static Long decr(String key){
        return stringRedisTemplate.opsForValue().decrement(key) ;
    }
    public static Long decrBy(String key,long value){
        return stringRedisTemplate.opsForValue().decrement(key,value) ;
    }
    public static void hset(String key,String field,String value){
        stringRedisTemplate.opsForHash().put(key,field,value);
    }
    public static void hmset(String key, Map map){
        stringRedisTemplate.opsForHash().putAll(key,map);
    }
    public static Map hgetAll(String key){
        return stringRedisTemplate.opsForHash().entries(key) ;
    }
}

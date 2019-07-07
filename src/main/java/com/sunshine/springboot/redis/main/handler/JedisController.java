package com.sunshine.springboot.redis.main.handler;

import com.sunshine.springboot.redis.main.util.HttpCode;
import com.sunshine.springboot.redis.main.util.RedisCommand;
import com.sunshine.springboot.redis.main.util.BloomFilterUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试jedis的客户端封装的redis命令
 */
@RestController
public class JedisController {
    @GetMapping("/get")
    public String get(String key){
        return RedisCommand.get(key);
    }
    @PostMapping("/set")
    public Integer set(String key,String value){
        Integer result = HttpCode.SUCCESS ;
        try {
            RedisCommand.set(key,value);
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @PostMapping("/setNx")
    public Integer setNx(String key,String value){
        Integer result = HttpCode.SUCCESS ;
        Boolean flag ;
        try {
            flag = RedisCommand.setNx(key,value);
            if (!flag){
                result = HttpCode.FAILED ;
            }
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @PostMapping("/setEx")
    public Integer setEx(String key,String value,long timeOut){
        Integer result = HttpCode.SUCCESS ;
        try {
            RedisCommand.setEx(key,value,10000);
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @PostMapping("/setNxTimeOut")
    public Integer setNx(String key,String value,Long timeOut){
        Integer result = HttpCode.SUCCESS ;
        Boolean flag ;
        try {
            flag = RedisCommand.setNx(key,value,10000);
            if (!flag){
                result = HttpCode.FAILED ;
            }
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @GetMapping("/strlen")
    public Long strlen(String key){
        return RedisCommand.strlen(key) ;
    }
    @PostMapping("/append")
    public Integer append(String key,String value){
        return RedisCommand.append(key,value) ;
    }
    @PostMapping("/incr")
    public Long incr(String key){
        return RedisCommand.incr(key) ;
    }
    @PostMapping("/decr")
    public Long decr(String key){
        return RedisCommand.decr(key) ;
    }
    @PostMapping("/hset")
    public Integer hset(String key,String field,String value){
        Integer result = HttpCode.SUCCESS ;
        try {
            RedisCommand.hset(key,field,value);
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @PostMapping("/hmset")
    public Integer hmset(String key){
        Integer result = HttpCode.SUCCESS ;
        try {
            Map map = new HashMap() ;
            map.put("f1","v1") ;
            map.put("f2","v2") ;
            map.put("f3","v3") ;
            RedisCommand.hmset(key,map);
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
    @PostMapping("/hgetAll")
    public static Map hgetAll(String key){
        for (int i = 0; i < 10 ; i++) {
            BloomFilterUtil.put("SSS"+i) ;
        }
        BloomFilterUtil.mightContain("SSS6") ;
        return RedisCommand.hgetAll(key) ;
    }
}

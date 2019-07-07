package com.sunshine.springboot.redis.main.handler;

import com.sunshine.springboot.redis.main.util.HttpCode;
import com.sunshine.springboot.redis.main.util.RedisCommandV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Redisson客户端封装redis的命令
 */
@RestController
@RequestMapping("/r")
public class RedissonController {

    @GetMapping("/get")
    public String get(String key){
        return RedisCommandV2.get(key) ;
    }
    @PostMapping("/set")
    public Integer set(String key,String value){
        Integer result = HttpCode.SUCCESS ;
        try {
            RedisCommandV2.set(key,value);
        }catch (RuntimeException e){
            result = HttpCode.FAILED ;
        }
        return result ;
    }
}

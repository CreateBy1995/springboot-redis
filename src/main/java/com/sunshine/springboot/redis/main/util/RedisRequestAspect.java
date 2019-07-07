package com.sunshine.springboot.redis.main.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Aspect
public class RedisRequestAspect {
    // 表示com.sunshine.springboot.redis.main.handler包下的RedisController类中的所有方法，".."表示所有方法中的参数不限个数
    // check 表示切点名
    @Pointcut("execution(public * com.sunshine.springboot.redis.main.handler.JedisController.*(..))")
    public void check() {

    }

    @Before("check()")
    public void before(JoinPoint joinPoint) {
        Object []args = joinPoint.getArgs() ;
        if (args.length == 0 || StringUtils.isEmpty(args[0]) ){
            throw new IllegalArgumentException("key must not be null") ;
        }
    }

}

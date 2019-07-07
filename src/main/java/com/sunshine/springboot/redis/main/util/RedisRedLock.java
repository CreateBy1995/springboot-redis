package com.sunshine.springboot.redis.main.util;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisRedLock {
    @Autowired
    @Qualifier("redisson1")
    private RedissonClient redissonClient1 ;
    @Autowired
    @Qualifier("redisson2")
    private RedissonClient redissonClient2 ;
    @Autowired
    @Qualifier("redisson3")
    private RedissonClient redissonClient3 ;

    /**
     * 通过三个redis节点来构造一个红锁
     * @param resourceName 要加锁的临界资源
     * @return
     */
    public RedissonRedLock getRedLock(String resourceName){
        RLock lock1 = redissonClient1.getLock(resourceName);
        RLock lock2 = redissonClient2.getLock(resourceName);
        RLock lock3 = redissonClient3.getLock(resourceName);
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);
        return redLock ;
    }
    public boolean lock(RedissonRedLock redissonRedLock, long waitTime, long leaseTime){
        boolean flag ;
        try {
            flag = redissonRedLock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            flag = false ;
            redissonRedLock.unlock();
        }
        return flag ;
    }
    public void unlock(RedissonRedLock redissonRedLock){
        redissonRedLock.unlock();
    }
}

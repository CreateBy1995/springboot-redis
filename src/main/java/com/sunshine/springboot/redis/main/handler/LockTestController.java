package com.sunshine.springboot.redis.main.handler;

import com.sunshine.springboot.redis.main.util.RedisRedLock;
import com.sunshine.springboot.redis.main.util.SingletonLock;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试单实例redis的分布式锁
 */
@RestController
public class LockTestController {

    @Autowired
    private RedisRedLock redisRedLock ;
    @Autowired
    private SingletonLock singletonLock ;
    @GetMapping("testLock")
    public void testLock(){
        ExecutorService executorService = Executors.newFixedThreadPool(10) ;
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                lockMethod();
            });
        }
    }
    @GetMapping("testLock2")
    public void testLock2(){
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                testLock1();
            }).start();
        }
    }
    @GetMapping("testLock1")
    public void testLock1(){
        RedissonRedLock redissonRedLock = redisRedLock.getRedLock("hello") ;
        while (true){
            if(redisRedLock.lock(redissonRedLock,500,15000)){ //if(true){
                System.out.println("do something");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }finally {
                    redisRedLock.unlock(redissonRedLock);
                }
                break;
            }
        }
    }

    public void lockMethod(){
        // 对方法加锁
        singletonLock.lock("lockMethod",10000);
        try {
            // 模拟业务逻辑花费时间
            System.out.println("doing something now");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        singletonLock.unLock("lockMethod") ;
    }

}

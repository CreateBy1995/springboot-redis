package com.sunshine.springboot.redis.main.handler;

import com.sunshine.springboot.redis.main.util.SingletonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class LockTestController {
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

package com.sunshine.springboot.redis.main.util;

import org.springframework.stereotype.Component;

/**
 * 适用于单机redis的分布式锁 ， 在单点故障的情况下 ，譬如 redis 只有两个节点，
 * 一主一从，当在redis的主节点中用setnx命令设置了一个键（也就是对方法加锁），当主节点在将
 * setnx设置的键发送到从节点之前发生了宕机，导致从节点上并没有这个键，但是这个方法实际上已经被加锁，这样就会产生问题
 * 所以本方法不适用于副本集或者集群
 */
@Component
public class SingletonLock {
    // 用于记录各个调用lock方法的线程的名称
    private static ThreadLocal<String> nameThreadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName() ;
        }
    };
    /**
     * 分布式锁
     * @param key 加锁的方法名
     * @param timeOut key的过期时间 单位ms
     */
    public void lock(String key,long timeOut){
        System.out.println("当前线程--"+nameThreadLocal.get()+"---尝试加锁");
        while (true){
            // 会返回true后即可跳出循环 去执行业务方法
            // 如果setnx设值成功  就会返回true
            if(RedisCommand.setNx(key,nameThreadLocal.get(),timeOut)){
                System.out.println("当前线程--"+nameThreadLocal.get()+"---成功获取锁");
                break;
            }
        }
    }
    /**
     * 解锁方法
     * @param key 解锁的方法名
     */
    public void unLock(String key){
        // 由于本步骤不是原子性 所以需要使用lua脚本来实现
        // 譬如可能我在get和del中间夹杂着一个setnx的命令，所以这样删除的就不是自己的key了
        // 比如线程1 setnx加锁成功 过期时间为10s 然后在执行完业务方法后 调用unLock方法 此时花了9s多
        // 然后在get获取key值 发现 确实是线程1刚才设置的key ，然后在这一瞬间 key刚好过期
        // 过期的一瞬间线程2执行setnx成功 也设置了一个key 由于加锁的方法是同一个 也就是key是一样的
        // 所以此时线程1调用del删除的将会是线程2设置好的key 从而锁失效

        // 但是由于此处连接的是redis集群  集群默认不支持lua脚本 需要自己通过slot获取对应的原生的jedis连接 所以就不测试了
        // lua脚本中的命令和此段java命令是一致的，只是说lua脚本在redis中执行具有原子性
        if(RedisCommand.get(key).equals(nameThreadLocal.get())){
            System.out.println("当前线程--"+nameThreadLocal.get()+"---解锁");
            RedisCommand.del(key) ;
        }
    }
}

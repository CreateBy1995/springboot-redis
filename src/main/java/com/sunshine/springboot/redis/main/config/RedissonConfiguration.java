package com.sunshine.springboot.redis.main.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * 此处使用的是Redisson客户端，此客户端和jedis相比 主要的却别是redisson对一些分布式常用到的操作都有封装好的方法，
 * 就比如分布式锁，而且redisson对redis封装的抽象程度比较高，因为它主旨是促进使用者对redis的关注，
 * 就比如redis的set方法  在jedis中 就是 jedis.set("key","value")这样的方法，而redisson则为
 * Rbuckct rbucket = redisson.getBucket("key")  rbucket.set("value") ，封装的抽象程度较高
 *
 * 而jedis提供的redis命令会比较全面
 *
 * 由于此处要使用redis的redLock算法 所以配置了多个单实例节点
 */

@Configuration
public class RedissonConfiguration {
    @Bean("redisson1")
    public RedissonClient redisson1() throws IOException {
        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
        Config config = Config.fromYAML(RedissonConfiguration.class.getClassLoader().getResource("redisson1-config.yml"));
        return Redisson.create(config);
    }
    @Bean("redisson2")
    public RedissonClient redisson2() throws IOException {
        Config config = Config.fromYAML(RedissonConfiguration.class.getClassLoader().getResource("redisson2-config.yml"));
        return Redisson.create(config);
    }
    @Bean("redisson3")
    public RedissonClient redisson3() throws IOException {
        Config config = Config.fromYAML(RedissonConfiguration.class.getClassLoader().getResource("redisson3-config.yml"));
        return Redisson.create(config);
    }
}

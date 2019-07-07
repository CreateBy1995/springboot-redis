package com.sunshine.springboot.redis.main.util;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 布隆过滤器
 */
public class BloomFilterUtil {
    // 由于单个布隆过滤器的容量可能不够 所以可能有多个过滤器
    private static List<BloomFilter> filterLists = new ArrayList<>() ;
    // 当前使用的布隆过滤器索引
    private static int currentIndex = 0 ;
    // 过滤器初始化默认容量
    private final static int CAPACITY = 1024 ;
    // 记录每个布隆过滤器已经存放的key数 如果数量到达容量值 则扩容
    private static AtomicInteger currentCapacity = new AtomicInteger(0) ;

    /**
     * 创建一个新的布隆过滤器
     */
    private static void createFilter(){
        BloomFilter bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),CAPACITY);
        filterLists.add(bloomFilter) ;
        currentIndex = filterLists.size()-1 ;
        currentCapacity = new AtomicInteger(0) ;
    }

    /**
     * 加入key值
     * @param key
     * @return
     */
    public static boolean put(Object key){
        // key到达容量值 则扩容  或者第一次使用过滤器 则新增
        if(currentCapacity.get()==CAPACITY || filterLists.isEmpty()){
            createFilter();
        }
        BloomFilter bloomFilter = filterLists.get(currentIndex) ;
        boolean flag = bloomFilter.put(key);
        if (flag){
            // 新增成功 则记录key数
            currentCapacity.addAndGet(1) ;
        }
        return flag ;
    }
    /**
     * 遍历所有布隆过滤器查看是否存在相应的key
     * @param key
     * @return
     */
    public static boolean mightContain(Object key){
        boolean flag = false ;
        for (BloomFilter bloomFilter : filterLists){
            if ((flag=bloomFilter.mightContain(key)))
                break;
        }
        return flag ;
    }
}

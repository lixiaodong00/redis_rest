package com.li.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisDemo {
    public static void main(String[] args) {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.203.128", 6379);
        // 测试
        String ping = jedis.ping();
        System.out.println(ping);
    }

    // 操作Key
    @Test
    public void demo1() {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.116.128", 6379);

        // 添加
        jedis.set("name","lucy");

        // 查看所有key
        Set<String> keys = jedis.keys("*");
        for(String key : keys){
            System.out.println(key);
        }

    }
}

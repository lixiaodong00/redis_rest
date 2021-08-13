package com.li.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

public class PhoneCode {
    public static void main(String[] args) {
        // 1.生成随机数
        // 2.验证次数
        verifyCode("123456");
        // 3.验证验证码是否正确
//        verifyCodeTrue("123456","893733");
    }

    // 验证验证码是否正确
    public static void verifyCodeTrue(String phone,String code){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.203.128", 6379);
        // 定义验证码的key
        String codeKey = "verify"+phone+"code";
        // 获取用户输入的验证码
        String redisCode = jedis.get(codeKey);
        // 进行判断
        if (redisCode.equals(code)) {
            System.out.println("验证正确，欢迎！");
        }else {
            System.out.println("验证失败！");
        }
        jedis.close();
    }

    // 验证每天只能输入三次，并把验证码放入到redis中
    public static void verifyCode(String phone){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.203.128", 6379);

        // 定义发送次数手机验证码的key
        String phoneKeyCount = "verify"+phone+"count";
        // 定义验证码的key
        String codeKey = "verify"+phone+"code";

        // 先获取redis中的发送次数
        String count = jedis.get(phoneKeyCount);

        if(count == null) {
            // 说明是第一次发送
            jedis.setex(phoneKeyCount,24*60*60,"1");
        }else if(Integer.parseInt(count) < 3){
            jedis.incr(phoneKeyCount);
        }else {
            System.out.println("今天发送次数已到三次，明天再来吧");
            jedis.close();
        }

        // 把验证码放入
        jedis.setex(codeKey, 120, createCode());
        jedis.close();

    }

    // 生成六位验证码
    public static String createCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int i1 = random.nextInt(10);
            code += i1;
        }
        return code;
    }
}

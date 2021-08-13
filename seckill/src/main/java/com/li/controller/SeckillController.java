package com.li.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
public class SeckillController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/addKc/{pid}")
    public String addKc(@PathVariable("pid") String pid){
        redisTemplate.opsForValue().set("pro:"+pid,10);
        System.out.println("添加成功");
        return "添加成功";
    }


    // 使用post请求的方式，随机生成用户的id，这样只需要携带商品的id就行了
    @PostMapping("/seckill")
    public Boolean testRedis(@RequestParam  String pid) {
        System.out.println(pid);
        // 先判断传过来的参数是否为空
        if(pid == null){
            return false;
        }

        // 拼接key
        int uid = new Random().nextInt(1000);
        String userKey = "pro:user" + uid; // 用户key
        String proKey = "pro:"+ pid; // 商品key

        // 先判断商品的id在redis中存不存在
        if(redisTemplate.opsForValue().get(proKey) == null){
            System.out.println("秒杀还未开始");
            return false;
        }
        // 判断商品的库存是否还有
        System.out.println(redisTemplate.opsForValue().get(proKey));
        if((int)redisTemplate.opsForValue().get(proKey) <= 0){
            System.out.println("秒杀已经结束了");
            return false;
        }

        // 判断是否已经秒杀过了
        if(redisTemplate.opsForSet().isMember(userKey,uid)){
            System.out.println("你已经秒杀过了");
            return false;
        }

        // 秒杀过程
        // 商品库存减一
        redisTemplate.opsForValue().decrement(proKey);
        // 秒杀成功的用户列表里添加当前用户的id
        redisTemplate.opsForSet().add(userKey,uid);
        System.out.println("秒杀成功");
        return true;
    }


//    @GetMapping("/seckill/{uid}/{pid}")
//    public Boolean testRedis(@PathVariable String uid,@PathVariable String pid) {
//
//        // 先判断传过来的参数是否为空
//        if(uid == null || pid == null){
//            return false;
//        }
//
//        // 拼接key
//        String userKey = "pro:user"; // 用户key
//        String proKey = "pro:"+ pid; // 商品key
//
//        // 先判断商品的id在redis中存不存在
//        if(redisTemplate.opsForValue().get(proKey) == null){
//            System.out.println("秒杀还未开始");
//            return false;
//        }
//        // 判断商品的库存是否还有
//        System.out.println(redisTemplate.opsForValue().get(proKey));
//        if((int)redisTemplate.opsForValue().get(proKey) <= 0){
//            System.out.println("秒杀已经结束了");
//            return false;
//        }
//
//        // 判断是否已经秒杀过了
//        if(redisTemplate.opsForSet().isMember(userKey,uid)){
//            System.out.println("你已经秒杀过了");
//            return false;
//        }
//
//        // 秒杀过程
//        // 商品库存减一
//        redisTemplate.opsForValue().decrement(proKey);
//        // 秒杀成功的用户列表里添加当前用户的id
//        redisTemplate.opsForSet().add(userKey,uid);
//        System.out.println("秒杀成功");
//        return true;
//    }

}

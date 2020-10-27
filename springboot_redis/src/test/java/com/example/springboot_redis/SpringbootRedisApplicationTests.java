package com.example.springboot_redis;

import com.example.springboot_redis.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void stringTest() {// 对字符串的支持
        String key = "hello";
        redisTemplate.opsForValue().set(key,"JsonTom888");
        Assert.assertEquals("JsonTom888",redisTemplate.opsForValue().get("hello"));
        redisTemplate.delete(key);//删除key
//        assert redisTemplate.hasKey("hello");//判断是否存在
    }

    @Test
    void pojoTest() {//对pojo的支持
        String key = "JsonTom888";
        User user = new User("JsonTom","男",18);
        ValueOperations<String , User> operations = redisTemplate.opsForValue();
        operations.set(key,user);
        User redisUser = operations.get(key);
        Assert.assertEquals(user.toString(),redisUser.toString());

    }

    @Test
    public void testExpire() throws InterruptedException {//设置过期时间
        String key = "JsonTom888";
        User user = new User("JsonTom","男",18);
        ValueOperations<String , User> operations = redisTemplate.opsForValue();
        operations.set(key,user,100, TimeUnit.MILLISECONDS);
        Thread.sleep(1000);
        if(redisTemplate.hasKey(key)){
            User redisUser = operations.get(key);
            Assert.assertEquals(user.toString(),redisUser.toString());
        }else{
            System.out.println("key值不存在");
//            assert false;
        }
    }

    @Test
    public void testHash(){//hash类型
        String key = "hashkey";
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,"you","me");
        String value = (String)hash.get(key,"you");
        Assert.assertEquals("me",value);
    }

    @Test
    public void testList(){//List类型
        String key = "list";
        ListOperations<String,String> list = redisTemplate.opsForList();
        list.leftPush(key,"json");//从左边添加数据
        list.leftPush(key,"tom");
        list.leftPush(key,"888");
        String leftValue = list.leftPop(key);//从左边获取一个数据
        System.out.println("leftValue "+leftValue);
        String rightValue = list.rightPop(key);//从右边边获取一个数据
        System.out.println("rightValue "+rightValue);

        list.rightPush(key,"999");//从右边添加数据
        rightValue = list.rightPop(key);//从右边边获取一个数据
        System.out.println("rightValue "+rightValue);

        long lenth = (long)list.size("list");
        System.out.println("长度"+lenth);

        //获取多个值，当第三个参数+1大于最大值个数时，返回所有值
        List<String> rangeValue = list.range(key,0,10);
        for (String s:rangeValue)
            System.out.println(s);

        redisTemplate.delete(key);
    }


    @Test
    public void testSet(){
        String key1 = "key1";
        String key2 = "key2";
        SetOperations<String , String> set = redisTemplate.opsForSet();
        set.add(key1,"json");
        set.add(key1,"tom");
        set.add(key1,"888");
        Set<String> values=set.members(key1);
        for (String v:values){
            System.out.println("set value :"+v);
        }

        set.add(key2,"tom");
        set.add(key2,"888");
        set.add(key2,"999");
        Set<String> diffs = set.difference(key1,key2);
        for (String v:diffs){
            System.out.println("diffs set value :"+v);
        }

        Set<String> union = set.union(key1,key2);
        for (String u:union){
            System.out.println("union set value :"+u);
        }

        redisTemplate.delete(key1);
        redisTemplate.delete(key2);
    }

    @Test
    public void testZset(){
        String key="zset";
        redisTemplate.delete(key);
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.add(key,"json",1);
        zset.add(key,"tom",6);
        zset.add(key,"999",4);
        zset.add(key,"888",3);
        Set<String> zsets=zset.range(key,0,3);
        for (String v:zsets){
            System.out.println("zset value :"+v);
        }
        Set<String> zsetB=zset.rangeByScore(key,0,3);
        for (String v:zsetB){
            System.out.println("zsetB value :"+v);
        }
    }

}

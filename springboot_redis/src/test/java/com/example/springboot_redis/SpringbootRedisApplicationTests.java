package com.example.springboot_redis;

import com.example.springboot_redis.common.SpringUtils;
import com.example.springboot_redis.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Test
    public void testBitMaps(){//位图使用
        //此处必须使用StringRedisTemplate，否则bitcount功能失效
        StringRedisTemplate stringRedisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
        String key = "sign";
        ValueOperations operations = stringRedisTemplate.opsForValue();
        stringRedisTemplate.delete(key);
        System.out.println("key是否存在："+stringRedisTemplate.hasKey(key));
        operations.setBit(key,0,true);//设置新值返回旧值
        operations.setBit(key,1,true);
        operations.setBit(key,2,false);
        operations.setBit(key,3,true);
        operations.setBit(key,4,true);
        operations.setBit(key,5,false);
        operations.setBit(key,6,false);

        System.out.println(operations.getBit(key,4));//获取key对应的第5个位的值
        System.out.println(operations.getBit(key,6));//获取key对应的第7个位的值
        System.out.println(operations.getBit(key,7));//获取key对应的第8个位的值，如果不存在则返回false

        System.out.println(operations.getBit(key,3));//获取key对应的第4个位的值
        //统计key中value为true的数量
        long count = (long)stringRedisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
        System.out.println(count);
        //统计key中指定范围[start*8,end*8]内所有value=1的数量
        count = (long)stringRedisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes(),1,5));
        System.out.println(count);

    }



    @Test
    public void testGeoSpatial(){//reids 3.2版本之后支持
        String citys = "citys";
        String chengdu = "chengdu";
        String beijing = "beijing";
        String chongqing = "beijing";
        String luzhou = "beijing";
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(citys,new RedisGeoCommands.GeoLocation<String>(chengdu,new Point(104,30)));
        geoOperations.add(citys,new Point(116,40),beijing);
        geoOperations.add(citys,new Point(106,29),chongqing);
        geoOperations.add(citys,new Point(105,25),luzhou);

        System.out.println(geoOperations.position(citys, "chengdu"));//获取成都经纬度
        System.out.println(geoOperations.distance(citys, chengdu, beijing));//获取两地距离

        Point center = new Point(104,30);//定义中心点
        Distance radius = new Distance(800, Metrics.KILOMETERS);//定义范围
        Circle within = new Circle(center, radius);
        //返回范围内的城市
        System.out.println(geoOperations.radius(citys, within));
        //根据距离排序,返回最近的两个
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
        System.out.println(geoOperations.radius(citys, within, args));

    }

    @Test
    public void testHyperLogLog(){
        String key1 = "HyperLogLog1";
        String key2 = "HyperLogLog2";
        String desKey = "desHyperLogLog";
        HyperLogLogOperations operations = redisTemplate.opsForHyperLogLog();
        //给基数添加元素
        operations.add(key1, "a", "b", "c");
        operations.add(key2, "d", "e", "f","g");

        //返回HyperLogLog的基数值
        Long size = operations.size(key1);
        System.out.println(key1+"的基数值: " + size);

        size = operations.size(key2);
        System.out.println(key2+"的基数值: " + size);

        //获取两个基数集合的并集
        size = operations.union(desKey,key1,key2);
        System.out.println(desKey+"的基数值: " + size);
        
    }

}

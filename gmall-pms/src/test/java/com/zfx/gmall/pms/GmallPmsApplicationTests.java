package com.zfx.gmall.pms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.zfx.gmall.pms.entity.Brand;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.service.BrandService;
import com.zfx.gmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
public class GmallPmsApplicationTests {


    @Reference
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        Product byId = productService.getById(1);
        System.out.println(byId.getName());
    }

    /*
    * 测试增删改在主库，查询在从库
    * */
    @Test
    public void testC() {

        Brand brand = new Brand();
        brand.setName("大宝剑").setFirstLetter("D").setProductCommentCount(100);
        boolean save = brandService.save(brand);
        System.out.println("save----"+save);
    }

    @Test
    public void testU() {

        Brand brand = new Brand();
        brand.setName("大宝剑").setFirstLetter("D").setProductCommentCount(100).setBrandStory("爱的故事").setId(53L);
        boolean update = brandService.updateById(brand);
        System.out.println("update----"+update);
    }

    @Test
    public void testD() {
        boolean remove = brandService.removeById(53L);
        System.out.println("remove----"+remove);
    }


    @Test
    public void testR() {
        Brand byId = brandService.getById(54L);
        System.out.println("查询的数据----"+byId.getName());
    }

    /*
    * redis的测试
           redis Template.opsForValue() //操作redis中string类型的
           redisTemplate.opsForHash() //操作redis中 hash类型的
           redisTemplate.opsForlist() ////操作redis中list类型的
    * */
    @Test
    public void redisTest() {
        //redis操作k-v都为String的字符串
        stringRedisTemplate.opsForValue().set("name","zhangsan");
        System.out.println("存入redis的数据 name:"+stringRedisTemplate.opsForValue().get("name"));

    }
    @Test
    public void redisTestObject() {
        //redis操作对象
        Brand brand = new Brand();
        brand.setName("无敌").setSort(10).setBrandStory("hahahahhaha");
        redisTemplate.opsForValue().set("brand4",brand);
        System.out.println("存入redis的对象 brand3:"+redisTemplate.opsForValue().get("brand"));

    }
    @Test
    public void redisTestObjectMv() {
        Object obj = redisTemplate.opsForValue().get("brand4");
        System.out.println("存入redis的对象 brand4:"+obj);
        redisTemplate.delete("brand4");
        System.out.println("删除成功");
    }

}

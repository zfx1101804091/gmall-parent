package com.zfx.gmall.pms;

import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.zfx.gmall.pms.entity.Brand;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.service.BrandService;
import com.zfx.gmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GmallPmsApplicationTests {


    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

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
}

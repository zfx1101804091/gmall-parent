package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.constant.SysCacheConstant;
import com.zfx.gmall.pms.entity.ProductCategory;
import com.zfx.gmall.pms.mapper.ProductCategoryMapper;
import com.zfx.gmall.pms.service.ProductCategoryService;
import com.zfx.gmall.vo.product.PmsProductCategoryWithChildrenItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Slf4j
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listProductCategoryChildren(Integer i) {
        //改造菜单数据存redis
        List<PmsProductCategoryWithChildrenItem> menuItems =null;
        Object cacheMenu = redisTemplate.opsForValue().get(SysCacheConstant.CATEGORY_MENU_CACHE_KEY);
        if(cacheMenu!=null){
            //说明redis存在这个key，直接从redis中取,强转下
            log.debug("######本次使用菜单缓存########");
            menuItems= (List<PmsProductCategoryWithChildrenItem>) cacheMenu;
        }else {
            //缓存中没有，直接从数据库查，再放入缓存
            menuItems= productCategoryMapper.listProductCategoryChildren(i);
            redisTemplate.opsForValue().set(SysCacheConstant.CATEGORY_MENU_CACHE_KEY,menuItems);
        }
        return menuItems;
    }
}

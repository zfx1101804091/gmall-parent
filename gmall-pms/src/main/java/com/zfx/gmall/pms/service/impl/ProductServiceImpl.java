package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.mapper.ProductMapper;
import com.zfx.gmall.pms.service.ProductService;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}

package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.ProductAttribute;
import com.zfx.gmall.pms.mapper.ProductAttributeMapper;
import com.zfx.gmall.pms.service.ProductAttributeService;
import com.zfx.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    ProductAttributeMapper productAttributeMapper;
    
    @Override
    public PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        Page<ProductAttribute> page = new Page<ProductAttribute>(pageNum,pageSize);//从第*页开始，每页显示*条
        QueryWrapper<ProductAttribute> wrapper = new QueryWrapper<ProductAttribute>()
                .eq("product_attribute_category_id",cid)
                .eq("type",type);

        IPage<ProductAttribute> iPage = productAttributeMapper.selectPage(page,wrapper);
        return PageInfoVo.getVo(iPage,pageSize.longValue());
    }
}

package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.ProductAttributeCategory;
import com.zfx.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.zfx.gmall.pms.service.ProductAttributeCategoryService;
import com.zfx.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

    @Autowired
    ProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Override
    public PageInfoVo productAttributeCategoryPageInfo(Integer pageSize, Integer pageNum) {
        Page<ProductAttributeCategory> page = new Page<ProductAttributeCategory>(pageNum,pageSize);//从第*页开始，每页显示*条
        IPage<ProductAttributeCategory> ipage = productAttributeCategoryMapper.selectPage(page, null);
        return PageInfoVo.getVo(ipage,pageSize.longValue());
    }
}

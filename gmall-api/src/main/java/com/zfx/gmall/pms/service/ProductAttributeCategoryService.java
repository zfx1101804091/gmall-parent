package com.zfx.gmall.pms.service;

import com.zfx.gmall.pms.entity.ProductAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfx.gmall.vo.PageInfoVo;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
public interface ProductAttributeCategoryService extends IService<ProductAttributeCategory> {

    PageInfoVo productAttributeCategoryPageInfo(Integer pageSize, Integer pageNum);
}

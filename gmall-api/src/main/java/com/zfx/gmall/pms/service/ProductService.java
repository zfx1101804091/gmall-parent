package com.zfx.gmall.pms.service;

import com.zfx.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfx.gmall.vo.PageInfoVo;
import com.zfx.gmall.vo.product.PmsProductQueryParam;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
public interface ProductService extends IService<Product> {

    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);
}

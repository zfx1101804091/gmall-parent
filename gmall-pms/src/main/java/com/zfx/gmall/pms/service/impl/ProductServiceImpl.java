package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.mapper.ProductMapper;
import com.zfx.gmall.pms.service.ProductService;
import com.zfx.gmall.vo.PageInfoVo;
import com.zfx.gmall.vo.product.PmsProductQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Component
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {

        Page<Product> page = new Page<>(param.getPageNum(),param.getPageSize());//从第*页开始，每页显示*条
        QueryWrapper<Product> wrapper = new QueryWrapper<Product>();

        if(param.getBrandId()!=null){
            wrapper.eq("brand_id",param.getBrandId());
        }
        if(param.getProductCategoryId()!=null){
            wrapper.eq("product_category_id",param.getProductCategoryId());
        }
        if(!StringUtils.isEmpty(param.getProductSn())){
            wrapper.like("product_sn",param.getProductSn());
        }

        if (!StringUtils.isEmpty(param.getKeyword())){
            wrapper.like("name",param.getKeyword());
        }
        if(param.getVerifyStatus()!=null){
            wrapper.eq("verify_status",param.getVerifyStatus());
        }
        if(param.getPublishStatus()!=null){
            wrapper.eq("publish_status",param.getPublishStatus());
        }

        IPage<Product> productIPage = productMapper.selectPage(page, wrapper);

        //前端需要的PageInfoVo格式的数据
        PageInfoVo vo = PageInfoVo.getVo(productIPage, param.getPageSize());
        return vo;
    }
}

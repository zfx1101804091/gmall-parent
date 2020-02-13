package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.Product;
import com.zfx.gmall.pms.mapper.*;
import com.zfx.gmall.pms.service.ProductService;
import com.zfx.gmall.vo.PageInfoVo;
import com.zfx.gmall.vo.product.PmsProductParam;
import com.zfx.gmall.vo.product.PmsProductQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Slf4j
@Component
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    ProductFullReductionMapper productFullReductionMapper;
    @Autowired
    ProductLadderMapper productLadderMapper;
    @Autowired
    SkuStockMapper skuStockMapper;

    //当前线程共享同样的数据 把下面需要使用的productId 存入
    ThreadLocal<Long> threadLocal =  new ThreadLocal();
    //ThreadLocal底层原理
    Map<Thread,Long> map = new HashMap();

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

    @Override
    public void saveProduct(PmsProductParam productParam) {
        //1)、pms_product: 保存商品基本信息
        saveBaseInfo(productParam);

        //2)、pms_product_attribute_value: 保存这个商品对应的所有属性的值
        saveProductAttributeValue(productParam);

        //3)、pms_product_full_reduction: 保存商品的满诚信息
        saveProductFullReduction(productParam);

        //4)、pms_product_ladder: 满减表
        saveProductLadder(productParam);

        //5)、pms_sku_stock_sku_ 库存表
        saveSkuStock(productParam);
    }

    public void saveSkuStock(PmsProductParam productParam) {
        productParam.getSkuStockList().forEach(skuStock -> {
            skuStock.setProductId(threadLocal.get());
            //生成sku_code的默认值
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                skuStock.setSkuCode(getTimeStr()+"_"+threadLocal.get());
            }
            skuStockMapper.insert(skuStock);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    public void saveProductLadder(PmsProductParam productParam) {
        productParam.getProductLadderList().forEach(productLadder -> {
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    public void saveProductFullReduction(PmsProductParam productParam) {
        productParam.getProductFullReductionList().forEach(productFullReduction -> {
            productFullReduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(productFullReduction);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    public void saveProductAttributeValue(PmsProductParam productParam) {
        productParam.getProductAttributeValueList().forEach(productAttributeValue -> {
            //在pms_product_attribute_value表内需要设置其关联的productId
            productAttributeValue.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(productAttributeValue);
        });
        log.debug("saveSkuStock-->>当前线程--{}-->线程名：{}--->productID:{}-->{}"
                ,Thread.currentThread().getId(),Thread.currentThread().getName(),threadLocal.get(),map.get(Thread.currentThread()));
    }

    public void saveBaseInfo(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam,product);//将前台传递过来的数据源内的相同属性copy过来赋值给product
        productMapper.insert(product);
        //将商品基础信息的productId存入ThreadLocal中
        threadLocal.set(product.getId());
        //ThreadLocal底层原理实现
        map.put(Thread.currentThread(),product.getId());
    }

    public String getTimeStr(){
        /*
        20191531191523234  //一般用于生成订单流水号
        2019-08-31 19:15:23:234
        2019-08-31
        2019-08-31
        2019-08-31 19:15:23
         */

        LocalDateTime now=LocalDateTime.now();

        //年月日时分秒毫秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        //年月日时分秒毫秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        //生成年月日
//        System.out.println(now.format(DateTimeFormatter.ISO_DATE));
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        //年月日时分秒
//        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}

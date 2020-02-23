package com.zfx.gmall.pms.service;

import com.zfx.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfx.gmall.vo.PageInfoVo;
import com.zfx.gmall.vo.product.PmsProductParam;
import com.zfx.gmall.vo.product.PmsProductQueryParam;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
public interface ProductService extends IService<Product> {
    
    
    /*
     * 功能描述: 查询商品详情
     *  
     * @Param: 
     * @Return: 
     * @Author: Administrator
     * @Date: 2020/2/23 0023 14:41
     */
    
    Product productInfo(Long id);

    /*
     * 功能描述: 根据复杂查询条件返回分页数据
     * @Param: [productQueryParam]
     * @Return: com.zfx.gmall.vo.PageInfoVo
     * @Author: Administrator
     * @Date: 2020/2/23 0023 14:19
     */
    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);

    /*
     * 功能描述: 保存商品数据
     * 
     * @Param: [productParam]
     * @Return: void
     * @Author: Administrator
     * @Date: 2020/2/23 0023 14:20
     */
    void saveProduct(PmsProductParam productParam);

    /*
     * 功能描述: 批量上下架商品
     * 
     * @Param: [ids, publishStatus]
     * @Return: void
     * @Author: Administrator
     * @Date: 2020/2/23 0023 14:20
     */
    void updatePublishStatus(List<Long> ids, Integer publishStatus);
}

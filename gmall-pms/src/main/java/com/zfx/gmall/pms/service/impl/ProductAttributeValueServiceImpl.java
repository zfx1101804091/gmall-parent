package com.zfx.gmall.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.ProductAttributeValue;
import com.zfx.gmall.pms.mapper.ProductAttributeValueMapper;
import com.zfx.gmall.pms.service.ProductAttributeValueService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存储产品参数信息的表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Service
public class ProductAttributeValueServiceImpl extends ServiceImpl<ProductAttributeValueMapper, ProductAttributeValue> implements ProductAttributeValueService {

}

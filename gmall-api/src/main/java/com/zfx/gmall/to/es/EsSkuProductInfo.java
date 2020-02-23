package com.zfx.gmall.to.es;

import com.zfx.gmall.pms.entity.SkuStock;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: zheng-fx
 * @time: 2020/2/23 0023 14:35
 */
@Data
public class EsSkuProductInfo extends SkuStock implements Serializable {

    private String skuTitle;//sku的特定标题
    /*
     * 功能描述: 每个sku不同属性以及值
     *      颜色：黑色。白色
     *      内存：64G、128G
     */
    List<EsProductAttributeValue> attributeValues;
}

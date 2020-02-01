package com.zfx.gmall.admin.pms.vo;

import com.zfx.gmall.pms.entity.ProductCategory;
import lombok.Data;

import java.util.List;


/**
 */
@Data
public class PmsProductCategoryWithChildrenItem extends ProductCategory {
    private List<ProductCategory> children;

}

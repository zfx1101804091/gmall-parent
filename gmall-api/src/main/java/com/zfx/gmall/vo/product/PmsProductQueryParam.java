package com.zfx.gmall.vo.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * 封装页面产品查询参数的vo
 */
@Data
public class PmsProductQueryParam  implements Serializable {

    @ApiModelProperty("上架状态")
    private Integer publishStatus;

    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

    @ApiModelProperty("商品名称模糊关键字")
    private String keyword;

    @ApiModelProperty("商品货号")
    private String productSn;

    @ApiModelProperty("商品分类编号")
    private Long productCategoryId;

    @ApiModelProperty("商品品牌编号")
    private Long brandId;

    @ApiModelProperty("每页显示记录数")
    private Long pageSize = 5L;
    @ApiModelProperty("当前页码")
    private Long pageNum = 1L;

}

package com.zfx.gmall.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Data
public class PageInfoVo implements Serializable {

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("总页码")
    private Long totalPage;

    @ApiModelProperty("每页显示的记录数")
    private Long pageSize;

    @ApiModelProperty("分页查出的数据")
    private List<? extends  Object> list;

    @ApiModelProperty("当前页的页码")
    private Long pageNum;

    /*iPage:分页数据对象  size：每页显示记录数，这个从前端传过来的数据中取*/
    public static PageInfoVo getVo(IPage iPage,Long size){
        return new PageInfoVo(iPage.getTotal(),iPage.getPages(),size,iPage.getRecords(),
                iPage.getCurrent());

    }
}

package com.zfx.gmall.pms.service;

import com.zfx.gmall.pms.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfx.gmall.vo.PageInfoVo;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
public interface BrandService extends IService<Brand> {

    PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize);
}

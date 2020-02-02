package com.zfx.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.pms.entity.Brand;
import com.zfx.gmall.pms.mapper.BrandMapper;
import com.zfx.gmall.pms.service.BrandService;
import com.zfx.gmall.vo.PageInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Slf4j
@Component
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Override
    public PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        Page<Brand> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();

        QueryWrapper<Brand> name=null;
        if(!StringUtils.isEmpty(keyword)){
            name = wrapper.like("name", keyword);
        }
        IPage<Brand> brandIPage = brandMapper.selectPage(page, name);
        PageInfoVo vo = PageInfoVo.getVo(brandIPage, Long.valueOf(pageSize));
        return vo;
    }
}

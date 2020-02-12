package com.zfx.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfx.gmall.ums.entity.MemberLevel;
import com.zfx.gmall.ums.mapper.MemberLevelMapper;
import com.zfx.gmall.ums.service.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Service
@Component
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

    @Autowired
    MemberLevelMapper memberLevelMapper;
    @Override
    public Object levelList() {
        return memberLevelMapper.selectList(null);
    }
}

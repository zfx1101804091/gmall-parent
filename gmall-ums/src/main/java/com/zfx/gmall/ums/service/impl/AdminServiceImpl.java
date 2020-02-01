package com.zfx.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zfx.gmall.ums.entity.Admin;
import com.zfx.gmall.ums.mapper.AdminMapper;
import com.zfx.gmall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author zheng_fx
 * @since 2020-01-31
 */
@Component//保证容器中能用
@Service//开启服务别人用
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {

        //数据库password是MD5加密过的
        String pw = DigestUtils.md5DigestAsHex(password.getBytes());

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        QueryWrapper<Admin> queryWrapper = wrapper.eq("username", username).eq("password", pw);
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    /*
    *
    * 获取用户详情
    * */
    @Override
    public Admin getUserInfo(String userName) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",userName));
    }
}

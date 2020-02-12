package com.zfx.gmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zfx.gmall.to.CommonResult;
import com.zfx.gmall.ums.service.MemberLevelService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员相关
 */
@CrossOrigin
@RestController
@RequestMapping("/memberLevel")
public class UmsMemberlevelController {

    @Reference
    MemberLevelService memberLevelService;


    @RequestMapping("/list")
    public CommonResult memberLevelList(){
        return new CommonResult().success(memberLevelService.levelList());
    }
}

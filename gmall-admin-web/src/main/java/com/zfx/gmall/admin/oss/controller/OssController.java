package com.zfx.gmall.admin.oss.controller;


import com.zfx.gmall.admin.oss.component.OssComponent;
import com.zfx.gmall.to.CommonResult;
import com.zfx.gmall.to.OssPolicyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Oss相关操作接口
 * 	跨域也可以做安全限制
 * 	图片显示上传需要开启oss的跨域设置和读写设置
 */
//@CrossOrigin(origins = "http://192.168.25.20/***")
@CrossOrigin
@Controller
@Api(tags = "OssController",description = "Oss管理")
@RequestMapping("/aliyun/oss")
public class OssController {

	@Autowired
	OssComponent ossComponent;

	@ApiOperation(value = "oss上传签名生成")
	@GetMapping(value = "/policy")
	@ResponseBody
	public Object policy() {
		OssPolicyResult result = ossComponent.policy();
		return new CommonResult().success(result);
	}

}

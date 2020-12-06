package com.hope.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hope.model.bean.SysUserBean;
import com.hope.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:aodeng
 * @create:2018-12-10 20:13
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ISysUserService iSysUserService;

    @RequestMapping("test")
    public String test() {
        IPage<SysUserBean> list = iSysUserService.selectUserListPageVo(new Page(1, 10), new SysUserBean());
        System.out.println(list);
        return "1";
    }
}

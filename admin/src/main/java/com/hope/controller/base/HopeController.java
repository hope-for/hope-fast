package com.hope.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转路由
 * @author aodeng
 **/
@Controller
public class HopeController {

    /***
     * 首页
     * @return
     */
    @GetMapping(value = {"/", "/common/index", "/index"})
    public String index() {
        return "common/index";
    }

    /***
     * 登录
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    /**
    * 其他
    * @author aodeng
    */
    @GetMapping("/hope-boot")
    public String hopeboot() {
        return "common/hope-boot";
    }

    @GetMapping("/index_v3")
    public String index_v3() {
        return "admin/index/index_v3";
    }

    @GetMapping("/equipment/equipment")
    public String equipment() {
        return "admin/equipment/equipment";
    }

    @GetMapping("/equipment/add")
    public String equipmentAdd() {
        return "admin/equipment/add";
    }

    @GetMapping("/equipment/update")
    public String equipmentUpdate() {
        return "admin/equipment/update";
    }

    @GetMapping("/role/role")
    public String role() {
        return "admin//role/role";
    }

    @GetMapping("/role/add")
    public String roleAdd() {
        return "admin//role/add";
    }

    @GetMapping("/resource/resource")
    public String resource() {
        return "admin/resource/resource";
    }

    @GetMapping("/forgetpwd")
    public String forgetpwd() {
        return "common/forgetpwd";
    }
}
package com.hope.filter;

import cn.hutool.core.util.ObjectUtil;
import com.hope.service.impl.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token校验拦截器
 *
 * @author AoDeng
 * @date 16:08 21-8-2
 */
@Slf4j
public class TokenCheckFilter implements HandlerInterceptor {


    @Autowired
    private LoginService loginService;

    /**
     * 拦截忽略URL：
     */
    private String URLS[]={
            "/login",
            "/logout"
    };

    /**
     * 进入controller层之前拦截请求
     * @param	request
     * @param	response
     * @param	handler
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url=request.getRequestURI();
        //获取请求当中的参数
        String token = request.getHeader("token");
        String userId=request.getHeader("userId");

        //处理需要忽略拦截的请求
        for (String s : this.URLS) {
            if (ObjectUtil.equal(s,url)) return true;
        }

        //token验证
        if (!loginService.tokenCheck(token,userId)) return false;

        return true;
    }
}

package com.hope.service.impl;

import org.springframework.stereotype.Service;

/**
 * 登录相关实现
 *
 * @author AoDeng
 * @date 16:39 21-8-2
 */
@Service
public class LoginService {

    /**
     * token check
     * @param	token
     * @param	userId
     * @return boolean
     */
    public boolean tokenCheck(String token,String userId){
        //校验逻辑...

        return true;
    }
}

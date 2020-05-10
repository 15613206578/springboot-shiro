package com.zhaoy.service;

import com.zhaoy.entity.User;

/**
 * 用户service接口
 */
public interface UserService {
	
    /**根据用户名查找用户实体*/
    public User findByUserName(String userName);
    
}

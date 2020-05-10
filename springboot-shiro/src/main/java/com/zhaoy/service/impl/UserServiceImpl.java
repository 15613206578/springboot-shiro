package com.zhaoy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhaoy.entity.User;
import com.zhaoy.repository.UserRepository;
import com.zhaoy.service.UserService;

/**
 * 用户service实现类
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

}

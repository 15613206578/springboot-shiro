package com.zhaoy.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhaoy.entity.User;
import com.zhaoy.service.UserService;

public class UserRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;

	/**
	 * 执行授权逻辑
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("执行授权逻辑");
	
		//获取用户名
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        
		//给资源进行授权
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	
        //判断用户是否是管理员
        if("管理员".equals(user.getRoleName())) {
        	//赋予管理员的一些权限
        	info.addStringPermission("添加操作");
            info.addStringPermission("进入管理员主页");
        }
		
		return info;
	}
	
	
	/**
	 * 执行认证逻辑
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("执行认证逻辑");
			
		//System.out.println(token.getPrincipal());
		//getPrincipal()方法—获取当前记录的用户名
		String username = (String) token.getPrincipal();
		//根据用户名查询用户信息
		User user = userService.findByUserName(username);
		System.out.println("执行认证逻辑："+user);
		// 编写shiro判断逻辑 ，判断用户名和密码
		// 1.判断用户
        if (user == null) {
			// 用户不存在
			return null; // shiro底层会自动抛出UnKnowAccountException
        } else {
			// 2.判断密码（身份信息）
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), "");//第一个这里放的是user对象，第二个参数必须放放数据库的密码 shiro会自动判断，（可以加一个参数，盐，用来密码加密的，暂时不讲）第三个参数为当前realm的名字
            return authenticationInfo;
        }

	}

}

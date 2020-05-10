package com.zhaoy.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {


	/**
	 * add
	 */
    @RequiresPermissions(value = "添加操作")
	@RequestMapping("/toAdd")
	public String add() {
		return "/user/add";
	}
	
	/**
	 * update
	 */
	@RequestMapping("/toUpdate")
	public String update() {
		return "/user/update";
	}
	
	/**
	 * login
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	
	/**
	 * 登录逻辑处理
	 * @param username	账号
	 * @param password	密码
	 * @param model		返回msg消息
	 * @return
	 */
	@RequestMapping("/login")
	public String login(String username,String password,Model model){
		
		/**
		 * 使用Shiro编写认证操作
		 */
		//1.获取Subject
		Subject subject = SecurityUtils.getSubject();

    	//2.把用户名和密码封装为 UsernamePasswordToken 对象
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		
		//3.执行登录方法
        try {
			subject.login(token);//执行到subject.login()时，shiro会去UserRealm执行认证逻辑。
			
			//登录成功
			//跳转到test.html
			return "redirect:/testThymeleaf";
        } 
        // 若没有指定的账户, 则 shiro 将会抛出 UnknownAccountException 异常. 
        catch (UnknownAccountException uae) {
        	//登录失败:用户名不存在
			model.addAttribute("msg", "用户名不存在");
			return "login";
        } 
        // 若账户存在, 但密码不匹配, 则 shiro 会抛出 IncorrectCredentialsException 异常。 
        catch (IncorrectCredentialsException ice) {
        	//登录失败:密码错误
			model.addAttribute("msg", "密码错误");
			return "login"; 
        }             

        // 所有认证时异常的父类. 
        catch (AuthenticationException ae) {
        	model.addAttribute("msg", "其他错误！");
			return "login"; 
        }
    }

}

package com.zhaoy.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 根路径及其他请求处理
 */
@Controller
public class IndexController {
	
	/**
	 * 测试方法
	 */
	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		System.out.println("IndexController.hello()");
		return "ok";
	}
	
	/**
	 * 测试thymeleaf
	 */
	@RequestMapping("/testThymeleaf")
	public String testThymeleaf(Model model) {
		
		//在Model里存个测试数据
		model.addAttribute("test","测试");
		
		//返回test.html
		return "test";
		
	}
	
	  /**
     * 跳转管理员的主界面
     */
    @RequestMapping("/toAdmin")
    @RequiresPermissions(value = "进入管理员主页")
    public String toAdmin() {
        return "/admin/index";
    }
    
	
	
    /**
     * @ControllerAdvice注解的作用：是一个Controller增强器，可对controller中被@RequestMapping注解的方法加一些逻辑处理，最常用的就是异常处理；【三种使用场景】全局异常处理。全局数据绑定，全局数据预处理
     * @Order 注解@Order或者接口Ordered的作用是定义SpringIOC容器中Bean的执行顺序的优先级，而不是定义Bean的加载顺序，Bean的加载顺序不受@Order或Ordered接口的影响；
     * @ExceptionHandler 统一异常处理
     * 
     */
    @ControllerAdvice
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public class GlobalExceptionHandler {
    	@ExceptionHandler(value = AuthorizationException.class)
    	public String handleAuthorizationException() {
    		return "noAuth";
    	}
    }
    
    
}

package com.zhaoy.shiro;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 * 
 * @author zhaoy
 *
 */
@Configuration
public class ShiroConfig {

	/**
	 * 创建ShiroFilterFactoryBean
	 */
	@Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
	
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		//添加Shiro内置过滤器
		/**
		 * Shiro内置过滤器，可以实现权限相关的拦截器
		 *    常用的过滤器：
		 *       anon: 无需认证（登录）可以访问
		 *       authc: 必须认证才可以访问
		 *       user: 如果使用rememberMe的功能可以直接访问
		 *       perms： 该资源必须得到资源权限才可以访问
		 *       role: 该资源必须得到角色权限才可以访问
		 */

		Map<String,String> filterMap = new LinkedHashMap<String,String>();
		filterMap.put("/testThymeleaf", "anon");
		filterMap.put("/user/login", "anon");
//		filterMap.put("/user/toAdd", "authc");
//		filterMap.put("/user/toUpdate", "authc");
		
		//最后加默认全部拦截，不然会和后面添加的指定拦截
		filterMap.put("/**","authc");//这个 /* 可以代替上面两个
		
		//修改跳转的默认登录页面
		shiroFilterFactoryBean.setLoginUrl("/user/toLogin");
	
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		
		return shiroFilterFactoryBean;
	}
	
	 @Bean
	    public SecurityManager securityManager(){
	        //安全管理器
	        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	        //设置realm
	        securityManager.setRealm(userRealm());
	        return securityManager;
	    }

	    /**
	     * 身份认证 realm
	     * @return
	     */
	    @Bean
	    public UserRealm userRealm(){
	    	UserRealm userRealm = new UserRealm();
	        return userRealm;
	    }

	    /**
	     * shiro 生命周期
	     */
	    @Bean
	    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
	        return new LifecycleBeanPostProcessor();
	    }

	    /**
	     * 开启shiro的注解（如： @RequiresRoles, @RequiresPermissions），需借助SpringAOP扫描使用shiro注解的类，并在必要的时候进行安全逻辑验证
	     * 配置以下两个Bean即可实现shiro这个功能
	     */
	    @Bean
	    @DependsOn("lifecycleBeanPostProcessor")
	    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
	        //默认自动代理创建程序
	        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
	        advisorAutoProxyCreator.setProxyTargetClass(true);
	        return advisorAutoProxyCreator;
	    }

	    @Bean
	    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
	        //授权属性资源管理器
	        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
	        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());

	        return authorizationAttributeSourceAdvisor;
	    }

	    
	    /**
		 * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
		 */
		@Bean
		public ShiroDialect getShiroDialect(){
			return new ShiroDialect();
		}
	
	
}

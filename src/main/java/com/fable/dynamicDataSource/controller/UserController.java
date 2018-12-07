package com.fable.dynamicDataSource.controller;

import com.fable.dynamicDataSource.Util.SpringContextUtil;
import com.fable.dynamicDataSource.domain.Tenant;
import com.fable.dynamicDataSource.domain.User;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSource;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceContextHolder;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceRegister;
import com.fable.dynamicDataSource.service.TenantService;
import com.fable.dynamicDataSource.service.UserService;
import com.fable.dynamicDataSource.webSecurityConfig.WebSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TenantService tenantService;


	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String getUserList(@RequestParam("ds")String tenant, Map<String, Object> model){
		// 动态加载数据源
		dynamicRegisterDataSourceBean(tenant);

		List<User> users = null;
		if (!"".equals(tenant)){
			DynamicDataSourceContextHolder.setDataSource(tenant);
			users = userService.findUsersByDatasourcce(tenant);
		} else {
			DynamicDataSourceContextHolder.restoreDataSource();
			users = userService.findUsersByDefaultDatasourcce();
		}
		model.put("userList", users);
		return "user";
	}

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public String getUserList(Map<String, Object> model,HttpSession session){
		String tenant = session.getAttribute(WebSecurityConfig.SESSION_KEY).toString();
		return "redirect:/user?ds=" + tenant;
	}

	@GetMapping("/")
	public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY)String account, Model model){
		return "index";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@PostMapping("/loginVerify")
	public String loginVerify(String username,String password,HttpSession session){
		Tenant tenantUser = new Tenant();
		tenantUser.setTenantID(username);
		tenantUser.setTenantPwd(password);

		boolean verify = tenantService.verifyLogin(tenantUser);
		if (verify) {
			session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
			// 动态加载数据源
			dynamicRegisterDataSourceBean(username);
			return "index";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		DynamicDataSourceContextHolder.dataSourceNames.add("dataSource");
		return "redirect:/login";
	}

	private void dynamicRegisterDataSourceBean(String tenantID){
		if(DynamicDataSourceContextHolder.containsDataSource(tenantID)){
			return;
		}
		DynamicDataSourceContextHolder.setDataSource("dataSource");
		DataSource dataSource = null;
		try {
			Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName("com.zaxxer.hikari.HikariDataSource");
			Tenant tenant = tenantService.findByTenantID(tenantID);
			String connectionUrl = tenant.getConnectionUrl();
			String driverClassName = "net.sf.log4jdbc.DriverSpy";
			String dbUser = tenant.getDbusername();
			String dbPwd = tenant.getDbPwd();
			dataSource = DataSourceBuilder.create().type(dataSourceType).driverClassName(driverClassName).url(connectionUrl).username(dbUser).password(dbPwd).build();

			Map<String, DataSource> customDataSources = DynamicDataSourceRegister.getCustomDataSources();
			customDataSources.put(tenantID, dataSource);

			Map<Object, DataSource> targetDataSources = new HashMap<Object, DataSource>();
			// 添加默认数据源
			targetDataSources.put("dataSource", DynamicDataSourceRegister.getDefaultDataSource());
			// 添加其他数据源
			targetDataSources.putAll(customDataSources);
			DynamicDataSource.setTargetDataSource(targetDataSources);
			DynamicDataSourceContextHolder.dataSourceNames.add(tenantID);
		}catch (Exception e){
			LOGGER.error("创建数据源失败：{}", e);
			return;
		}

	}

}

package com.fable.dynamicDataSource.controller;

import com.fable.dynamicDataSource.Util.SpringContextUtil;
import com.fable.dynamicDataSource.domain.User;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSource;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceContextHolder;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceRegister;
import com.fable.dynamicDataSource.service.TenantService;
import com.fable.dynamicDataSource.service.UserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TenantService tenantService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getUserList(@RequestParam("ds") String tenant, Map<String, Object> model){
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

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam("ds") String tenant){
		// 动态加载数据源
		dynamicRegisterDataSourceBean(tenant);
		return "ok";
	}



	private void dynamicRegisterDataSourceBean(String tenantID){
		if(DynamicDataSourceContextHolder.containsDataSource(tenantID)){
			return;
		}

		DataSource newDs = null;
		try {
			Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName("com.zaxxer.hikari.HikariDataSource");
			DynamicDataSourceContextHolder.setDataSource("dataSource");
			String jdbcUrl = tenantService.findByTenantID(tenantID);
			newDs = DataSourceBuilder.create().type(dataSourceType).driverClassName("").url(jdbcUrl).username("root").password("123456").build();

			Map<String, DataSource> customDataSources = DynamicDataSourceRegister.getCustomDataSources();
			customDataSources.put(tenantID, newDs);

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

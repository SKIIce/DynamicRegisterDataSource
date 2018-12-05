package com.fable.dynamicDataSource.dynamicDataSource;

import com.fable.dynamicDataSource.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
	// 默认的数据源类型
	private static final String DEFAULT_DATASOUCE_TYPE = "com.zaxxer.hikari.HikariDataSource";
	// 默认数据源
	private static DataSource defaultDataSource;
	// 数据源公共属性
	private PropertyValues dataSourcePropertyValues;
	// 其他数据源
	private static Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

	/**
	 * 加载多数据源配置
	 */
	public void setEnvironment(Environment env) {
		initDefaultDataSource(env);
		//initCustomDataSources(env);
	}

	/**
	 * 初始化默认数据源
	 */
	private void initDefaultDataSource(Environment env) {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env, "spring.datasource.");
		Map<String, Object> dataSourceMap = new HashMap<String, Object>(5);
		dataSourceMap.put("type", resolver.getProperty("type"));
		dataSourceMap.put("driver-class-name", resolver.getProperty("driver-class-name"));
		dataSourceMap.put("url", resolver.getProperty("url"));
		dataSourceMap.put("username", resolver.getProperty("username"));
		dataSourceMap.put("password", resolver.getProperty("password"));

		defaultDataSource = buildDataSource(dataSourceMap);
		bindData(defaultDataSource, env);
	}

	/**
	 * 创建DataSource
	 */
	private DataSource buildDataSource(Map<String, Object> dataSourceMap) {
		Object type = dataSourceMap.get("type");
		type = type == null ? DEFAULT_DATASOUCE_TYPE : type;
		try {
			Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName(type.toString());
			String driverClassName = dataSourceMap.get("driver-class-name").toString();
			String url = dataSourceMap.get("url").toString();
			String username = dataSourceMap.get("username").toString();
			String password = dataSourceMap.get("password").toString();
			return DataSourceBuilder.create().type(dataSourceType).driverClassName(driverClassName).url(url)
					.username(username).password(password).build();
		} catch (ClassNotFoundException e) {
			LOGGER.error("创建数据源失败：{}", e);
			return null;
		}
	}

	/**
	 * 为DataSource绑定更多属性
	 */
	private void bindData(DataSource dataSource, Environment env) {
		RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
		dataBinder.setConversionService(new DefaultConversionService());
		dataBinder.setIgnoreNestedProperties(false);
		dataBinder.setIgnoreInvalidFields(false);
		dataBinder.setIgnoreUnknownFields(true);

		if(dataSourcePropertyValues == null){
			Map<String, Object> subProperties = new RelaxedPropertyResolver(env, "spring.datasource")
					.getSubProperties(".");
			Map<String, Object> values = new HashMap<String, Object>(subProperties);
			// 排除已经设置的属性
			values.remove("type");
			values.remove("driver-class-name");
			values.remove("url");
			values.remove("username");
			values.remove("password");
			dataSourcePropertyValues = new MutablePropertyValues(values);
		}

		dataBinder.bind(dataSourcePropertyValues);
	}

	/**
	 * 初始化其他数据源
	 */
	private void initCustomDataSources(Environment env) {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env, "custom.datasource.");
		String dataSourceNames = resolver.getProperty("names");
		for (String dsName : dataSourceNames.split(",")){
			Map<String, Object> properties = resolver.getSubProperties(dsName + ".");
			DataSource dataSource = buildDataSource(properties);
			customDataSources.put(dsName, dataSource);
			bindData(dataSource, env);
		}
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
			BeanDefinitionRegistry registry) {
		Map<String, DataSource> targetDataSources = new HashMap<String, DataSource>();
		// 添加默认数据源
		targetDataSources.put("dataSource", defaultDataSource);
		DynamicDataSourceContextHolder.dataSourceNames.add("dataSource");
		// 添加其他数据源
		targetDataSources.putAll(customDataSources);
		for (String dsName : customDataSources.keySet()){
			DynamicDataSourceContextHolder.dataSourceNames.add(dsName);
		}

		// 创建DynamicDataSource
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DynamicDataSource.class);
		beanDefinition.setSynthetic(true);
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		propertyValues.addPropertyValue("defaultTargetDataSource", defaultDataSource);
		propertyValues.addPropertyValue("targetDataSources", targetDataSources);

		registry.registerBeanDefinition("dataSource", beanDefinition);
	}

	public static DataSource getDefaultDataSource(){
		return  defaultDataSource;
	}

	public static Map<String, DataSource> getCustomDataSources(){
		return customDataSources;
	}


}

package com.fable.dynamicDataSource.dynamicDataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切换数据源Advice
 */
@Aspect
@Order(-10)//保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("@annotation(ds)")
	public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
		String dsName = ds.name();
		if (!DynamicDataSourceContextHolder.containsDataSource(dsName)) {
			logger.error("数据源[{}]不存在，使用默认数据源 > {}", dsName, point.getSignature());
		} else {
			logger.debug("Use DataSource : {} > {}", dsName, point.getSignature());
			DynamicDataSourceContextHolder.setDataSource(dsName);
		}
	}

//	@Before("execution(* com.fable.dynamicDataSource.service.impl.*.*(..))")
//	public void changeDataSource(JoinPoint joinPoint) throws Throwable {
//		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//		Method method = methodSignature.getMethod();
//		TargetDataSource myAnnotation = method.getAnnotation(TargetDataSource.class);
//		if (myAnnotation != null){
//			DynamicDataSourceContextHolder.setDataSource(myAnnotation.name());
//		}
//	}

	@After("@annotation(ds)")
	public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
		logger.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
		DynamicDataSourceContextHolder.restoreDataSource();
	}

}

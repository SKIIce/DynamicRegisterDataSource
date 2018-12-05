package com.fable.dynamicDataSource.dynamicDataSource;

import java.lang.annotation.*;

/**
 * 在方法上使用，指定使用哪个数据源
 */
@Target({ ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
	String name();
}

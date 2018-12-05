package com.fable.dynamicDataSource.dynamicDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class DynamicDataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	public static List<String> dataSourceNames = new ArrayList<String>();

	public static void setDataSource(String dataSource){
		contextHolder.set(dataSource);
	}

	public static String getDataSource(){
		return contextHolder.get();
	}

	/**
	 * 恢复数据源
	 */
	public static void restoreDataSource(){
		contextHolder.remove();
	}

	public static boolean containsDataSource(String dataSourceName){
		return dataSourceNames.contains(dataSourceName);
	}
}

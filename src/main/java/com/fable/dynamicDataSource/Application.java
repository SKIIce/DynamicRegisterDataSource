package com.fable.dynamicDataSource;

import com.fable.dynamicDataSource.Util.SpringContextUtil;
import com.fable.dynamicDataSource.dynamicDataSource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Administrator on 2016/12/1 0001.
 */
//启注解事务管理
@EnableTransactionManagement
@SpringBootApplication
@EnableRedisHttpSession
@Import({ DynamicDataSourceRegister.class})//注册动态多数据源
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }


    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

}

package com.gogo.psy.user.config;


import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.gogo.psy.**.mapper")
public class MybatisConfig {
    @Bean
    public PageInterceptor buildPageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","false");
        properties.setProperty("rowBoundsWithCount","false");
        properties.setProperty("pageSizeZero","false");
        properties.setProperty("reasonable","false");
        properties.setProperty("supportMethodsArguments","false");
        properties.setProperty("autoRuntimeDialect","true");
        properties.setProperty("closeConn","true");
        properties.setProperty("params","pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}

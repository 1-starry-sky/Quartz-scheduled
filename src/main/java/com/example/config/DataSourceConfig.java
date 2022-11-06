//package com.example.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//
//@Component
//public class DataSourceConfig {
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "datasource.primary")
//    public DataSourceProperties primaryDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.scheduler")
//    public DataSourceProperties quartzDataSourceProperties() {
//        return new DataSourceProperties();
//    }
////
//    @Bean(name = "primaryDataSource")
//    public DataSource primaryDataSource() {
//        return primaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }
//
////    @Bean(name = "quartzDataSource")
////    @ConfigurationProperties(prefix = "datasource.scheduler")
////    @QuartzDataSource
////    public DataSource quartzDataSource() {
////        DataSource datasource = quartzDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class)
////                .build();
////        return datasource;
////    }
//
//    /**
//     * 创建 SqlSessionFactory
//     */
//    @Bean(name = "sqlSessionFactory")
//    @Primary
//    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource)
//            throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        // bean.setMapperLocations(new
//        // PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/db1/*.xml"));
//        return bean.getObject();
//    }
//
//
//    @Bean(name = "sqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate primarySqlSessionTemplate(
//            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}

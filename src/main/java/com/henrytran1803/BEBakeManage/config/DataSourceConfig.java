package com.henrytran1803.BEBakeManage.config;

import com.henrytran1803.BEBakeManage.common.util.RoleBasedRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean(name = "adminDataSource")
    public DataSource adminDataSource() {
        System.out.println("admin");
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/bemanagebake")
                .username("root")
                .password("18032002")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean(name = "userDataSource")
    public DataSource userDataSource() {
        System.out.println("user");
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/bemanagebake")
                .username("root")
                .password("18032002")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean
    public DataSource dataSource(
            @Qualifier("adminDataSource") DataSource adminDataSource,
            @Qualifier("userDataSource") DataSource userDataSource) {
        RoleBasedRoutingDataSource routingDataSource = new RoleBasedRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ROLE_ADMIN", adminDataSource);
        dataSourceMap.put("ROLE_USER", userDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(userDataSource);
        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), new HashMap<>(), null)
                .dataSource(dataSource)
                .packages("com.henrytran1803.BEBakeManage.category.entity", // Cập nhật cho entity
                        "com.henrytran1803.BEBakeManage.user.entity",
                        "com.henrytran1803.BEBakeManage.recipe.entity") // Thêm các package entity khác nếu có
                .persistenceUnit("roleBasedPU")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}

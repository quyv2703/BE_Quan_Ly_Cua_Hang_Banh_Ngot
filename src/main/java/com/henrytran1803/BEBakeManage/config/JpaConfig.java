
package com.henrytran1803.BEBakeManage.config;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Configuration
@EnableTransactionManagement
public class JpaConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), new HashMap<>(), null)
                .dataSource(dataSource)
                .packages("com.henrytran1803.BEBakeManage.user.entity",
                        "com.henrytran1803.BEBakeManage.category.entity",
                        "com.henrytran1803.BEBakeManage.recipe.entity",
                        "com.henrytran1803.BEBakeManage.product.entity",
                        "com.henrytran1803.BEBakeManage.promotion.entity",
                        "com.henrytran1803.BEBakeManage.image.entity",
                        "com.henrytran1803.BEBakeManage.export_ingredients.entity",
                        "com.henrytran1803.BEBakeManage.import_ingredients.entity",
                        "com.henrytran1803.BEBakeManage.ingredients.entity",
                        "com.henrytran1803.BEBakeManage.product_batches.entity",
                        "com.henrytran1803.BEBakeManage.supplier.entity",
                        "com.henrytran1803.BEBakeManage.units.entity",
                        "com.henrytran1803.BEBakeManage.quycode.entity",
                        "com.henrytran1803.BEBakeManage.disposed_product.entity",
                        "com.henrytran1803.BEBakeManage.daily_productions.entity",
                        "com.henrytran1803.BEBakeManage.product_history.entity",
                        "com.henrytran1803.BEBakeManage.daily_discount.entity"

                        )
                .persistenceUnit("roleBasedPU")
                .build();
    }
    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}

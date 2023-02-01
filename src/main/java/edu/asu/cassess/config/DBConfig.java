package edu.asu.cassess.config;

import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
//Disabled for now as this annotation creates proxy beans that do not
//function well with session-based objects needed for Angular<->Spring communication
//@EnableTransactionManagement(proxyTargetClass=true)
@EnableTransactionManagement
@ComponentScan({"edu.asu.cassess.dao", "edu.asu.cassess.persist", "edu.asu.cassess.service"})
@EnableJpaRepositories("edu.asu.cassess")
public class DBConfig {

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

/*
        //Connection Parameters for Local Deployment
        dataSource.setUrl("jdbc:mysql://localhost/cassess");
        dataSource.setUsername("root");
        dataSource.setPassword("root123");
*/
        //Connection Parameters for ASU RHEL Server Deployment
        dataSource.setUrl("jdbc:mysql://localhost/cassess");
        dataSource.setUsername("root");
        dataSource.setPassword("root123");
/*
        dataSource.setUrl("jdbc:mysql://cassess.fulton.asu.edu/cassess");
        dataSource.setUsername("cassess");
        dataSource.setPassword("4qHb!9d3");
*/
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(getDataSource());
        emf.setPackagesToScan(new String[]{"edu.asu.cassess.*"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(additionalProperties());
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public JPAAnnotationMetadataUtil metadataUtil() {
        JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
        return metadataUtil;
    }

    @Bean
    public JPASearchProcessor searchProcessor(JPAAnnotationMetadataUtil metadataUtil) {
        JPASearchProcessor searchProcessor = new JPASearchProcessor(metadataUtil);
        return searchProcessor;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public DozerBeanMapper getMapper() {
        return new DozerBeanMapper();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        //validate forces schema validation on program execution; use create to recreate schema and all tables
        //based on entities; create will overwrite any existing data
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }

} 

package com.xajiusuo.jpa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by sht on 2018/1/8.
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("{spring.datasource.connectionProperties}")
    private String connectionProperties;


    @Value("${spring.datasource.logSlowSql}")
    private String logSlowSql;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", username);
        reg.addInitParameter("loginPassword", password);
        reg.addInitParameter("logSlowSql", logSlowSql);
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }


    @Bean(name = "primaryDataSource", initMethod = "init", destroyMethod = "close")
    @Qualifier("primaryDataSource")
    @Primary
    public DataSource dataSource() {
        log.info("start init dataSource!!!");
        return getDruidDataSource(driverClassName,dbUrl,username,password, initialSize);
    }

    @Bean(name = "primaryJdbcTemplate")
    @Qualifier("primaryJdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(@Qualifier("primaryDataSource")DataSource datasource){
        return new JdbcTemplate(datasource);
    }

//    public DataSource secondDataSource() {
//        log.info("start init dataSource!!!");
//        return getDruidDataSource(driverClassName2,dbUrl2,username2,password2);
//    }
//
//    public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDataSource")DataSource datasource){
//        log.error("获取第二个连接jdbc");
//        return new JdbcTemplate(datasource);
//    }

    public DruidDataSource getDruidDataSource(String Clazz, String url, String user, String pwd, int initialSize){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(Clazz);
        datasource.setUrl(url);
        datasource.setUsername(user);
        datasource.setPassword(pwd);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(this.minIdle);
        datasource.setMaxActive(this.maxActive);
        datasource.setMaxWait((long)this.maxWait);
        datasource.setTimeBetweenEvictionRunsMillis((long)this.timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis((long)this.minEvictableIdleTimeMillis);
        datasource.setValidationQuery(this.validationQuery);
        datasource.setTestWhileIdle(this.testWhileIdle);
        datasource.setTestOnBorrow(this.testOnBorrow);
        datasource.setTestOnReturn(this.testOnReturn);

        try {
            datasource.setFilters(this.filters);
        } catch (SQLException var3) {
            log.error("druid configuration initialization filter : {0}", var3);
        }

        datasource.setConnectionProperties(this.connectionProperties);

        return datasource;
    }

}

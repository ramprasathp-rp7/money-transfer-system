package com.banking.moneytransfer.config;

import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "snowflake", name = "enabled", havingValue = "true")
public class SnowflakeConfig {

    @Value("${snowflake.url}")
    private String url;

    @Value("${snowflake.user}")
    private String user;

    @Value("${snowflake.password}")
    private String password;

    @Value("${snowflake.database}")
    private String database;

    @Value("${snowflake.schema}")
    private String schema;

    @Value("${snowflake.warehouse}")
    private String warehouse;


    @Bean(name = "snowflakeDataSource")
    public DataSource snowflakeDataSource() {

        SnowflakeBasicDataSource ds = new SnowflakeBasicDataSource();
        ds.setUrl(this.url);
        ds.setUser(this.user);
        ds.setPassword(this.password);

        ds.setDatabaseName(this.database);
        ds.setSchema(this.schema);
        ds.setWarehouse(this.warehouse);

        return ds;
    }
}
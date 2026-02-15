package com.banking.moneytransfer.config;

import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "snowflake", name = "enabled", havingValue = "true", matchIfMissing = false)
public class SnowflakeConfig {
    @Bean(name = "snowflakeDataSource")
    public DataSource snowflakeDataSource() {

        SnowflakeBasicDataSource ds = new SnowflakeBasicDataSource();
        ds.setUrl("jdbc:snowflake://srztpza-sq01845.snowflakecomputing.com");
        ds.setUser("RAAHULKUMXR");
        ds.setPassword("Raahul16022005");
        ds.setWarehouse("COMPUTE_WH");
        ds.setDatabaseName("MONEY_TRANSFER_DW");
        ds.setSchema("ANALYTICS");

        return ds;
    }
}
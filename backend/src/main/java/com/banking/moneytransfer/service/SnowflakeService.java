package com.banking.moneytransfer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
@Service
@Slf4j
public class SnowflakeService {

    @Autowired
    @Qualifier("snowflakeDataSource")
    private DataSource snowflakeDataSource;

    public void uploadAndCopy(File file) throws Exception {
        log.info("Loading CSV file into snowflake");
        try (Connection conn = snowflakeDataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // upload CSV to stage
            stmt.execute("PUT file://" + file.getAbsolutePath() + " @TRANSFER_STAGE");

            // bulk load: map CSV columns to warehouse fact table
            stmt.execute("""
                COPY INTO FACT_TRANSACTIONS (ACCOUNT_FROM_KEY,ACCOUNT_TO_KEY,AMOUNT,STATUS,DATE_KEY)
                FROM @TRANSFER_STAGE
                FILE_FORMAT = CSV_FORMAT
                ON_ERROR='CONTINUE'
            """);
        }
    }
}
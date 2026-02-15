package com.banking.moneytransfer.service;

import com.banking.moneytransfer.model.entity.TransactionLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class CsvService {

    public File writeCsv(List<TransactionLog> data) throws Exception {
        log.info("Writing Transaction Log into CSV file");
        File file = new File("transactions.csv");

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("ACCOUNT_FROM_KEY,ACCOUNT_TO_KEY,AMOUNT,STATUS,DATE_KEY");

            for (TransactionLog t : data) {
                String dateKey = java.time.LocalDate.from(t.getCreatedOn()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                pw.println(t.getFromAccount().getId() + "," +
                           t.getToAccount().getId() + "," +
                           t.getAmount() + "," +
                           t.getStatus() + "," +
                           dateKey);
            }
        }
        return file;
    }
}
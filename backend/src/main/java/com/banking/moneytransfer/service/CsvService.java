package com.banking.moneytransfer.service;
import com.banking.moneytransfer.model.entity.TransactionLog;
import com.banking.moneytransfer.repository.TransactionLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

@Service
@Slf4j
public class CsvService {

    public File writeCsv(List<TransactionLog> data) throws Exception {
        log.info("Creating Transaction Log in csv format");
        File file = new File("transactions.csv");

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("ACCOUNT_FROM_KEY,ACCOUNT_TO_KEY,AMOUNT,STATUS,DATE_KEY");

            for (TransactionLog t : data) {
                // Convert timestamp to DATE_KEY format (YYYYMMDD)
                String dateKey = java.time.LocalDate.from(t.getCreatedOn()).format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                pw.println(
                                t.getFromAccount().getId() + "," +
                                t.getToAccount().getId() + "," +
                                t.getAmount() + "," +
                                t.getStatus() + "," +
                                dateKey
                );
            }
        }

        return file;
    }
}
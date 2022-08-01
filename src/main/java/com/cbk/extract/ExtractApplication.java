package com.cbk.extract;

import com.cbk.extract.entity.Operation;
import com.cbk.extract.entity.Transfer;
import com.cbk.extract.service.OperationService;
import com.cbk.extract.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class ExtractApplication implements CommandLineRunner{

    @Autowired
    OperationService operationService;
    @Autowired
    TransferService transferService;

    public static void main(String[] args) {
        SpringApplication.run(ExtractApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        List<Operation> operationList = operationService.read();
        operationService.save(operationList);
        List<Transfer> transfersList = transferService.parse();
        transferService.save(transfersList);

        List<Operation> operations = operationService.findAll();
        List<Transfer> transfers = transferService.findAll();
        Map<String, Boolean> map = new HashMap<>();

        for (Operation operation : operations) {
            map.put(operation.getOperation(), false);
        }


        log.info("Идет сравнение платежей...");
        for (Transfer transfer : transfers) {
            if (map.containsKey(transfer.getPlatformReferenceNumber())) {
                map.replace(transfer.getPlatformReferenceNumber(), true);
            }
        }

        System.out.println(map.values());
    }

}

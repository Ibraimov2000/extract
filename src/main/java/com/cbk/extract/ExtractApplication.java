package com.cbk.extract;

import com.cbk.extract.entity.Operation;
import com.cbk.extract.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ExtractApplication implements CommandLineRunner{

    @Autowired
    OperationService operationService;

    public static void main(String[] args) {
        SpringApplication.run(ExtractApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        List<Operation> list = operationService.read();
        operationService.save(list);
    }

}

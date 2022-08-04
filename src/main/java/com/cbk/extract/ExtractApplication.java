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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class ExtractApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtractApplication.class, args);
    }

}

package com.cbk.extract.controller;

import com.cbk.extract.entity.Operation;
import com.cbk.extract.entity.Transfer;
import com.cbk.extract.service.OperationService;
import com.cbk.extract.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
@Slf4j
public class TransferController {

    private final TransferService transferService;
    private final OperationService operationService;

    @Autowired
    public TransferController(TransferService transferService, OperationService operationService) {
        this.transferService = transferService;
        this.operationService = operationService;
    }

    @RequestMapping("/save")
    public ResponseEntity<List<Transfer>> saveTransfer(@RequestParam MultipartFile[] files) throws IOException {
        return ResponseEntity.ok().body(transferService.saveAll(transferService.parse(files)));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok().body(transferService.findAll());
    }

    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Boolean>> getCompare() {


        List<Operation> operations = operationService.findAll();
        List<Transfer> transfers = transferService.findAll();
        Map<String, Boolean> map = new HashMap<>();

        for (Transfer transfer : transfers) {
            map.put(transfer.getPlatformReferenceNumber(), false);
        }


        log.info("Идет сравнение платежей...");
        for (Operation operation : operations) {
            if (map.containsKey(operation.getOperation())) {
                map.replace(operation.getOperation(), true);
            }
        }

        return ResponseEntity.ok().body(map);
    }
}

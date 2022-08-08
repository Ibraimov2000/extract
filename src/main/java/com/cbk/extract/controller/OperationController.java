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
@RequestMapping("/api/operation")
@Slf4j
public class OperationController {

    private final OperationService operationService;
    private final TransferService transferService;

    @Autowired
    public OperationController(OperationService operationService, TransferService transferService) {
        this.operationService = operationService;
        this.transferService = transferService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<List<Operation>> saveOperation(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(operationService.saveAll(operationService.parse(file)));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Operation>> getAllOperations() {
        return ResponseEntity.ok().body(operationService.findAll());
    }

    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Map<String, Boolean>>> getCompare() {

        List<Operation> operations = operationService.findAll();
        List<Transfer> transfers = transferService.findAll();
        Map<String, Boolean> map = new HashMap<>();
        Map<String, Boolean> map1 = new HashMap<>();
        Map<String, Map<String, Boolean>> difference = new HashMap<>();

        for (Operation operation : operations) {
            map.put(operation.getOperation(), false);
        }

        log.info("Идет сравнение по transfer...");
        for (Transfer transfer : transfers) {
            if (map.containsKey(transfer.getPlatformReferenceNumber())) {
                map.remove(transfer.getPlatformReferenceNumber());
            }
        }

        for (Transfer transfer : transfers) {
            map1.put(transfer.getPlatformReferenceNumber(), false);
        }

        log.info("Идет сравнение по operation...");
        for (Operation operation : operations) {
            if (map1.containsKey(operation.getOperation())) {
                map1.remove(operation.getOperation());
            }
        }

        difference.put("transfer", map);
        difference.put("operation", map1);

        return ResponseEntity.ok().body(difference);
    }
}

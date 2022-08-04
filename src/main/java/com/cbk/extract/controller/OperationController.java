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
    public ResponseEntity<Map<String, Boolean>> getCompare() {


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

        return ResponseEntity.ok().body(map);
    }
}

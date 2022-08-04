package com.cbk.extract.service;

import com.cbk.extract.entity.Operation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OperationService {
    List<Operation> parse(MultipartFile file) throws IOException;
    List<Operation> saveAll(List<Operation> list);
    void deleteById(Long id);
    Operation findById(Long id);
    List<Operation> findAll();
}

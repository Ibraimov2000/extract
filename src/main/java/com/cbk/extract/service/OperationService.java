package com.cbk.extract.service;

import com.cbk.extract.entity.Operation;

import java.io.IOException;
import java.util.List;

public interface OperationService {
    List<Operation> read() throws IOException;
    void save(List<Operation> list);
    void deleteById(Long id);
    Operation findById(Long id);
    List<Operation> findAll();
}

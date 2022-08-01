package com.cbk.extract.service;

import com.cbk.extract.entity.Transfer;

import java.io.IOException;
import java.util.List;

public interface TransferService {

    Transfer findById(Long id);
    List<Transfer> findAll();
    void save(List<Transfer> list);
    void deleteById(Long id);
    List<Transfer> parse() throws IOException;
}

package com.cbk.extract.service;

import com.cbk.extract.entity.Transfer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TransferService {

    Transfer findById(Long id);
    List<Transfer> findAll();
    List<Transfer> saveAll(List<Transfer> list);
    void deleteById(Long id);
    List<Transfer> parse(MultipartFile[] multipartFiles) throws IOException;
}

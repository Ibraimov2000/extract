package com.cbk.extract.service;

import com.cbk.extract.entity.Operation;
import com.cbk.extract.entity.ReceivingAmount;

import java.util.List;

public interface ReceivingAmountService {
    ReceivingAmount save(ReceivingAmount receivingAmount);
    void deleteById(Long id);
    ReceivingAmount findById(Long id);
    List<ReceivingAmount> findAll();
}

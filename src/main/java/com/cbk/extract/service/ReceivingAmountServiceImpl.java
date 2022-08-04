package com.cbk.extract.service;

import com.cbk.extract.entity.ReceivingAmount;
import com.cbk.extract.repository.ReceivingAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivingAmountServiceImpl implements ReceivingAmountService {

    private final ReceivingAmountRepository receivingAmountRepository;

    @Autowired
    public ReceivingAmountServiceImpl(ReceivingAmountRepository receivingAmountRepository) {
        this.receivingAmountRepository = receivingAmountRepository;
    }

    @Override
    public ReceivingAmount save(ReceivingAmount receivingAmount) {
        return receivingAmountRepository.save(receivingAmount);
    }

    @Override
    public void deleteById(Long id) {
        receivingAmountRepository.deleteById(id);
    }

    @Override
    public ReceivingAmount findById(Long id) {
        return receivingAmountRepository.findById(id).get();
    }

    @Override
    public List<ReceivingAmount> findAll() {
        return receivingAmountRepository.findAll();
    }
}

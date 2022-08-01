package com.cbk.extract.service;


import com.cbk.extract.entity.Transfer;
import com.cbk.extract.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public Transfer findById(Long id) {
        return transferRepository.findById(id).get();
    }

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    @Transactional
    public void save(List<Transfer> list) {

        log.info("СОХРАНЕНИЕ ДАННЫХ В БД...");
        for (Transfer transfer : list) {
            transferRepository.save(transfer);
        }
    }

    @Override
    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }

    @Override
    public List<Transfer> parse() throws IOException {

        List<Transfer> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try {

            final File folder = new File("D:\\work\\json_datas\\");

            log.info("ЧТЕНИЕ ДАННЫХ ИЗ JSON ФАЙЛОВ...");
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {

                String path = fileEntry.getName();

                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(folder +"\\" + path));
                JSONArray jsonArray = (JSONArray) jsonObject.get("transfers");
                for (var value : jsonArray) {
                    JSONObject jsonObject1 = (JSONObject) value;
                    jsonObject1 = (JSONObject) jsonObject1.get("transfer");
                    String platformReferenceNumber = (String) jsonObject1.get("platformReferenceNumber");
                    Transfer transfer = new Transfer();
                    transfer.setPlatformReferenceNumber(platformReferenceNumber);
                    list.add(transfer);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }
}

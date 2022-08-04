package com.cbk.extract.service;


import com.cbk.extract.entity.ReceivingAmount;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final ReceivingAmountService receivingAmountService;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, ReceivingAmountService receivingAmountService) {
        this.transferRepository = transferRepository;
        this.receivingAmountService = receivingAmountService;
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
    public List<Transfer> saveAll(List<Transfer> list) {

        log.info("СОХРАНЕНИЕ ДАННЫХ В БД...");
        return transferRepository.saveAll(list);
    }

    @Override
    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }

    @Override
    public List<Transfer> parse(MultipartFile[] multipartFiles) throws IOException {

        List<Transfer> list = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try {

            //final File folder = new File("D:\\work\\json_datas\\");

            log.info("ЧТЕНИЕ ДАННЫХ ИЗ JSON ФАЙЛОВ...");
            for (final MultipartFile file : Objects.requireNonNull(multipartFiles)) {

                File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
                convFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(convFile);
                fileOutputStream.write(file.getBytes());
                fileOutputStream.close();

                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader((convFile)));
                JSONArray jsonArray = (JSONArray) jsonObject.get("transfers");

                for (var value : jsonArray) {
                    JSONObject jsonObject1 = (JSONObject) value;
                    jsonObject1 = (JSONObject) jsonObject1.get("transfer");
                    String date = (String) jsonObject1.get("transferDate");
                    String platformReferenceNumber = (String) jsonObject1.get("platformReferenceNumber");
                    JSONObject jsonObject2 = (JSONObject) jsonObject1.get("receivingAmount");
                    ReceivingAmount receivingAmount = new ReceivingAmount();
                    receivingAmount.setAmount((Double) jsonObject2.get("amount"));
                    receivingAmount.setCurrency((String) jsonObject2.get("currency"));
                    receivingAmount = receivingAmountService.save(receivingAmount);
                    Transfer transfer = new Transfer();
                    transfer.setDate(date);
                    transfer.setPlatformReferenceNumber(platformReferenceNumber);
                    transfer.setReceivingAmount(receivingAmount);
                    list.add(transfer);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }
}

package com.cbk.extract.service;

import com.cbk.extract.entity.Operation;
import com.cbk.extract.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository) {

        this.operationRepository = operationRepository;
    }


    @Override
    public List<Operation> parse(MultipartFile file) throws FileNotFoundException {

        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        FileInputStream fileInputStream = new FileInputStream(convFile);
        List<Operation> list = new ArrayList<>();

        log.info("ЧТЕНИЕ ДАННЫХ ИЗ EXCEL ФАЙЛА...");
        try {
            Workbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet workSheet = (HSSFSheet) workbook.getSheet("radA45FF");

            for (int i = 12; i < workSheet.getLastRowNum() - 10; i++) {
                Row row = workSheet.getRow(i);

                String date = row.getCell(0).toString();
                date = date.replace("\u00a0", "");
                String recipient = row.getCell(1).getStringCellValue();
                recipient = recipient.replace("\u00a0", "");
                String strSum = "";

                if (!row.getCell(2).getStringCellValue().isEmpty()) {
                    strSum = row.getCell(2).getStringCellValue().toString();
                }

                double sum = Double.parseDouble(strSum.replaceAll(",", "").replaceAll("\\u00a0", ""));
                String[] operations = row.getCell(4).getStringCellValue().split("");
                boolean flag = false;
                StringBuilder op = new StringBuilder();

                for (String ch : operations) {

                    if (ch.equals(".")) {
                        flag = false;
                    }
                    if (flag) {
                        op.append(ch);
                    }
                    if (ch.equals("_")) {
                        flag = true;
                    }

                }

                Operation operation = new Operation();
                operation.setOperation(op.toString());
                operation.setDate(date);
                operation.setRecipient(recipient);
                operation.setSum(sum);

                list.add(operation);
            }

        } catch (NumberFormatException | IOException numberFormatException) {
            numberFormatException.printStackTrace();
        }
        return list;
    }

    @Transactional
    @Override
    public List<Operation> saveAll(List<Operation> list) {

        log.info("СОХРАНЕНИЕ ДАННЫХ В БД...");
        return operationRepository.saveAll(list);
    }

    @Override
    public void deleteById(Long id) {
        operationRepository.findById(id);
    }

    @Override
    public Operation findById(Long id) {
        return operationRepository.findById(id).get();
    }

    @Override
    public List<Operation> findAll() {
        return operationRepository.findAll();
    }
}

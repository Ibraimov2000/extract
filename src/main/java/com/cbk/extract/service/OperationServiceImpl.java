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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository) {

        this.operationRepository = operationRepository;
    }


    @Override
    public List<Operation> parse(MultipartFile file) throws IOException {


        InputStream fileInputStream = file.getInputStream();
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

                String op = getOperationId(row.getCell(4).getStringCellValue());

                Operation operation = new Operation();
                operation.setOperation(op.replaceAll("\\.","").replaceAll("SBPIN2_", ""));
                operation.setDate(date);
                operation.setRecipient(recipient);
                operation.setSum(sum);

                if (!op.isEmpty() | !op.equals("")) {
                    list.add(operation);
                }

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

    private String getOperationId(String value){
        Pattern p = Pattern.compile("SBPIN2_[^)]*\\.");
        Matcher matcher = p.matcher(value);
        return  matcher.find() ? matcher.group() : "";
    }
}

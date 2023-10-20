package com.msil.evaluation.service.impl;

import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.entity.Symbols;
import com.msil.evaluation.repository.SymbolRepository;
import com.msil.evaluation.service.QuoteService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Log4j2
@Transactional
public class QuoteServiceImpl implements QuoteService {
    @Autowired
    SymbolRepository symbolsRepository;

    /* * Method to import symbols from an Excel file, read the data, and save it to the database.
     */
    public void importSymbolsFromExcel(MultipartFile file) throws IOException {
        log.info(StringConstants.Quote + "importSymbolsFromExcel");

        // Read symbols from the Excel file and save them to the database.
        List<Symbols> symbolsList = readSymbolsFromExcel(file.getInputStream());
        symbolsRepository.saveAll(symbolsList);
    }

    /**Method to read symbols from an Excel file and convert them into a list of Symbols object*/
    public List<Symbols> readSymbolsFromExcel(InputStream excelFile) throws IOException {
        // Log a message indicating the method being executed.
        log.info(StringConstants.Quote + "readSymbolsFromExcel");

        List<Symbols> symbolsList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }
            Symbols symbol = new Symbols();
            symbol.setSymbol(row.getCell(0).getStringCellValue());
            symbol.setSymbolName(row.getCell(1).getStringCellValue());
            symbol.setIndexName(row.getCell(2).getStringCellValue());
            symbol.setCompanyName(row.getCell(3).getStringCellValue());
            symbol.setIndustry(row.getCell(4).getStringCellValue());
            symbol.setSeries(row.getCell(5).getStringCellValue());
            symbol.setIsinCode(row.getCell(6).getStringCellValue());
            symbol.setExchange(row.getCell(7).getStringCellValue());

            Cell createdAtCell = row.getCell(8);
            if (createdAtCell.getCellType() == CellType.NUMERIC) {
                symbol.setCreatedAt(createdAtCell.getLocalDateTimeCellValue());
            } else {
                // Handle the case where the cell contains a non-numeric value
                log.warn("Non-numeric value found in 'CreatedAt' cell at row " + row.getRowNum());
                // You can log an error or take appropriate action here.
            }

            Cell updatedAtCell = row.getCell(9);
            if (updatedAtCell.getCellType() == CellType.NUMERIC) {
                symbol.setUpdatedAt(updatedAtCell.getLocalDateTimeCellValue());
            } else {
                // Handle the case where the cell contains a non-numeric value
                log.warn("Non-numeric value found in 'UpdatedAt' cell at row " + row.getRowNum());
                // You can log an error or take appropriate action here.
            }

            symbol.setScripCode(row.getCell(10).getStringCellValue());
            symbolsList.add(symbol);
        }
        workbook.close();
        return symbolsList;
    }

    /**Method to retrieve all symbols from the database.*/
    public List<Symbols> getAllSymbols() {
        log.info(StringConstants.Quote + "getAllSymbols");
        return symbolsRepository.findAll();
    }
}

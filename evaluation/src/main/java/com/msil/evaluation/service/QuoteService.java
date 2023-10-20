package com.msil.evaluation.service;

import com.msil.evaluation.dto.TradeRequest;
import com.msil.evaluation.entity.Symbols;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
public interface QuoteService {
    void importSymbolsFromExcel(MultipartFile file) throws IOException;
    List<Symbols> readSymbolsFromExcel(InputStream excelFile) throws IOException;
    List<Symbols> getAllSymbols();

}

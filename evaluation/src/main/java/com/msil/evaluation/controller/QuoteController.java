package com.msil.evaluation.controller;
import com.msil.evaluation.constants.ErrorConstants;
import com.msil.evaluation.constants.PathConstants;
import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.ApiResponse;
import com.msil.evaluation.entity.Symbols;
import com.msil.evaluation.service.QuoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(PathConstants.Quote)
@Log4j2
public class QuoteController {
    @Autowired
    QuoteService quoteService;

    @PostMapping(PathConstants.Import_Symbols)
    public ApiResponse<String> importSymbols(@RequestParam("file") MultipartFile file) throws IOException {
        log.info(StringConstants.Import_Symbol_Request);
            quoteService.importSymbolsFromExcel(file);
            return new ApiResponse<>((StringConstants.Success),StringConstants.Symbols_Import_Successfully, HttpStatus.OK);
    }

    @GetMapping(PathConstants.All_Symbols)
    public ApiResponse<List<Symbols>> getAllSymbols() {
        log.info(StringConstants.All_Symbols_Request);
        return new ApiResponse<>((StringConstants.LIST_SYMBOLS_SUCCESS) ,quoteService.getAllSymbols(),HttpStatus.OK);
    }

}

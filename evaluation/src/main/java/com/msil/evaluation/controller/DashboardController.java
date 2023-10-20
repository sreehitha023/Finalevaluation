package com.msil.evaluation.controller;
import com.msil.evaluation.constants.PathConstants;
import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.*;
import com.msil.evaluation.service.DashboardService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Log4j2
@RequestMapping(PathConstants.Dashboard)
@RestController
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    @PostMapping(PathConstants.Add_Group)
    public ResponseEntity<StringResponse<String>> addWatchlistGroup(@RequestBody TradeRequest tradeRequest) {
        log.info(StringConstants.ADD_WATCHLIST_GROUP_SUCCESS);
        return ResponseEntity.ok(new StringResponse<>(dashboardService.addWatchlistGroup(tradeRequest)));
    }

    @PostMapping(PathConstants.Add_Symbol_To_Group)
    public ResponseEntity<ApiResponse<String>> addSymbolToWatchListGroups(@RequestBody AddSymbol addSymbol) {
        log.info(StringConstants.ADD_SYMBOL_TO_WATCHLIST_GROUPS_SUCCESS);
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.addSymbolToWatchlistGroups(addSymbol), "Symbol Added"));
    }

    @GetMapping(PathConstants.Get_Groups)
    public ResponseEntity<ApiResponse<List<WatchListGroupDto>>> getWatchListGroups() {
        log.info(StringConstants.GET_WATCHLIST_GROUPS_SUCCESS);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.Watchlist_Retrieval_Success), dashboardService.getWatchlistGroups()));
    }

    @GetMapping(PathConstants.Get_Watchlist_Symbols)
    public ResponseEntity<ApiResponse<List<String>>> getSymbolsFromWatchListGroups(@RequestBody TradeRequest tradeRequest) {
        log.info(StringConstants.GET_SYMBOLS_FROM_WATCHLIST_GROUPS_SUCCESS);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.Symbol_Retrieval_Success), dashboardService.getSymbolsFromWatchlistGroups(tradeRequest)));

    }

    @PostMapping(PathConstants.Add_Order)
    public ResponseEntity<StringResponse<String>> addOrder(@Valid @RequestBody TradeRequest tradeRequest) {
        log.info(StringConstants.Add_Order_Success);
        return ResponseEntity.ok(new StringResponse<>(dashboardService.editOrder(tradeRequest)));
    }
    @GetMapping(PathConstants.Get_Portfolio)
    public ResponseEntity<ApiResponse<List<PortfolioItemDto>>> getPortfolio() {
        log.info(StringConstants.Portfolio_Retrieval_Success);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.Portfolio_Retrieval_Success), dashboardService.getPortfolio()));
    }

    @GetMapping(PathConstants.Get_TradeHistory)
    public ResponseEntity<ApiResponse<List<TradeHistoryDto>>> getTradeHistory() {
        log.info(StringConstants.TradeHistory_Retrieval_Success);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.TradeHistory_Retrieval_Success), dashboardService.getTradeHistory()));
    }


}

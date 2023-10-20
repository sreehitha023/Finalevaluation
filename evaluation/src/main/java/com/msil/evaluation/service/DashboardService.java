package com.msil.evaluation.service;

import com.msil.evaluation.dto.*;

import java.util.List;

public interface DashboardService {
    String addWatchlistGroup(TradeRequest tradeRequest);

    List<WatchListGroupDto> getWatchlistGroups();

    String addSymbolToWatchlistGroups(AddSymbol addSymbol);

    List<String> getSymbolsFromWatchlistGroups(TradeRequest tradeRequest);

     void executeOrders();
    String editOrder(TradeRequest tradeRequest);
     List<PortfolioItemDto> getPortfolio();

     List<TradeHistoryDto> getTradeHistory();
}

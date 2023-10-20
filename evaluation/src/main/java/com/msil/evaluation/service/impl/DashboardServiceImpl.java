package com.msil.evaluation.service.impl;

import com.msil.evaluation.constants.ErrorConstants;
import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.*;
import com.msil.evaluation.entity.*;
import com.msil.evaluation.exceptionHandler.ArgumentConstraintViolation;
import com.msil.evaluation.exceptionHandler.CustomException;
import com.msil.evaluation.exceptionHandler.DuplicateEntryException;
import com.msil.evaluation.exceptionHandler.UserNotFoundException;
import com.msil.evaluation.filter.JwtAuthFilter;
import com.msil.evaluation.repository.*;
import com.msil.evaluation.service.DashboardService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService
{
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    SymbolRepository symbolRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WatchListRepository watchListRepository;
    @Autowired
    TradeRepository tradeRepository;

    /* *Retrieves current user info*/
    public UserDetail getCurrentUserDetails() {
        // Gets the username from the JwtAuthFilter class, which is stored in the userNameMatches.
        String userName = JwtAuthFilter.userNameMatches;

        // Search in the db for a user with a matching username or email.
        Optional<UserDetail> existingUser = userDetailsRepository.findByUserNameOrEmail(userName, userName);

        // If a user is found, return the user details. Else,throw a UserNotFoundException.
        return existingUser.orElseThrow(() -> new UserNotFoundException(ErrorConstants.User_NotFound));
    }


    /* * Adds a new watchlist group for the current user.*/
    public String addWatchlistGroup(TradeRequest tradeRequest) {
        log.info(StringConstants.DashBoard + "addWatchlistGroup");

        // Gets the group name from the tradeRequest
        String groupName = tradeRequest.getGroupName();

        // Check if the groupName is not null and not empty
        if (groupName != null && !groupName.isEmpty()) {
            log.info("service group name " + groupName);

            // Get the current user's details
            UserDetail userDetail = getCurrentUserDetails();

            // Create a new WatchList object and set its user and group name.
            WatchList watchList = new WatchList();
            watchList.setUser(userDetail);
            watchList.setGroupName(groupName);

            // Save the watchList to the repository and retrieve its ID
            watchList = watchListRepository.save(watchList);
            Long watchListId = watchList.getId();

            return StringConstants.WatchList_Added + StringConstants.WatchList_Id + " " + watchListId + " by " + userDetail.getUserName();
        } else {
            // If the group name is null or empty, throw a CustomException with an appropriate error message.
            throw new CustomException(ErrorConstants.Group_Id_Null);
        }
    }

    /* * Retrieves the watchlist groups for the current user.*/
    public List<WatchListGroupDto> getWatchlistGroups() {
        log.info(StringConstants.DashBoard + "getWatchListGroups");

        // Gets current user's details
        UserDetail userDetail = getCurrentUserDetails();

        // Convert the user's watchlists to a list of WatchListGroupDto objects
        List<WatchListGroupDto> watchlistGroupDtos = convertToWatchlistGroupDtos(userDetail.getWatchlists());
        return watchlistGroupDtos;
    }


    /* * Converts a list of Watchlist entities to list of WatchListGroupDto objects.*/
    private List<WatchListGroupDto> convertToWatchlistGroupDtos(List<WatchList> watchLists) {
        return watchLists
                .stream()  // Create a stream of WatchList objects
                .map(this::convertToWatchListGroupDto)  // Map each WatchList to a WatchListGroupDto using the conversion method
                .collect(Collectors.toList());  // Collects the mapped objects into a list and return it
    }


    /* * Converts a Watchlist entity to a WatchListGroupDto object.*/
    private WatchListGroupDto convertToWatchListGroupDto(WatchList watchList) {
        // Create a new WatchListGroupDto object to store the converted data
        WatchListGroupDto groupDTO = new WatchListGroupDto();

        // Set the ID,groupName of the WatchListGroupDto using the ID,groupName from the WatchList entity
        groupDTO.setId(watchList.getId());
        groupDTO.setGroupName(watchList.getGroupName());

        // Get the list of symbols from the WatchList entity
        List<String> symbols = watchList.getSymbols();

        // Set the symbols in the WatchListGroupDto. If the list is not null, create a new ArrayList with the same elements
        // If it's null, set an empty list
        groupDTO.setSymbols(symbols != null ? new ArrayList<>(symbols) : Collections.emptyList());
        return groupDTO;
    }

    /* * Adds a symbol to a specific watchlist group.*/
    public String addSymbolToWatchlistGroups(AddSymbol addSymbol) {
        log.info(StringConstants.DashBoard + "addSymbolToWatchListGroups");

        // Get the symbol and group ID from the addSymbol
        String symbol = addSymbol.getSymbol();
        Long groupId = addSymbol.getGroupId();

        // Check if a group ID is provided.
        if (groupId != null) {
            UserDetail userDetail = getCurrentUserDetails();

            // Check if the watchlist group exists and retrieve it
            WatchList watchlist = checkWatchListGroupExists(userDetail, groupId);

            // Get the name of the watchlist
            String watchListName = watchlist.getGroupName();
            log.info(StringConstants.WatchList + watchListName);
            // Get the list of symbols from the watchlist.
            List<String> symbols = watchlist.getSymbols();

            // Check if the symbol already exists in the symbol repository and is not already in the watchlist.
            if (symbolRepository.existsBySymbol(symbol) && !symbols.contains(symbol))
            {
                symbols.add(symbol);
                return symbol + StringConstants.Symbol_Added + watchListName;
            } else if (!symbolRepository.existsBySymbol(symbol)) {
                // If the symbol does not exist , throw an exception.
                throw new CustomException(ErrorConstants.Symbol_Not_Found);
            } else {
                // If the symbol already exists , throw an exception.
                throw new DuplicateEntryException(ErrorConstants.Symbol_Already_Exists);
            }
        } else {
            // If no group ID is provided, throw an exception.
            throw new ArgumentConstraintViolation(ErrorConstants.Group_Id_Null);
        }
    }

    /* *Checks if a specific watchlist group exists for the user.*/
    private WatchList checkWatchListGroupExists(UserDetail userDetail, Long groupId) {
        // gets the list of watchlist groups of particular user.
        List<WatchList> watchlists = userDetail.getWatchlists();

        // stream is used to filter the watchlist groups and find the one with matching ID if matched return watchlist group
        WatchList watchlist = watchlists.stream()
                .filter(group -> Objects.equals(group.getId(), groupId))
                .findFirst()
                .orElseThrow(() -> new DuplicateEntryException(ErrorConstants.Group_Id_Not_Found + " " + userDetail.getUserName()));
        return watchlist;
    }

    /* * Retrieves symbols from a specific watchlist group.*/
    public List<String> getSymbolsFromWatchlistGroups(TradeRequest tradeRequest) {
        log.info(StringConstants.DashBoard + "getSymbolsFromWatchListGroups");
        UserDetail userDetail = getCurrentUserDetails();

        // Get the ID from tradeRequest
        Long groupId = tradeRequest.getGroupId();

        // Check if the watchlist group with ID exists for the user.
        WatchList watchList = checkWatchListGroupExists(userDetail, groupId);

        // Get the list of symbols associated with the watchlist group.
        List<String> symbols = watchList.getSymbols();

        // If symbols exist, return the list else, return an empty list.
        return symbols != null ? symbols : Collections.emptyList();
    }


    //Edit an order for a specific stock symbol
    public String editOrder(TradeRequest tradeRequest) {
        log.info(StringConstants.DashBoard + "editOrder");
        UserDetail currentUser = getCurrentUserDetails();

        // get the order operation from tradeRequest
        String orderOperation = tradeRequest.getOrderOperation();

        if ("addOrder".equals(orderOperation)) {
            Order order = tradeRequest.getOrder();
            String symbol = order.getStockSymbol();
            // Check if the symbol exists
            if (symbolRepository.existsBySymbol(symbol)) {
                // Set user-related order details.
                order.setUserOrdersId(currentUser);
                order.setStatus(StringConstants.PENDING);
                order.setTimestamp(LocalDateTime.now());

                // Add the order to the user's orders
                currentUser.getOrders().add(order);
                orderRepository.save(order);

                return String.format(StringConstants.Add_Order_Success + " with id : " + " " + order.getId() + "by" + " " + currentUser.getUserName());
            } else {
                // if symbol is not found throw error
                throw new CustomException(ErrorConstants.Symbol_Not_Found);
            }
        } else if ("cancelOrder".equals(orderOperation)) {
            // If the operation is cancelOrder
            long orderId;
            try {
                orderId = Long.parseLong(tradeRequest.getOrderId());
            } catch (NumberFormatException e) {
                // throw an error if the order ID is not a valid integer.
                throw new NumberFormatException(ErrorConstants.Order_Integer);
            }

            // Get the order by its ID
            Optional<Order> optionalOrder = orderRepository.findById(orderId);

            if (optionalOrder.isPresent()) {
                Order savedOrder = optionalOrder.get();

                if (savedOrder.getStatus().equals(StringConstants.PENDING) && savedOrder.getUserOrdersId().getId().equals(currentUser.getId())) {
                    // If the order status is pending and belongs to the current user set status to Cancelled
                    savedOrder.setStatus("CANCELLED");
                    savedOrder.setTimestamp(LocalDateTime.now());
                    return StringConstants.Cancel_Order_Success + " " + currentUser.getUserName();
                } else {
                    // Throw error if the order status is incorrect or doesn't belong to the current user.
                    return ErrorConstants.Order_NotFound;
                }
            } else {
                // Throw error if the order is not found.
                return ErrorConstants.Order_Operation;
            }
        }
        return StringConstants.No_Operation_Performed;
    }

    /* * Executes matching buy and sell orders periodically.*/
    @Scheduled(fixedRate = 30000)
    public void executeOrders() {
        log.info("Executing orders, method: executeOrders");

        // Retrieve buy and sell orders with matching stock symbols
        List<Order> buyOrders = orderRepository.findBuyOrders();
        List<Order> sellOrders = orderRepository.findSellOrders();

        for (Order buyOrder : buyOrders) {
            for (Order sellOrder : sellOrders) {
                // Check if stock symbols match and buy order price is greater or equal to sell order price
                if (buyOrder.getStockSymbol().equals(sellOrder.getStockSymbol()) &&
                        buyOrder.getPrice() >= sellOrder.getPrice()) {

                    // Check if both buy and sell orders have a status of "PENDING"
                    if (buyOrder.getStatus().equals(StringConstants.PENDING) && sellOrder.getStatus().equals(StringConstants.PENDING)) {

                        // Calculate the transaction quantity as the minimum of buy and sell order quantities
                        int quantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

                        if (quantity > 0) {
                            // Create Trade objects for the buy and sell orders
                            buyOrder.setPrice(sellOrder.getPrice());
                            Trade buyTrade = createTrade(buyOrder, StringConstants.Buy, quantity);
                            Trade sellTrade = createTrade(sellOrder, StringConstants.Sell, quantity);

                            // Update user trade history with the buy and sell trades
                            updateUserTradeHistory(buyOrder.getUserOrdersId(), buyTrade);
                            updateUserTradeHistory(sellOrder.getUserOrdersId(), sellTrade);

                            // Update buy and sell order quantities and statuses
                            updateOrder(buyOrder, quantity);
                            updateOrder(sellOrder, quantity);
                        }
                    }
                }
            }
        }
    }

    /* * Updates the user's trade history with a new trade.*/
    private void updateUserTradeHistory(UserDetail userDetail, Trade trade) {
        // Add the trade to the user's trade history
        userDetail.getTrade().add(trade);
        userDetailsRepository.save(userDetail);
    }

    /**Updates the order after a trade is executed.It decreases the order quantity and updates the status*/
    private void updateOrder(Order order, int quantity) {
        // Decrease the order quantity by the trade quantity
        order.setQuantity(order.getQuantity() - quantity);
        // If the order quantity becomes zero, set its status to "EXECUTED"
        if (order.getQuantity() == 0) {
            order.setStatus(StringConstants.EXECUTED);
        }
        orderRepository.save(order);
    }
    /* * Retrieves the portfolio of the current user.*/
//    public List<PortfolioItemDto> getPortfolio() {
//        log.info("Returned by Dashboard service method : getPortfolio");
//
//        // Create an empty list to store portfolio items
//        List<PortfolioItemDto> portfolio = new ArrayList<>();
//
//        // Gets details of the current user
//        getCurrentUserDetails();
//
//        // list of sell orders from the db for the current user
//        List<Order> sellOrders = orderRepository.findByUserOrdersIdAndOrderTypeAndQuantityGreaterThanAndStatus(getCurrentUserDetails(), StringConstants.Sell, 0, StringConstants.PENDING);
//
//        //list of buy orders from the db for the current user
//        List<Order> buyOrders = orderRepository.findByUserOrdersIdAndOrderTypeAndQuantityGreaterThanAndStatus(getCurrentUserDetails(), StringConstants.Buy, 0, StringConstants.PENDING);
//
//        // Loop through each sell order in the list of sell orders.
//        for (Order sellOrder : sellOrders) {
//            // For each sell order, create a PortfolioItemDto, which represents an item in the user's portfolio which stores all the data
//            PortfolioItemDto portfolioItem = createPortfolioItem(sellOrder.getStockSymbol(), sellOrder.getStatus(), sellOrder.getQuantity(), sellOrder.getTimestamp());
//            portfolio.add(portfolioItem);
//        }
//        for (Order buyOrder : buyOrders) {
//            PortfolioItemDto portfolioItem = createPortfolioItem(buyOrder.getStockSymbol(), buyOrder.getStatus(), buyOrder.getQuantity(), buyOrder.getTimestamp());
//            portfolio.add(portfolioItem);
//        }
//        return portfolio;
//    }
    public List<PortfolioItemDto> getPortfolio() {
        log.info("Returned by Dashboard service method: getPortfolio");

        // Create an empty list to store portfolio items
        List<PortfolioItemDto> portfolio = new ArrayList<>();

        // Gets details of the current user
        UserDetail currentUser = getCurrentUserDetails();
        log.info("Current user: " + currentUser.getUserName());

        // list of sell orders from the db for the current user
        List<Order> sellOrders = orderRepository.findByUserOrdersIdAndOrderTypeAndQuantityGreaterThanAndStatus(currentUser, StringConstants.Sell, 0, StringConstants.PENDING);
        log.info("Sell Orders: " + sellOrders.size());

        // list of buy orders from the db for the current user
        List<Order> buyOrders = orderRepository.findByUserOrdersIdAndOrderTypeAndQuantityGreaterThanAndStatus(currentUser, StringConstants.Buy, 0, StringConstants.PENDING);
        log.info("Buy Orders: " + buyOrders.size());

        // Loop through each sell order in the list of sell orders.
        for (Order sellOrder : sellOrders) {
            // For each sell order, create a PortfolioItemDto
            PortfolioItemDto portfolioItem = createPortfolioItem(sellOrder.getStockSymbol(), sellOrder.getStatus(), sellOrder.getQuantity(), sellOrder.getTimestamp());
            portfolio.add(portfolioItem);
        }
        for (Order buyOrder : buyOrders) {
            PortfolioItemDto portfolioItem = createPortfolioItem(buyOrder.getStockSymbol(), buyOrder.getStatus(), buyOrder.getQuantity(), buyOrder.getTimestamp());
            portfolio.add(portfolioItem);
        }
        return portfolio;
    }


    /* * Creates a PortfolioItemDto for a specific order.*/
    private PortfolioItemDto createPortfolioItem(String stockSymbol, String assertType, int quantity, LocalDateTime timeStamp) {
        // Create a new PortfolioItemDto to represent a portfolio item.
        PortfolioItemDto portfolioItem = new PortfolioItemDto();
        // Set the stock symbol,quantity,status,timestamp for the portfolio item.
        portfolioItem.setStockSymbol(stockSymbol);
        portfolioItem.setQuantity(quantity);
        portfolioItem.setOrderStatus(StringConstants.PENDING);
        portfolioItem.setTimestamp(timeStamp);
        return portfolioItem;
    }


    /* * Retrieves the trade history of the current user.*/
    public List<TradeHistoryDto> getTradeHistory() {
        log.info(StringConstants.DashBoard+"getTradeHistory");
        List<TradeHistoryDto> tradeHistory = new ArrayList<>();
        // Retrieve orders and map to TradeHistoryDTO
        tradeHistory.addAll(mapOrdersToTradeHistory(orderRepository.findByUserOrdersId(getCurrentUserDetails())));
        //Retrieve trades and map to TradeHistoryDTO
        tradeHistory.addAll(mapTradesToTradeHistory(tradeRepository.findByUserTradeId(getCurrentUserDetails())));
        return tradeHistory;
    }

    /* * Maps order entities to TradeHistoryDto objects for trade history.*/
    private List<TradeHistoryDto> mapOrdersToTradeHistory(List<Order> orders) {
        // Filter and map orders to TradeHistoryDto objects for trade history.
        return orders.stream()
                .filter(order -> order.getQuantity() > 0 && !order.getStatus().equals(StringConstants.EXECUTED))
                .map(order -> createTradeHistoryItem(order.getStockSymbol(), order.getQuantity(), order.getStatus(), order.getPrice(), order.getOrderType(), order.getTimestamp()))
                .collect(Collectors.toList());
    }

    /* Maps order entities to TradeHistoryDto objects for trade history. */
    private List<TradeHistoryDto> mapTradesToTradeHistory(List<Trade> trades) {
        return trades.stream()
                .map(trade -> createTradeHistoryItem(trade.getStockSymbol(), trade.getQuantity(), StringConstants.EXECUTED, trade.getPrice(), trade.getOrderType(), trade.getTimestamp()))
                .collect(Collectors.toList());
    }

    /* Creates a TradeHistoryDto for trade history entries. */
    private TradeHistoryDto createTradeHistoryItem(String stockSymbol, int quantity, String orderStatus, double price, String orderType, LocalDateTime timestamp) {
        // Create a new TradeHistoryDto to represent a trade history entry.
        TradeHistoryDto tradeHistoryItem = new TradeHistoryDto();
        // Set the stock symbol,quantity,status,tradeHistory,orderType,timestamp for the trade history entry.
        tradeHistoryItem.setStockSymbol(stockSymbol);
        tradeHistoryItem.setQuantity(quantity);
        tradeHistoryItem.setOrderStatus(orderStatus);
        tradeHistoryItem.setPrice(price);
        tradeHistoryItem.setOrderType(orderType);
        tradeHistoryItem.setTimestamp(timestamp);
        return tradeHistoryItem;
    }

    // Create a Trade Item
    private Trade createTrade(Order order, String orderType, int quantity) {
        // Create a new Trade to represent a trade item.
        Trade trade = new Trade();
        // Set the user tradeID,symbol,orderType,quantity,price,timestamp for the trade item.
        trade.setUserTradeId(order.getUserOrdersId());
        trade.setStockSymbol(order.getStockSymbol());
        trade.setOrderType(orderType);
        trade.setQuantity(quantity);
        trade.setPrice(order.getPrice());
        trade.setTimestamp(LocalDateTime.now());
        return trade;
    }

}
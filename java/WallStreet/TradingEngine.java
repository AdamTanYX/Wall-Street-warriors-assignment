package WallStreet;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lichee
 */
public class TradingEngine {

    private List<Stock> stocks;
    private Map<Stock, List<Order>> buyOrders;
    private Map<Stock, List<Order>> sellOrders;
    private TradingApp tradingApp;
    private Map<Stock, Integer> lotPool; // keep track of the 500-lot pool
    private List<Order> pendingOrders;
    private List<Order> orderHistory;

    private Stocklist2 stocklist;
    private Database database;
    private NotificationService notificationService;
    private ScheduledExecutorService executorService;
    private final int INTERVAL_HOURS = 24;

    public enum Criteria {
        CRITERIA_LONGEST_TIME_LENGTH,
        CRITERIA_HIGHEST_AMOUNT_OF_MONEY
    }

    public TradingEngine(List<Stock> stocks) {
        this.stocks = stocks;
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.pendingOrders = new ArrayList<>();
        this.lotPool = new HashMap<>();
        database = new Database();
        for (Stock stock : stocks) {
            buyOrders.put(stock, new ArrayList<>());
            sellOrders.put(stock, new ArrayList<>());
//        lotPool.put(stock, 50000); //each stock have 500lots
//        database.insertlotPool(lotPool); ald inserted
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::replenishLotPool, 0, INTERVAL_HOURS, TimeUnit.HOURS);

    }


    public void executeOrder(Order order, PortFolio portfolio) {
        String symbol = order.getStock().getSymbol();
        double price = order.getStock().getPrice(); //get current market stock price
        double acceptableRange = price * 0.01; // Calculate 1% of the current price
        double lowerBound = price - acceptableRange;
        double upperBound = price + acceptableRange;
        double orderPrice = order.getPrice();
        Order.Position position = order.getPosition();
        // Check if the stock exists in the buyOrders map
        if (!buyOrders.containsKey(order.getStock())) {
            buyOrders.put(order.getStock(), new LinkedList<>());
        }

        // Check if the stock exists in the sellOrders map
        if (!sellOrders.containsKey(order.getStock())) {
            sellOrders.put(order.getStock(), new LinkedList<>());
        }
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Please try again during trading hours.");
            database.updateOrderHistory(order.getID(), "Failed");
            return;
        }
        switch (position) {
            case MARKET:
                if (order.getType() == Order.Type.BUY) {
                    buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                    tryExecuteBuyOrders(order.getStock(), portfolio);

                } else {
                    sellOrders.get(order.getStock()).add(order);
                    tryExecuteSellOrders(order.getStock(), portfolio);
                }
                break;
            case LIMIT:
                if (orderPrice >= lowerBound && orderPrice <= upperBound) {
                    if (order.getType() == Order.Type.BUY) {
                        buyOrders.get(order.getStock()).add(order); //find order whether its available or not , if available, add order
                        tryExecuteBuyOrders(order.getStock(), portfolio);
                    } else {
                        sellOrders.get(order.getStock()).add(order);
                        tryExecuteSellOrders(order.getStock(), portfolio);
                    }
                } else {
                    System.out.println("Price is outside the acceptable range, order failed");
                    database.updateOrderHistory(order.getID(), "Failed");
                }
                break;
        }
    }

    public void tryExecuteBuyOrders(Stock stock, PortFolio portfolio) {
        notificationService = new NotificationService(portfolio.getUser());
        List<Order> orders = buyOrders.get(stock);
        List<Order> availableSellOrder = database.retriveSellPendingOrder(portfolio.getUser()); //If no participants are selling a particular stock, the system will automatically allocate shares from the 500-lot pool for each stock.
        orderHistory = database.retriveOrderHistory(portfolio.getUser());
        double price = stock.getPrice();
        boolean orderExecuted = false;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            double totalPrice = order.getValue();
            if (portfolio.getAccountBalance() >= totalPrice) {
                for (int j = 0; j < availableSellOrder.size(); j++) {
                    Order availablesellorder = availableSellOrder.get(j);
                    if (order.getStock().getSymbol().equals(availablesellorder.getStock().getSymbol())
                            && order.getShares() == availablesellorder.getShares()
                            && order.getPrice() == availablesellorder.getPrice()
                            && !availablesellorder.getName().equals(portfolio.getUser().getName())) {  // searching whether participants are selling the particular stock
                        portfolio.addStock(stock, order.getShares(), order.getPrice());
                        this.buyOrders.get(order.getStock()).remove(order);
                        orderExecuted = true;
                        database.updatePendingOrder(availablesellorder.getID(), "Completed");
                        database.updateOrderHistory(availablesellorder.getID(), "Completed");
                        User user1 = new User(availablesellorder.getName());//update holding on specific user
                        System.out.println(availablesellorder.getName());
                        PortFolio portfolio1 = new PortFolio(user1);
                        portfolio1.removeStock(availablesellorder.getStock(), availablesellorder.getShares(), availablesellorder.getPrice());
                        portfolio1.countProfitLoss(availablesellorder);
                        portfolio1.getHoldings();
                        System.out.println("Buy Order completed");
                        database.updateOrderHistory(order.getID(), "Completed");
                        System.out.println("Current Holdings");
                        System.out.println("-------------------------------------------");
                        System.out.printf("%-20s %-20s\n", "Stock symbol", "Shares");
                        System.out.println("-------------------------------------------");
                        for (Map.Entry<Stock, Integer> entry : portfolio.getHoldings().entrySet()) {
                            Stock stocks = entry.getKey();
                            int shares = entry.getValue();
                            System.out.printf("%-20s %-20s\n", stocks.getSymbol(), shares);
                        }
                        System.out.println("Account Balance: " + portfolio.getAccountBalance());
                        break;
                    }
                }
                if (orderExecuted && !orders.isEmpty()) {
                    orders.remove(i);
                    i--;
                }
                if (!orderExecuted) {
                    lotPool = database.retriveLotPool();
                    int remainingShares = lotPool.getOrDefault(stock, 0);
                    if (remainingShares >= order.getShares() && order.getStock().getPrice() == order.getPrice()) {
                        portfolio.addStock(stock, order.getShares(), order.getPrice());
                        database.UpdatelotPool(stock, remainingShares - order.getShares());
                        orders.remove(i);
                        this.buyOrders.get(order.getStock()).remove(order);
                        i--;
                        System.out.println("Buy Order completed");
                        database.updateOrderHistory(order.getID(), "completed");
                        notificationService.sendBuySuccessful(order);// notification
                        System.out.println("Current Holdings");
                        System.out.println("-------------------------------------------");
                        System.out.printf("%-20s %-20s\n", "Stock symbol", "Shares");
                        System.out.println("-------------------------------------------");
                        for (Map.Entry<Stock, Integer> entry : portfolio.getHoldings().entrySet()) {
                            Stock stocks = entry.getKey();
                            int shares = entry.getValue();
                            System.out.printf("%-20s %-20s\n", stocks.getSymbol(), shares);
                        }
                        System.out.println("Acoount Balance: " + portfolio.getAccountBalance());
                    } else {
                        database.insertPendingOrder(order, portfolio.getUser());
                        System.out.println("No orders available. Order is pending.");
                        notificationService.sendBuyPending(order);
                        break;
                    }
                }
            } else {
                System.out.println("Current Account Balance not enough.Order Failed.");
                orders.remove(i);
                this.buyOrders.get(order.getStock()).remove(order);
                database.updateOrderHistory(order.getID(), "Failed");
                i--;
            }


        }
    }

    public void tryExecuteSellOrders(Stock stock, PortFolio portfolio) {//if dont have user want to buy the sell order, will added to the pending list
        notificationService = new NotificationService(portfolio.getUser());
        List<Order> orders = sellOrders.get(stock);
        orderHistory = database.retriveOrderHistory(portfolio.getUser());
        List<Order> availableBuyOrder = database.retriveBuyPendingOrder(portfolio.getUser());
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            boolean matchFound = false;
            for (int j = 0; j < availableBuyOrder.size(); j++) {
                Order availablebuyorder = availableBuyOrder.get(j);
                if (order.getStock().getSymbol().equals(availablebuyorder.getStock().getSymbol())
                        && order.getShares() == availablebuyorder.getShares()
                        && order.getPrice() == availablebuyorder.getPrice()
                        && !availablebuyorder.getName().equals(portfolio.getUser().getName())) {  // searching whether participants are buying the particular stock
                    int currentShares = portfolio.getHoldings().getOrDefault(stock, 0);
                    System.out.println(currentShares);
                    if (currentShares >= order.getShares()) {
                        portfolio.removeStock(stock, order.getShares(), order.getPrice());
                        orders.remove(i);
                        this.sellOrders.get(order.getStock()).remove(order);
                        database.updatePendingOrder(availablebuyorder.getID(), "Completed");
                        database.updateOrderHistory(availablebuyorder.getID(), "Completed");
                        User user1 = new User(availablebuyorder.getName());//add holding on specific user
                        PortFolio portfolio1 = new PortFolio(user1);
                        portfolio1.addStock(availablebuyorder.getStock(), availablebuyorder.getShares(), availablebuyorder.getPrice());
                        i--;
                        matchFound = true;
                        System.out.println("Sell Order Completed.");
                        System.out.println("Current Holdings");
                        System.out.println("-------------------------------------------");
                        System.out.printf("%-20s %-20s\n", "Stock symbol", "Shares");
                        System.out.println("-------------------------------------------");
                        for (Map.Entry<Stock, Integer> entry : portfolio.getHoldings().entrySet()) {
                            Stock stocks = entry.getKey();
                            int shares = entry.getValue();
                            System.out.printf("%-20s %-20s\n", stocks.getSymbol(), shares);
                        }
                        System.out.println("Acoount Balance: " + portfolio.getAccountBalance());
                        database.updateOrderHistory(order.getID(), "Completed");
                        notificationService.sendSellSuccessful(order); //notification
                        portfolio.countProfitLoss(order);
                        double profitLoss = order.getValue() - (database.retriveLatestBuyPrice(portfolio.getUser(), order.getStock()) * order.getShares());
                        double newProfitLoss = portfolio.getUser().getTotalPnL() + profitLoss;
                        database.updateProfitLoss(portfolio.getUser().getName(), newProfitLoss);
                        Automatching(order, portfolio.getUser());
                        break;
                    } else {
                        System.out.println("Not enough share to sell.Order failed");
                        orders.remove(i);
                        this.sellOrders.get(order.getStock()).remove(order);
                        database.updateOrderHistory(order.getID(), "Failed");
                        i--;
                        break;
                    }
                }
            }
            if (!matchFound) {
                database.insertPendingOrder(order, portfolio.getUser());
                System.out.println("No buy orders available. Order is pending.");
                notificationService.sendSellPending(order);

            }
        }

    }


    public void CheckPendingOrder(PortFolio portfolio) {
        System.out.println("Checking for pending order...");
        List<Order> pendingOrders = database.retrivePendingOrder(portfolio.getUser()); // Own pending orders
        List<Order> othersBuyPendingOrders = database.retriveBuyPendingOrder(portfolio.getUser());
        List<Order> othersSellPendingOrders = database.retriveSellPendingOrder(portfolio.getUser());
        boolean temp = false;
        for (Order pendingOrder : pendingOrders) {
            for (Order otherBuyOrder : othersBuyPendingOrders) {
                if (pendingOrder.getType() != otherBuyOrder.getType()
                        && pendingOrder.getStock().getSymbol().equals(otherBuyOrder.getStock().getSymbol())
                        && pendingOrder.getShares() == otherBuyOrder.getShares()
                        && pendingOrder.getPrice() == otherBuyOrder.getPrice()
                        && !pendingOrder.getName().equals(portfolio.getUser().getName())) {
                    buyOrders.get(pendingOrder.getStock()).add(pendingOrder);
                    tryExecuteBuyOrders(pendingOrder.getStock(), portfolio);
                    System.out.println("Your buy pending order has been successfully completed.");
                    System.out.println(pendingOrder);
                    database.updatePendingOrder(pendingOrder.getID(), "Completed");
                    database.updateOrderHistory(pendingOrder.getID(), "Completed");
                    temp = true;
                    break;
                }
            }

            for (Order otherSellOrder : othersSellPendingOrders) {
                if (pendingOrder.getType() != otherSellOrder.getType()
                        && pendingOrder.getStock().getSymbol().equals(otherSellOrder.getStock().getSymbol())
                        && pendingOrder.getShares() == otherSellOrder.getShares()
                        && pendingOrder.getPrice() == otherSellOrder.getPrice()
                        && !pendingOrder.getName().equals(portfolio.getUser().getName())) {
                    sellOrders.get(pendingOrder.getStock()).add(pendingOrder);
                    tryExecuteSellOrders(pendingOrder.getStock(), portfolio);
                    System.out.println("Your sell pending order has been successfully completed.");
                    System.out.println(pendingOrder);
                    database.updatePendingOrder(pendingOrder.getID(), "Completed");
                    database.updateOrderHistory(pendingOrder.getID(), "Completed");
                    temp = true;
                    break;
                }
            }

            if (pendingOrder.getStock().getPrice() == pendingOrder.getPrice()) {
                if (pendingOrder.getType() == Order.Type.BUY) {
                    buyOrders.get(pendingOrder.getStock()).add(pendingOrder);
                    tryExecuteBuyOrders(pendingOrder.getStock(), portfolio);
                    database.updatePendingOrder(pendingOrder.getID(), "Completed");
                    database.updateOrderHistory(pendingOrder.getID(), "Completed");
                    temp = true;
                }
            }
        }
        if (!temp) {
            System.out.println("No pending order matched.");
        }
    }


    public void cancelPendingOrder(Criteria criteria, PortFolio portfolio) {
        notificationService = new NotificationService(portfolio.getUser());
        Criteria criterias = criteria;
        pendingOrders = database.retrivePendingOrder(portfolio.getUser());
        if (!pendingOrders.isEmpty()) {
            switch (criterias) {
                case CRITERIA_LONGEST_TIME_LENGTH:
                    Order longestTimeOrder = null;
                    String longestTime = null;
                    for (Order order : pendingOrders) {
                        String orderTime = order.getTime();
                        if (longestTime == null || orderTime.compareTo(longestTime) > 0) {
                            longestTime = orderTime;
                            longestTimeOrder = order;
                        }
                    }
                    if (longestTimeOrder != null) {
                        Order canceledOrder = pendingOrders.remove(0);
                        database.updatePendingOrder(canceledOrder.getID(), "Canceled");
                        System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol()
                                + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
                        database.updateOrderHistory(canceledOrder.getID(), "Canceled");
                        notificationService.cancelOrder(canceledOrder);
                    }
                    break;
                case CRITERIA_HIGHEST_AMOUNT_OF_MONEY:
                    pendingOrders.sort(Comparator.comparing(Order::getValue).reversed());
                    Order canceledOrder = pendingOrders.remove(0);
                    database.updatePendingOrder(canceledOrder.getID(), "Canceled");
                    System.out.println("Canceled order: " + canceledOrder.getStock().getSymbol()
                            + " - " + canceledOrder.getShares() + " shares at " + canceledOrder.getPrice());
                    database.updateOrderHistory(canceledOrder.getID(), "Canceled");
                    notificationService.cancelOrder(canceledOrder);
                    break;
            }

        } else {
            System.out.println("No pending order available.");
        }
    }

    public void Automatching(Order order, User user) {
        tradingApp = new TradingApp(this);
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to purchase this order?");  //Automatching to others competitors sell order
        List<Order> sellorder = database.retriveSellPendingOrder(user);
        List<Order> matchSellOrder = new ArrayList<>();
        double value = order.getValue();
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-20s\n", "Order ID", " Stock", "Shares", "Price");
        System.out.println("------------------------------------------------------------------");
        if (sellorder.isEmpty()) {
            System.out.printf("%-20s %-20s %-20s\n", "null", "null", "null", "null");
            System.out.println("No order matched");
        }
        for (Order orders : sellorder) {
            double pendingOrdervalue = orders.getValue();
            if (value == pendingOrdervalue) {
                matchSellOrder.add(orders);
            }
        }
        if (matchSellOrder.isEmpty()) {
            System.out.printf("%-20s %-20s %-20s %-20s\n", "null", "null", "null", "null");
            System.out.println("No order matched");
        } else {
            for (Order match : matchSellOrder) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", match.getID(), match.getStock().getSymbol(), match.getShares(), match.getPrice());
            }
            System.out.println("1: Yes \n2: No");
            int response = sc.nextInt();
            switch (response) {
                case 1:
                    System.out.println("Enter Order ID");
                    int id = sc.nextInt();
                    for (Order match : matchSellOrder) {
                        if (match.getID() == id) {
                            Order.Type type = Order.Type.BUY;
                            int orderId = GenerateId();
                            Order order1 = new Order(orderId, match.getStock(), type, match.getPosition(), match.getShares(), match.getPrice());
                            tradingApp.placeOrder(user, order1);

                        }
                    }
                    break;

                case 2:
                    return;
            }
        }
    }


    public boolean isTradingHours() {
        // Get the current time
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();

        // Get the day of the week
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // Check if the day falls within the trading week (Monday to Friday)
        boolean isTradingDay = (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);

        LocalTime startMorning = LocalTime.of(9, 0);
        LocalTime endMorning = LocalTime.of(12, 30);
        LocalTime startAfternoon = LocalTime.of(14, 30);
        LocalTime endAfternoon = LocalTime.of(23, 50);

        // Check if the current time is within the trading hours
        boolean isMorningSession = currentTime.isAfter(startMorning) && currentTime.isBefore(endMorning);
        boolean isAfternoonSession = currentTime.isAfter(startAfternoon) && currentTime.isBefore(endAfternoon);

        return ((isMorningSession || isAfternoonSession));
    }

    public boolean checkCompetitionPeriod() {
        LocalDateTime sessionStart = LocalDateTime.of(2023, 6, 5, 9, 0);
        LocalDateTime now = LocalDateTime.now();
        long noOfDays = Duration.between(sessionStart, now).toDays();
        return noOfDays == 42;
    }

    public boolean isInitialTradingPeriod() {
        LocalDateTime sessionStart = LocalDateTime.of(2023, 6, 5, 9, 0);
        LocalDateTime now = LocalDateTime.now();
        long noOfDays = Duration.between(sessionStart, now).toDays();
        return noOfDays < 3;
    }

    public boolean isClosingTime() {
        LocalTime now = LocalTime.now();
        LocalTime closingTime = LocalTime.of(5, 0);
        boolean isClosingTime = now.equals(closingTime);

        return isClosingTime;
    }

    public void displaySuggestedPrice(Stock stock) {
        double stockPrice = stock.getPrice(); //get current market stock price
        double acceptableRange = stockPrice * 0.01; // Calculate 1% of the current price
        double lowerBound = stockPrice - acceptableRange;
        double upperBound = stockPrice + acceptableRange;
        System.out.printf("Suggested price range  (%.4f): %.4f - %.4f%n", stock.getPrice(), lowerBound, upperBound);
    }

    public void replenishLotPool() { //this is just the basic method, havent implemented a timer since idk how its gonna be used :p
        Map<Stock, Integer> lotpools = new HashMap<>();
        lotpools = database.retriveLotPool();
        for (Map.Entry<Stock, Integer> lotpool : lotPool.entrySet()) {
            Stock stock = lotpool.getKey();
            int share = lotpool.getValue();
            database.UpdatelotPool(stock, 50000);
        }
    }

    public int GenerateId() {
        Random rd = new Random();
        int id;
        List<Integer> orderID = database.retriveOrderID();
        HashSet<Integer> nonduplicateID = new HashSet<>();
        nonduplicateID.addAll(orderID);
        do {
            id = rd.nextInt(10000);
        } while (nonduplicateID.contains(id));
        return id;
    }

    public void getPendingOrders(User user) {
        List<Order> pendingorder = database.retrivePendingOrder(user);
        System.out.println("Pending Order:");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "Order ID", "Time", "Stock Symbol", "Type", "Position", "Price", "Shares");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        if (pendingorder.isEmpty()) {
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "null", "null", "null", "null", "null", "null", "null");
        }
        for (Order order : pendingorder) {
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", order.getID(), order.getTime(),
                    order.getStock().getSymbol(), order.getType(), order.getPosition(), order.getPrice(), order.getShares());
        }
    }
}



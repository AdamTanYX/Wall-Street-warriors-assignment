package WallStreet;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;

/**
 * @author lichee
 */
public class TradingApp {

    private TradingEngine tradingEngine;
    private PortFolio portfolio;
    private AdminPanel adminPanel;
    private List<Order> orderHistory;
    private Database database;
    private Stocklist2 stocklist;

    public TradingApp(TradingEngine tradingEngine) {
        this.tradingEngine = tradingEngine;

    }


    public void setOrder(User user) {
        this.database = new Database();
        this.stocklist = new Stocklist2();
        stocklist.fetchStockList();
        Order order = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("1: Buy\n2: Sell");
        int type = sc.nextInt();
        Order.Type orderType;
        switch (type) {
            case 1:
                orderType = Order.Type.BUY;
                break;
            case 2:
                orderType = Order.Type.SELL;
                break;
            default:
                System.out.println("Invalid option. Exiting.");
                return;
        }
        if (orderType == Order.Type.BUY) {
            //display all the stock available and shares(need to replenish lot first)
            System.out.println("Do you want to check stock available and it current share? \n1: Yes\n2: No ");
            int check = sc.nextInt();
            switch (check) {
                case 1:
                    System.out.println("Stock available");
                    System.out.println("-----------------------------------------------------");
                    System.out.printf("%-20s %-20s \n", "Stock", "Shares");
                    System.out.println("-----------------------------------------------------");
                    Map<Stock, Integer> lotpool = database.retriveLotPool();
                    for (Map.Entry<Stock, Integer> entry : lotpool.entrySet()) {
                        Stock stock = entry.getKey();
                        int shares = entry.getValue();
                        System.out.printf("%-20s %-20s\n", stock.getSymbol(), shares);
                    }

                    break;
                case 2:
                    break;
            }
            List<Order> sellorder = database.retriveSellPendingOrder(user);
            System.out.println("Others competitors sell order list");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-20s %-20s %-20s\n", "Stock", "Shares", "Price");
            System.out.println("-----------------------------------------------------");
            if (sellorder.isEmpty()) {
                System.out.printf("%-20s %-20s %-20s\n", "null", "null", "null");
            }
            for (int i = 0; i < sellorder.size(); i++) {
                Order orders = sellorder.get(i);
                Stock stock = orders.getStock();
                int share = orders.getShares();
                double price = orders.getPrice();
                System.out.printf("%-20s %-20s %-20s\n", stock.getSymbol(), share, price);
            }

        } else if (orderType == Order.Type.SELL) {
            System.out.println("Own Stock available to sell: ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-20s %-20s\n", " Stock", "Shares");
            System.out.println("-----------------------------------------------------");
            Map<Stock, Integer> holdings = database.retriveHoldings(user);
            if (holdings.isEmpty()) {
                System.out.printf("%-20s %-20s \n", "null", "null");
                System.out.println("No stock to sell.");
                return;
            }
            for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
                Stock stock = entry.getKey();
                int shares = entry.getValue();
                System.out.printf("%-20s %-20s\n", stock.getSymbol(), shares);
            }
            List<Order> buyorder = database.retriveBuyPendingOrder(user);
            System.out.println("Others competitors buy order list");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-20s %-20s %-20s\n", " Stock", "Shares", "Price");
            System.out.println("-----------------------------------------------------");
            if (buyorder.isEmpty()) {
                System.out.printf("%-20s %-20s %-20s\n", "null", "null", "null");
            }
            for (int i = 0; i < buyorder.size(); i++) {
                Order orders = buyorder.get(i);
                Stock stock = orders.getStock();
                int share = orders.getShares();
                double price = orders.getPrice();
                System.out.printf("%-20s %-20s %-20s\n", stock.getSymbol(), share, price);
            }
        }
        sc.nextLine();
        String symbol = "";
        while (true) {
            System.out.println("Enter stock symbol: ");
            symbol = sc.nextLine().toUpperCase(); // Convert symbol to uppercase for consistency

            if (stocklist.checkStocks(symbol)) {
                break;
            } else
                System.out.println("Stock not available.Please enter again.");
        }
        int maxShares = tradingEngine.isInitialTradingPeriod() ? Integer.MAX_VALUE : 500;
        int shares = 0;
        while (true) {
            System.out.println("Enter amount of shares: ");
            shares = sc.nextInt();
            if (shares <= maxShares) {
                break;
            } else
                System.out.println("Exceed order limitation");

        }
        System.out.println("1: Market\n2: Limit");
        int position = sc.nextInt();
        Order.Position orderPosition;
        switch (position) {
            case 1:
                orderPosition = Order.Position.MARKET;
                break;
            case 2:
                orderPosition = Order.Position.LIMIT;
                break;
            default:
                System.out.println("Invalid option. Exiting.");
                return;
        }

        if (orderPosition == Order.Position.LIMIT) {
            Stock stock = new Stock(symbol);
            tradingEngine.displaySuggestedPrice(stock);
            System.out.println("Enter price: ");
            double price = sc.nextDouble();
            int id = tradingEngine.GenerateId();
            order = new Order(id, stock, orderType, orderPosition, shares, price);
            System.out.println(order);
            placeOrder(user, order);
        } else {
            int id = tradingEngine.GenerateId();
            order = new Order(id, new Stock(symbol), orderType, orderPosition, shares);
            System.out.println(order);
            placeOrder(user, order);
        }
    }


    public void placeOrder(User user, Order order) {
        database = new Database();
        database.insertOrderHistory(order, user);
        portfolio = new PortFolio(user);
        tradingEngine.executeOrder(order, portfolio);
        if (tradingEngine.isClosingTime()) {
            adminPanel.checkAccountBalance();
        }

    }

}
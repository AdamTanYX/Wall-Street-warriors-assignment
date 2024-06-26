package WallStreet;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @author lichee
 */
public class PortFolio {

    private final double INITIALFUND = 500000;
    private Map<Stock, Integer> holdings;
    private double accountBalance;
    private boolean disqualified;
    private User user;
    private Database database;

    public PortFolio(User user) {
        this.user = user;
        holdings = new HashMap<>();
        this.accountBalance = INITIALFUND;
        this.disqualified = false;
        database = new Database();

    }

    public void addStock(Stock stock, int shares, double price) {
        if (database.readHoldings(user.getName(),stock.getSymbol())) {
            holdings = database.retriveHoldings(user);
            int currentShares = holdings.getOrDefault(stock, 0);
            int newShares = currentShares + shares;
            database.updateHoldings(user.getName(), stock.getSymbol(), newShares,price);
        } else {
            database.insertHoldings(user.getName(), stock.getSymbol(), shares,price);
        }
        accountBalance = database.retriveAccbalance(user.getName());
        this.accountBalance -= price * shares;
        database.updateAccbalance(user.getName(), accountBalance);
    }


    public void removeStock(Stock stock, int shares, double price) {
        holdings = database.retriveHoldings(user);
        int currentShares = holdings.getOrDefault(stock, 0);
        int newShares = currentShares - shares;
        database.updateHoldings(user.getName(), stock.getSymbol(), newShares);

        accountBalance = database.retriveAccbalance(user.getName());
        this.accountBalance += price * shares;
        database.updateAccbalance(user.getName(), accountBalance);
    }


    public Map<Stock, Integer> getHoldings() {

        return database.retriveHoldings(user);
    }

    public Map<String, Integer> getStockShare() { //ignore, this is for the dashboard
        Map<String, Integer> stockHoldings = new HashMap<>();
        for (Map.Entry<Stock, Integer> entry : database.retriveHoldings(user).entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            stockHoldings.put(stock.getName(), shares);
        }
        return stockHoldings;
    }
    public void countProfitLoss(Order order){
        double profitLoss = order.getValue()-(database.retriveLatestBuyPrice(user,order.getStock())*order.getShares());
        double newProfitLoss =user.getTotalPnL()+profitLoss;
        database.updateProfitLoss(user.getName(),newProfitLoss);
    }


    public double getValue() {
        double value = 0.0;
        for (Map.Entry<Stock, Integer> entry : database.retriveHoldings(user).entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            value += stock.getPrice() * shares;
        }
        return value;
    }
    public boolean hasStock(Stock stock, int quantity) {
        Integer currentQuantity = database.retriveHoldings(user).getOrDefault(stock, 0);
        return currentQuantity >= quantity;
    }


    public double getAccountBalance() {

        return database.retriveAccbalance(user.getName());
    }

    public User getUser() {
        return user;
    }
}

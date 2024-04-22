package WallStreet;

public class Transaction {
    private User user;
    private Order order;

    public Transaction(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    public Transaction(String name, String email, Stock stock, Order.Type type, int shares) {
    }

    public User getUser() {
        return user;
    }

    public Order getOrder() {
        return order;
    }

    public Stock getStock() {
        return order.getStock();
    }

    public int getShares() {
        return order.getShares();
    }

    public double getTotalCost() {
        return order.getShares() * order.getPrice();
    }

    public Order.Type getType() {
        return order.getType();
    }

    // Additional methods can be added here based on your requirements

    public boolean isBuyTransaction() {
        return order.getType() == Order.Type.BUY;
    }

    public boolean isSellTransaction() {
        return order.getType() == Order.Type.SELL;
    }

    public double getTransactionPrice() {
        return order.getPrice();
    }

    public double getTransactionCost() {
        return order.getShares() * order.getPrice();
    }

    public String toString(){
        return order.getTime()+" "+user.getName()+" "+order.getStock().getSymbol()+" "+order.getType()+" "+order.getShares()+" "+order.getShares();
    }
}
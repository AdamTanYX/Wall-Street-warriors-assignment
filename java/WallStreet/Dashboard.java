package WallStreet;
import java.util.*;
import java.util.Map.Entry;

public class Dashboard {
    private PortFolio pf;
    private Database database;

    public enum displayType {
        CHRONOLOGICAL,
        PRICE
    }

    public Dashboard(PortFolio pf) {
        this.pf = pf;
    }

    private class PriceComparator implements Comparator<Order> {
        @Override
        public int compare(Order order1, Order order2) {
            return Double.compare(order1.getPrice(), order2.getPrice());
        }
    }

    public void displayAll(displayType type) {
        database = new Database();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             DASHBOARD                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Account balance: %-47.2f ║%n", pf.getAccountBalance());
        System.out.printf("║ Current points: %-48s ║%n", String.format("%-9s", pf.getUser().getPoints()));
        System.out.printf("║ Current ranking: %-47s ║%n", String.format("%-9s", pf.getUser().getRanking()));
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Holdings:                                                        ║");
        System.out.println("║ Stock name                   ║ Shares                            ║");
        for (Entry<Stock, Integer> entry : database.retriveHoldings(pf.getUser()).entrySet()) {
            Stock stock = entry.getKey();
            int shares = entry.getValue();
            System.out.printf("║ %-28s ║ %-10d                        ║%n", stock.getSymbol(), shares);
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");

        System.out.println("║ Transaction history:                                             ║");
        List<Order> orderHistory = database.retriveOrderHistory(pf.getUser());
        if(orderHistory.isEmpty()){
            System.out.println("║ No past transactions.                                            ║");
        }
        else{
            Collections.sort(orderHistory, new PriceComparator());
            for (Order order : orderHistory){
                if(order.getStatus().equalsIgnoreCase("Completed")){
                    if (type == displayType.PRICE) {
                        String line = "══════════════════════════════════════════════════════════════════";
                        System.out.println(centerAlignWithBorders("Order Type: " + order.getType()));
                        System.out.println(centerAlignWithBorders("Stock: " + order.getStock().getSymbol()));
                        System.out.println(centerAlignWithBorders("Price: " + order.getPrice()));
                        System.out.println(centerAlignWithBorders("Shares: " + order.getShares()));
                        System.out.println(centerAlignWithBorders(line));
                    }
                    else{
                        String line = "══════════════════════════════════════════════════════════════════";
                        System.out.println(centerAlignWithBorders("Order Type: " + order.getType()));
                        System.out.println(centerAlignWithBorders("Stock: " + order.getStock().getSymbol()));
                        System.out.println(centerAlignWithBorders("Price: " + order.getPrice()));
                        System.out.println(centerAlignWithBorders("Shares: " + order.getShares()));
                        System.out.println(centerAlignWithBorders(line));
                    }
                }
            }
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    public static String centerAlignWithBorders(String text) {
        int totalSpaces = 66 - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        StringBuilder sb = new StringBuilder();
        sb.append("║");
        for (int i = 0; i < leftSpaces; i++) {
            sb.append(" ");
        }
        sb.append(text);
        for (int i = 0; i < rightSpaces; i++) {
            sb.append(" ");
        }
        sb.append("║");
        return sb.toString();
    }
}
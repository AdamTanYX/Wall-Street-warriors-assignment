package WallStreet;

import java.util.*;


public class Main {
    private static TradingApp tradingApp;
    private static CreateAccount createAccount;
    private static TradingEngine tradingEngine;
    private static User user;
    private static PortFolio portfolio;
    private static Database database;
    private static stockSearch2 stockSearch;
    private static AdminPanel adminPanel;
    private static NotificationService noti;
    private static Dashboard dashboard;
    private static Leaderboard lb;

    public static void main(String[] args) {
        createAccount = new CreateAccount();
        database = new Database();
        Stocklist2 stocklist = new Stocklist2();
        Price2 price2 = new Price2();
        PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
        TradingEngine tradingEngine = new TradingEngine(new ArrayList<>(retrievedStockList));
        TradingApp tradingapp = new TradingApp(tradingEngine);
        PortFolio portfolio = new PortFolio(user);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the trading competition!");
        System.out.println("Press 1 to create account, 2 to login");
        int num = scanner.nextInt();
        switch (num) {
            case 1:
                createAccount.Register();
                createAccount.Login();
                break;
            case 2:
                createAccount.Login();
                break;
        }
        if (createAccount.getSuccess()) {
            user = createAccount.getUser();
            portfolio = new PortFolio(createAccount.getUser());
            System.out.println("********************************");
            System.out.println("Enter your option");
            System.out.println("-------------------------------");
            System.out.println("1: Place order \n2: Pending order \n3: Notification \n4. Check holdings \n5. Leaderboard \n6. Stock search \n7. Trading Dashboard \n8. Leaderboard \n9. Report \n10. Order History \n11. Admin Panel \n12.Logout");
            int num1 = scanner.nextInt();

            while (true) {
                switch (num1) {
                    case 1:
                        tradingapp.setOrder(portfolio.getUser());
                        tradingEngine.CheckPendingOrder(portfolio);//keep checking
                        break;
                    case 2:
                        tradingEngine.CheckPendingOrder(portfolio);
                        tradingEngine.getPendingOrders(user);
                        System.out.println("Do you want to cencel your pending order?\n1: Yes\n2. No");
                        int num4 = scanner.nextInt();
                        if (num4 == 1) {
                            System.out.println("1. Cancel order base on longest time \n2. Cancel order base on highest amount of money");
                            int criteria = scanner.nextInt();
                            TradingEngine.Criteria criterias = null;
                            switch (criteria) {
                                case 1:
                                    criterias = TradingEngine.Criteria.CRITERIA_LONGEST_TIME_LENGTH;
                                    break;
                                case 2:
                                    criterias = TradingEngine.Criteria.CRITERIA_HIGHEST_AMOUNT_OF_MONEY;
                                    break;
                            }
                            tradingEngine.cancelPendingOrder(criterias, portfolio);
                            tradingEngine.CheckPendingOrder(portfolio); //keep checking the condition
                            break;
                        } else if (num4 == 2) {
                            break;
                        }

                    case 3:
                        noti = new NotificationService(user);
                        System.out.println("1. Enable Notification\n2. Disable Notification");
                        int num5 = scanner.nextInt();
                        if (num5 == 1) {
                            noti.setEnable(true);
                            System.out.println("You have enable your notification service");
                            break;
                        } else if (num5 == 2) {
                            noti.setEnable(false);
                            System.out.println("You have disable your notification service");
                            break;
                        }
                        noti.scheduleNotificationCheck();
                        break;


                    case 4:
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
                    case 5:
                        Leaderboard leaderboard=new Leaderboard();
                        leaderboard.displayLeaderboard();
                        break;
                    case 6:
                        stockSearch = new stockSearch2(retrievedStockList);
                        stockSearch.searchStocks();
                        break;
                    case 7:
                        dashboard = new Dashboard(portfolio);
                        Dashboard.displayType type1 = null;
                        System.out.println("1: CHRONOLOGICAL \n2: PRICE");
                        int choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                type1 = Dashboard.displayType.CHRONOLOGICAL;
                                break;
                            case 2:
                                type1 = Dashboard.displayType.PRICE;
                                break;
                        }
                        dashboard.displayAll(type1);
                        break;
                    case 8:
                        lb = new Leaderboard();
                        lb.displayLeaderboard();
                        break;
                    case 9:

                        System.out.println("Enter format: pdf/csv/text");
                        scanner.nextLine();
                        String format = scanner.nextLine().trim().toLowerCase();

                        if (!format.isEmpty()) {
                            switch (format) {
                                case "pdf":
                                case "csv":
                                case "text":
                                    ReportGenerator.generateReport(user, format);
                                    break;
                                default:
                                    System.out.println("Invalid format. Please enter 'pdf', 'csv', or 'text'.");
                            }
                        } else {
                            System.out.println("Format cannot be empty. Please enter 'pdf', 'csv', or 'text'.");
                        }
                        break;
                    case 10:
                        List<Order> orderHistory = database.retriveOrderHistory(user);
                        System.out.println("Order History: ");
                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "Order ID", "Time", "Type", "Position", "Price", "Share", "Total Price");
                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
                        for (Order OrderHistory : orderHistory) {
                            int ID = OrderHistory.getID();
                            String time = OrderHistory.getTime();
                            Order.Type type = OrderHistory.getType();
                            Order.Position position = OrderHistory.getPosition();
                            double price = OrderHistory.getPrice();
                            int share = OrderHistory.getShares();
                            double total = OrderHistory.getValue();
                            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", ID, time, type, position, price, share, total);
                        }
                        break;
                    case 11:
                        if (!user.getEmail().equals("22057495@siswa.um.edu.my") && !user.getEmail().equals("22004889@siswa.um.edu.my")) {
                            System.out.println("Access denied. You are not admin!");
                            break;
                        } else
                            adminPanel = new AdminPanel();
                        while (true) {
                            System.out.println("1: User List\n2. Order History\n3. Disqulify User\n4. AutoFraudChecking \n5. Quit");
                            int num3 = scanner.nextInt();
                            switch (num3) {
                                case 1:
                                    adminPanel.userList();
                                    break;
                                case 2:
                                    System.out.println("Enter username to see orderHistory.");
                                    String name2 = scanner.nextLine();
                                    User user1 = new User(name2);
                                    List<Order> orderHistory2 = database.retriveOrderHistory(user);
                                    System.out.println("Order History: ");
                                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
                                    System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s\n", "Order ID", "Time", "Type", "Position", "Price", "Share", "Total Price");
                                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
                                    for (Order OrderHistory : orderHistory2) {
                                        int ID = OrderHistory.getID();
                                        String time = OrderHistory.getTime();
                                        Order.Type type = OrderHistory.getType();
                                        Order.Position position = OrderHistory.getPosition();
                                        double price = OrderHistory.getPrice();
                                        int share = OrderHistory.getShares();
                                        double total = OrderHistory.getValue();
                                        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", ID, time, type, position, price, share, total);
                                    }
                                    break;

                                case 3:
                                    System.out.println("Enter username to disqualified competitior: ");
                                    scanner.nextLine();
                                    String name = scanner.nextLine();
                                    adminPanel.disqualifyUser(name);
                                    break;
                                case 4:
                                    FraudDetection fraudDetection = new FraudDetection();
                                    fraudDetection.detectFraud();

                                    if (!fraudDetection.isFraudDetected()) {
                                        System.out.println("No fraud detected.");
                                    }
                                    break;
                                case 5:
                                    return;
                            }
                        }

                    case 12:
                        System.exit(0);
                }
                System.out.println();
                System.out.println("********************************");
                System.out.println("Enter your option");
                System.out.println("-------------------------------");
                System.out.println("1: Place order \n2: Pending order \n3: Notification \n4. Check holdings \n5. Leaderboard \n6. Stock search \n7. Trading Dashboard \n8. Leaderboard \n9. Report \n10. Order History \n11. Admin Panel \n12.Logout");
                num1 = scanner.nextInt();
            }
        } else {
            System.out.println("Incorrect email address or password. Please try again.");
            createAccount.Login();
        }
    }
}
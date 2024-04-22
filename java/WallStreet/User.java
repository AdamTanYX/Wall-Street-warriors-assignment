package WallStreet;

import java.util.*;

public class User{


    private String name;
    private String email;
    private String password;
    private String status;
    private Database database = new Database();
    private Queue<Order> transactionHistory;
    private String id;
    private double accountBalance;
    private double totalPnL;
    private int numTrades;
    private int winningTrades;
    private int losingTrades;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        //this.portfolio = new PortFolio();
    }
    public User(String name, String email, String password,String status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status= status;
        //this.portfolio = new PortFolio();
    }

    public User(String name, String email, String password,String status,double accountBalance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status= status;
        this.accountBalance=accountBalance;
        //this.portfolio = new PortFolio();
    }


    public User(String id, double accountBalance, double totalPnL, int numTrades, int winningTrades, int losingTrades) {
        this.id = id;
        this.accountBalance = accountBalance;
        this.totalPnL = totalPnL;
        this.numTrades = numTrades;
        this.winningTrades = winningTrades;
        this.losingTrades = losingTrades;
    }


    public User(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
    public void setName() {
        this.name=name;
    }
    public void setEmail() {
        this.email=email;
    }
    public void setPassword() {
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void Requalified(){
        this.status="Qualified";
    }


    public String getStatus(){
        return status;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }


    public String getId() {
        return id;
    }


    public int getNumTrades() {
        return numTrades;
    }

    public int getWinningTrades() {
        return winningTrades;
    }

    public int getLosingTrades() {
        return losingTrades;
    }

    public double getAccountBalance(){
        return accountBalance;
    }

    public double getTotalPnL(){
        return database.retriveProfitLoss(name);
    }

    public double getPoints() {
        double points = (database.retriveProfitLoss(name) / 50000) * 100;
        String formattedPoints = String.format("%.3f", points);
        return Double.parseDouble(formattedPoints);

    }

    public Queue<Order> getTransactionHistory() {
        database = new Database();
        transactionHistory = new LinkedList<>(database.retriveOrderHistory(this));
        return transactionHistory;
    }
    public void Disqualified() {
        this.status = "disqualified";
        database.updateStatus(status, name);
    }

    public int getRanking() {
        Database database = new Database();
        List<User> userList = database.retriveUserList();

        List<User> usersToRemove = new ArrayList<>();

        for (User user : userList) {
            if (user.getName().equalsIgnoreCase("Admin")) {
                usersToRemove.add(user);
            }
        }

        userList.removeAll(usersToRemove);

        userList.sort(Comparator.comparing(User::getPoints).reversed());

        int currentRank = 1;
        double previousPoints = -1;

        for (int i = 0; i < userList.size(); i++) {
            User currentUser = userList.get(i);

            if (currentUser.getPoints() != previousPoints) {
                // Points changed, update the current rank
                currentRank = i + 1;
            }

            if (currentUser.equals(this)) {
                return currentRank;
            }

            previousPoints = currentUser.getPoints();
        }

        return -1; // User not found
    }

    //added
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User otherUser = (User) obj;
        return Objects.equals(this.name, otherUser.name);

    }

}






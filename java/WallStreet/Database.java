package WallStreet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/assignment";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "chai123!";

    public void insertUser(String name, String email, String password) {

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO user (Name, Email, Password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the parameters in the prepared statement
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            System.out.println("insert user Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean readUser(String email, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Email, Password FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String Demail = resultSet.getString("Email");
                String Dpassword = resultSet.getString("Password");
                if (Demail.equals(email) && Dpassword.equals(password)) {
                    return true;
                }

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error reading data: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void updateAccbalance(String name, double balance) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE user SET AccountBalance = ? WHERE Name= ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setDouble(1, balance);
            statement.setString(2, name);

            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update acc balance Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double retriveAccbalance(String name) {
        double accountBalance = 0.0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Name, AccountBalance FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("Name");
                double Dbalance = resultSet.getDouble("AccountBalance");
                if (Dname.equals(name)) {
                    return Dbalance;
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return accountBalance;
    }

    public double retriveLatestBuyPrice(User user,Stock stock) {
        double price=0.0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT UserName, StockSymbol, LatestBuyingPrice FROM holdings";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("UserName");
                String Dsymbol = resultSet.getString("StockSymbol");
                double Dprice = resultSet.getDouble("LatestBuyingPrice");
                if (Dsymbol.equals(stock.getSymbol())&& Dname.equals(user.getName())) {
                    return Dprice;
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return price;
    }
    public double retriveProfitLoss(String name) {
        double profits = 0.0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Name, TotalProfitLoss FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("Name");
                double Dprofit = resultSet.getDouble("TotalProfitLoss");
                if (Dname.equals(name)) {
                    return Dprofit;
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return profits;
    }
    public void updateProfitLoss(String name,double profit) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE user SET TotalProfitLoss = ? WHERE Name = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setDouble(1, profit);
            statement.setString(2, name);
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update profit Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateHoldings(String name, String symbol, int shares,double price) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE holdings SET Shares = ?, LatestBuyingPrice= ? WHERE UserName = ? AND Stocksymbol = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setInt(1, shares);
            statement.setDouble(2,price);
            statement.setString(3, name);
            statement.setString(4, symbol);
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update holding Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateHoldings(String name, String symbol, int shares) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE holdings SET Shares = ? WHERE UserName = ? AND Stocksymbol = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setInt(1, shares);
            statement.setString(2, name);
            statement.setString(3, symbol);
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update holding Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertHoldings(String name, String symbol, int shares,double price) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO holdings (UserName, Stocksymbol, Shares, LatestBuyingPrice) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the parameters in the prepared statement
            statement.setString(1, name);
            statement.setString(2, symbol);
            statement.setInt(3, shares);
            statement.setDouble(4,price);


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            System.out.println("insert holding Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public User retriveUser(String email) {
        User user = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Name, Email, Password ,Status FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("Name");
                String Demail = resultSet.getString("Email");
                String Dpassword = resultSet.getString("Password");
                String Dstatus = resultSet.getString("Status");
                if (Demail.equals(email)) {
                    user = new User(Dname, Demail, Dpassword, Dstatus);
                }

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return user;

    }
    //edited
    // will also retrieve the balance of user
    public List retriveUserList(){
        User user= null;
        List<User> userList= new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Name, Email, Password ,Status,AccountBalance FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("Name");
                String Demail = resultSet.getString("Email");
                String Dpassword=resultSet.getString("Password");
                String Dstatus= resultSet.getString("Status");
                double Dbalance=resultSet.getDouble("AccountBalance");

                user= new User(Dname,Demail,Dpassword,Dstatus,Dbalance);
                userList.add(user);
            }


            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return userList;

    }
    public void updateStatus(String status, String name) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE user SET Status = ? WHERE Name= ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setString(1, status);
            statement.setString(2, name);

            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update user qualification status Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public boolean readHoldings(String name,String symbol) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT UserName, Stocksymbol, Shares FROM holdings";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("UserName");
                String Dsymbol = resultSet.getString("Stocksymbol");

                if (Dname.equals(name)&&Dsymbol.equals(symbol)) {
                    return true;
                }

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error reading data: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public Map<Stock, Integer> retriveHoldings(User user) {
        Map<Stock, Integer> holdings = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT UserName, Stocksymbol, Shares FROM holdings";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String name = resultSet.getString("UserName");
                String symbol = resultSet.getString("Stocksymbol");
                int shares = resultSet.getInt("Shares");
                Stock stock = new Stock(symbol);
                // Store the data in the map
                if (name.equals(user.getName())) {
                    holdings.put(stock, shares);
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return holdings;

    }

    //for gui purpose
    public List<Order> retriveHoldingsForGui(User user) {
        List<Order> holdings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT UserName, Stocksymbol, Shares , LatestBuyingPrice FROM holdings";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            while (resultSet.next()) {
                String name = resultSet.getString("UserName");
                String symbol = resultSet.getString("Stocksymbol");
                Stock stock = new Stock(symbol);
                int shares = resultSet.getInt("Shares");
                double price = resultSet.getDouble("LatestBuyingPrice");
                Order order = new Order(stock, shares, price);
                // Store the data in the map
                if (name.equals(user.getName())) {
                    holdings.add(order);
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return holdings;

    }
    public void insertPendingOrder(Order order, User user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO PendingOrder (OrderID, Name, Position, OrderType, Symbol, OrderPrice, Shares, OrderTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the parameters in the prepared statement
            statement.setInt(1, order.getID());
            statement.setString(2, user.getName());
            statement.setString(3, order.getPosition().toString());
            statement.setString(4, order.getType().toString());
            statement.setString(5, order.getStock().getSymbol());
            statement.setDouble(6, order.getPrice());
            statement.setInt(7, order.getShares());
            statement.setString(8, order.getTime());


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            System.out.println("insert pending order Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Order> retrivePendingOrder(User user) {
        List<Order> pendingOrders = new ArrayList<>();
        Order order = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT OrderID, Name, OrderType, Position, Symbol, OrderPrice, Shares, OrderTime, Status FROM PendingOrder";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Did = resultSet.getInt("OrderID");
                String Dname = resultSet.getString("Name");
                String Dtype = resultSet.getString("OrderType");
                String Dposition = resultSet.getString("Position");
                String Dsymbol = resultSet.getString("Symbol");
                double Dprice = resultSet.getDouble("OrderPrice");
                int Dshare = resultSet.getInt("Shares");
                String Dtime = resultSet.getString("OrderTime");
                String Dstatus = resultSet.getString("Status");
                if (Dname.equals(user.getName()) && Dstatus.equalsIgnoreCase("Pending")) {
                    Stock stock = new Stock(Dsymbol);
                    Order.Position position = getPositionFromString(Dposition);
                    Order.Type type = getTypeFromString(Dtype);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(Dtime, formatter);
                    order = new Order(Did,Dname ,stock, type, position, Dshare, Dprice, dateTime);
                    pendingOrders.add(order);

                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return pendingOrders;

    }

    public List<Order> retriveBuyPendingOrder(User user) {
        List<Order> buypendingOrders = new ArrayList<>();
        Order order = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT OrderID, Name, OrderType, Position, Symbol, OrderPrice, Shares, OrderTime, Status FROM PendingOrder";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Did = resultSet.getInt("OrderID");
                String Dname = resultSet.getString("Name");
                String Dtype = resultSet.getString("OrderType");
                String Dposition = resultSet.getString("Position");
                String Dsymbol = resultSet.getString("Symbol");
                double Dprice = resultSet.getDouble("OrderPrice");
                int Dshare = resultSet.getInt("Shares");
                String Dtime = resultSet.getString("OrderTime");
                String Dstatus = resultSet.getString("Status");
                if (!Dname.equals(user.getName()) && Dtype.equalsIgnoreCase("Buy") && Dstatus.equalsIgnoreCase("Pending")) {
                    Stock stock = new Stock(Dsymbol);
                    Order.Position position = getPositionFromString(Dposition);
                    Order.Type type = getTypeFromString(Dtype);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(Dtime, formatter);
                    order = new Order(Did,Dname, stock, type, position, Dshare, Dprice, dateTime);
                    buypendingOrders.add(order);

                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return buypendingOrders;

    }

    public List<Order> retriveSellPendingOrder(User user) {
        List<Order> sellpendingOrders = new ArrayList<>();
        Order order = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT OrderID, Name, OrderType, Position, Symbol, OrderPrice, Shares, OrderTime, Status FROM PendingOrder";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Did = resultSet.getInt("OrderID");
                String Dname = resultSet.getString("Name");
                String Dtype = resultSet.getString("OrderType");
                String Dposition = resultSet.getString("Position");
                String Dsymbol = resultSet.getString("Symbol");
                double Dprice = resultSet.getDouble("OrderPrice");
                int Dshare = resultSet.getInt("Shares");
                String Dtime = resultSet.getString("OrderTime");
                String Dstatus = resultSet.getString("Status");
                if (!Dname.equals(user.getName()) && Dtype.equalsIgnoreCase("Sell") && Dstatus.equalsIgnoreCase("Pending")) {
                    Stock stock = new Stock(Dsymbol);
                    Order.Position position = getPositionFromString(Dposition);
                    Order.Type type = getTypeFromString(Dtype);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(Dtime, formatter);
                    order = new Order(Did,Dname, stock, type, position, Dshare, Dprice, dateTime);
                    sellpendingOrders.add(order);

                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return sellpendingOrders;

    }


    public static Order.Position getPositionFromString(String positionString) {
        switch (positionString) {
            case "LIMIT":
                return Order.Position.LIMIT;
            case "MARKET":
                return Order.Position.MARKET;
            default:
                throw new IllegalArgumentException("Invalid position value: " + positionString);
        }
    }
    public static Order.Type getTypeFromString(String type) {
        switch (type) {
            case "BUY":
                return Order.Type.BUY;
            case "SELL":
                return Order.Type.SELL;
            default:
                throw new IllegalArgumentException("Invalid position value: " + type);
        }
    }
    public void updatePendingOrder(int orderId,String status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE PendingOrder SET Status = ? WHERE OrderID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setString(1, status);
            statement.setInt(2, orderId);
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();//can be deleted after debugging
            System.out.println("update pending Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertOrderHistory(Order order, User user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO OrderHistory (OrderID, Name, OrderPosition, OrderType, StockSymbol, OrderPrice, Shares, OrderTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the parameters in the prepared statement
            statement.setInt(1, order.getID());
            statement.setString(2, user.getName());
            statement.setString(3, order.getPosition().toString());
            statement.setString(4, order.getType().toString());
            statement.setString(5, order.getStock().getSymbol());
            statement.setDouble(6, order.getPrice());
            statement.setInt(7, order.getShares());
            statement.setString(8, order.getTime());


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            System.out.println("insert order history Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateOrderHistory(int orderId, String status) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE OrderHistory SET Status = ? WHERE OrderID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setString(1, status);
            statement.setInt(2, orderId);
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();//can be deleted after debugging
            System.out.println("update order history Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Order> retriveOrderHistory(User user) {
        List<Order> orderHistory = new ArrayList<>();
        Order order = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT OrderID, Name, OrderPosition, OrderType, StockSymbol, OrderPrice, Shares, OrderTime, Status FROM OrderHistory";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Did = resultSet.getInt("OrderID");
                String Dname = resultSet.getString("Name");
                String Dposition = resultSet.getString("OrderPosition");
                String Dtype = resultSet.getString("OrderType");
                String Dsymbol = resultSet.getString("StockSymbol");
                double Dprice = resultSet.getDouble("OrderPrice");
                int Dshare = resultSet.getInt("Shares");
                String Dtime = resultSet.getString("OrderTime");
                String Dstatus = resultSet.getString("Status");
                if (Dname.equals(user.getName())) {
                    Stock stock = new Stock(Dsymbol);
                    Order.Position position = getPositionFromString(Dposition);
                    Order.Type type = getTypeFromString(Dtype);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(Dtime, formatter);
                    order = new Order(Did, Dname,stock, type, position, Dshare, Dprice, dateTime,Dstatus);
                    orderHistory.add(order);

                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("retrive order history Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return orderHistory;

    }

    public List<Integer> retriveOrderID() {
        List<Integer> orderIDs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT OrderID FROM OrderHistory";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Did = resultSet.getInt("OrderID");
                orderIDs.add(Did);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("retrive order history Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return orderIDs;

    }

    public void delete() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "DELETE FROM holdings WHERE UserName = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setString(1, "tester");
            // Execute the update statement

            int rowsAffected = statement.executeUpdate();//can be deleted after debugging
            System.out.println("update order history Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void UpdatelotPool(Stock stock,int share){
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE LotPool SET StockShare = ? WHERE StockSymbol = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setInt(1, share);
            statement.setString(2, stock.getSymbol());
            // Execute the update statement
            int rowsAffected = statement.executeUpdate();//can be deleted after debugging
            System.out.println("update lotpool Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public void insertlotPool(String symbol){
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO LotPool (StockSymbol) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Set the values for the parameters in the prepared statement
            statement.setString(1,symbol);


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            System.out.println("insert lotpool Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Map<Stock, Integer> retriveLotPool(){
        java.util.Map<Stock, java.lang.Integer> lotpool = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT StockSymbol, StockShare FROM LotPool";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String symbol = resultSet.getString("StockSymbol");
                int share = resultSet.getInt("StockShare");
                Stock stock = new Stock(symbol);
                // Store the data in the map
                lotpool.put(stock, share);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return lotpool;

    }

    public void updateNotification(String name, boolean able) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE user SET Notification = ? WHERE Name= ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the new values for the columns
            statement.setBoolean(1, able);
            statement.setString(2, name);

            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            System.out.println("update notification Rows affected: " + rowsAffected);

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
}

}
    public boolean retriveNotification(String name) {
        boolean Dable = false;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT Name, Notification FROM user";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            // Process the result set
            while (resultSet.next()) {
                String Dname = resultSet.getString("Name");
                Dable = resultSet.getBoolean("Notification");
                if (Dname.equals(name)) {
                    return Dable;
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            e.printStackTrace();
        }
        return Dable;
}

}









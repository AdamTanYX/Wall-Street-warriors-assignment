package WallStreet;


import java.util.*;

public class CreateAccount {
    private static TradingEngine tradingEngine;
    private static User user;
    private static Database database;
    private boolean loginSuccess = false;

    public static void Register() {
        Stocklist2 stocklist = new Stocklist2();
        tradingEngine = new TradingEngine(new ArrayList<>(stocklist.fetchStockList()));
        Scanner scanner = new Scanner(System.in);
        database = new Database();
        if (tradingEngine.checkCompetitionPeriod()) {
            System.out.println("The Competition Has Ended!");
            System.exit(0);
        }
        // 1. Create a trading account
        System.out.println("Welcome to the trading competition!");
        System.out.println("Please enter your details to create a trading account.");

        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email address: ");
        String email = scanner.nextLine();

        System.out.print("Enter a password(Must be more than 8 characters including alphabets, numbers and special characters: ");
        String password = scanner.nextLine();

        // Validate the password
        while (!isValidPassword(password)) {
            System.out.println("Password is invalid!");
            System.out.print("Enter a password: ");
            password = scanner.nextLine();
        }
        System.out.println("Password is valid!");
        System.out.println("Congratulations! Your trading account has been created successfully.");
        database.insertUser(name, email, password);
    }

    public void Login() {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        Stocklist2 stocklist = new Stocklist2();
        tradingEngine = new TradingEngine(new ArrayList<>(stocklist.fetchStockList()));
        if (tradingEngine.checkCompetitionPeriod()) {
            System.out.println("The Competition Has Ended!");
            System.exit(0);
        }
        // 2. Log in to the app
        System.out.println("Please enter your account credentials to log in to the trading competition app.");
        System.out.print("Enter your email address: ");
        String loginEmail = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        boolean isMatchFound = database.readUser(loginEmail, loginPassword);


        if (isMatchFound) {
            System.out.println("\n***********Logged in successfully!***********\n");
            user = database.retriveUser(loginEmail);
            loginSuccess = true;
        }
    }

    public boolean getSuccess() {
        return loginSuccess;
    }

    public User getUser() {
        return user;
    }

    public static boolean isValidPassword(String password) {
        // Check if the password is exactly 8 characters long
        if (password.length() < 8|| password.length() > 16) {
            return false;
        }

        // Check if the password contains at least one alphabet, one number, and one special character
        boolean hasAlphabet = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasAlphabet = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSpecialChar = true;
            }
        }


        if (!hasAlphabet || !hasNumber || !hasSpecialChar) {
            return false;
        }

        return true;
    }
}



package WallStreet;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportGenerator {
    Database database = new Database();
    // Method to generate a report for a specific user in the specified format
    public static void generateReport(User user, String format) {
        try {
            // Create a new file with the user's name as the file name
            File file = new File(user.getName() + "." + format);

            switch (format) {

                case "csv":
                    generateTextReport(user, file);
                    break;
                case "pdf":
                    generatePdfReport(user, file);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }

            // Print a success message
            System.out.println("Report generated for user " + user.getName() + " in " + format + " format");

        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    private static void generateTextReport(User user, File file) throws IOException {
        Database database = new Database();
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("UserName : "+user.getName());
            writer.write("\nAccount Balance : "+database.retriveAccbalance(user.getName()));
            writer.write("\nProfit / Loss : "+user.getTotalPnL());
            writer.write("\nPoints : "+user.getPoints());
            writer.write("\nPosition : "+user.getRanking());
            writer.write("\nRate of Return : "+rorCalculate(user));



            writer.write("\n\nOrder History");
            // Write the header row
            writer.write("\n\nDate, Stock Symbol, Price, Volume, Type, Position \n");
            // Write the data for the user
            List<Order> orderHistory = (List<Order>) database.retriveOrderHistory(user);
            for (Order order : orderHistory) {
                writer.write(order.getTime().toString() + "," + order.getStock().getSymbol() + "," +
                        String.valueOf(order.getPrice()) + "," + String.valueOf(order.getShares()) + "," +
                        String.valueOf(order.getType()) + "," + String.valueOf(order.getPosition()) + "\n");
            }
        }
    }

    private static  void generatePdfReport(User user, File file) throws IOException {
        Database database = new Database();
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        // Add a title to the document
        Paragraph title = new Paragraph("User Performance Metrics");
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        title.setTextAlignment(TextAlignment.LEFT);
        document.add(new Paragraph("UserName : " + user.getName()));
        document.add(new Paragraph("Account Balance : " + database.retriveAccbalance(user.getName())));
        document.add(new Paragraph("Profit / Loss : " + user.getTotalPnL()));
        document.add(new Paragraph("Points : " + user.getPoints()));
        document.add(new Paragraph("Position : " + user.getRanking()));
        document.add(new Paragraph("Rate of Return : " + String.valueOf(rorCalculate(user))));


        title.setTextAlignment(TextAlignment.CENTER);
        document.add(new Paragraph("\nOrder History\n"));

        List<Order> orderHistory = (List<Order>) database.retriveOrderHistory(user);

        float columnWidth[] = {200f,150f,100f,100f,100f,100f};
        // Create a table for the data
        Table table = new Table(columnWidth);
        // Add the header row
        addCellsToTable(table, "Date", "Stock Symbol", "Price", "Volume", "Type", "Position");
        // Add the data for the user
        for (Order order:orderHistory){
            addCellsToTable(table,order.getTime().toString(),order.getStock().getSymbol(),String.valueOf(order.getPrice()),String.valueOf(order.getShares()),String.valueOf(order.getType()),String.valueOf(order.getPosition()));
        }

        // Add the table to the document
        document.add(table);
        // Close the document
        document.close();
    }

    private static void addCellsToTable(Table table, String... values) {
        for (String value : values) {
            table.addCell(value);
        }
    }

    // Method to generate a report for all users in the specified format
    public static void generateReport(List<User> userList, String format) {
        try {
            // Create a new file with a timestamp as the file name
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            File file = new File("all_users_" + dateFormat.format(date) + "." + format);

            switch (format) {
                case "txt":
                case "csv":
                    generateTextReportForAllUsers(userList, file);
                    break;
                case "pdf":
                    generatePdfReportForAllUsers(userList, file);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }

            // Print a success message
            System.out.println("Report generated for all users in " + format + " format");

        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
    public static double rorCalculate(User user){
        return ((user.getAccountBalance()+user.getTotalPnL())/50000);
    }
    private static void generateTextReportForAllUsers(List<User> userList, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write the header row
            writer.write("User Name, Account Balance, Placeholder PnL, Placeholder Trades, Placeholder Winning Trades, Placeholder Losing Trades\n");
            // Write the data for each user
            for (User user : userList) {
                writer.write(user.getName() + "," + user.getAccountBalance() + ",0,0,0,0\n");
            }
        }
    }

    private static void generatePdfReportForAllUsers(List<User> userList, File file) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        // Add a title to the document
        Paragraph title = new Paragraph("Report for All Users");
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);
        // Create a table for the data
        Table table = new Table(6);
        // Add the header row
        addCellsToTable(table, "User Name", "Account Balance", "Placeholder PnL", "Placeholder Trades", "Placeholder Winning Trades", "Placeholder Losing Trades");

        // Add the data for each user
        for (User user : userList) {
            addCellsToTable(table, user.getName(), Double.toString(user.getAccountBalance()), "0", "0", "0", "0");
        }

        // Add the table to the document
        document.add(table);
        // Close the document
        document.close();
    }



}
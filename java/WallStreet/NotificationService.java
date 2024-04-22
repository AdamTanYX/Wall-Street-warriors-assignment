package WallStreet;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NotificationService {

    private User user;
    private  double initialPL;
    private static Database database;
    private static boolean enable= false;

    public void setEnable(boolean set){
        this.enable= set;
        database.updateNotification(user.getName(), set);
    }
    public void setEnable(){
        this.enable= !this.enable;
        database.updateNotification(user.getName(),this.enable);

    }

    public boolean getEnable(){
        enable = database.retriveNotification(user.getName());
        return enable;
    }
    private  int initialPosition;
    private List<Notification> notifications = new ArrayList<>();
    private List<String> enabledEmails = new ArrayList<>();
    private Map<String, Double> pAndLThresholds = new HashMap<>();
    private Timer timer;

    public void cancelTimer(){
        timer.cancel();
    }

    public  NotificationService(User users){
//        User user3 = null;
        database = new Database();
//        List<User> user1 = database.retriveUserList();
//        for(User user2:user1){
//            if(user2.getName().equals(users.getName()));
//            user3 = user2;
//            break;
//        }
        this.user=users;
        this.initialPosition=user.getRanking();
    }



    public void enableEmailNotification( ){
        enabledEmails.add(user.getEmail());
    }

    public void disableEmailNotification() {
        enabledEmails.remove(user.getEmail());
    }

    public void scheduleNotificationCheck() {
        timer = new Timer();
        timer.schedule(new NotificationTask(), 0, TimeUnit.MINUTES.toMillis(1)); // check for notifications every 1 minute
    }

    class NotificationTask extends TimerTask {

        public void run () {
            if (getEnable()) {
                //pnL diff
//                double lastestPnL= getUserPAndL(user);
//                double diff= lastestPnL-initialPL;
                if (user.getTotalPnL() >= 1000) {
                    Notification notification = new Notification(user.getEmail(), "Your PROFIT has reached the threshold of RM" + 1000
                            + "\nTotal P/L" + user.getTotalPnL());

                } else if (user.getTotalPnL() < -500) {
                    Notification notification = new Notification(user.getEmail(), "Your LOSS has crossed the threshold of RM" + 500
                            + "\nTotal P/L" + user.getTotalPnL());
                    sendNotification(user.getEmail(), notification);

                }


                int latestRanking = user.getRanking();
                if (initialPosition < latestRanking) {
                    Notification notification = new Notification(user.getEmail(), " Leaderboard position rose to  : " + latestRanking);
                    initialPosition = latestRanking;
                    sendNotification(user.getEmail(), notification);
                } else if (initialPosition > latestRanking) {
                    Notification notification = new Notification(user.getEmail(), " Leaderboard position drop to  : " + latestRanking);
                    initialPosition = latestRanking;
                    sendNotification(user.getEmail(), notification);
                }


            }
            else{
                System.out.println("Notification is turned off!");
            }

        }
    }


    public boolean sendNotification(String email,Notification notification) {
        notifications.add(notification);
        String userEmail = notification.getUserEmail();

        try {
            // set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // create email session with sender's credentials
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                }
            });

            // compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sender@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Stock Trading Notification");
            message.setText(notification.getMessage());

            // send message
            Transport.send(message);

            System.out.println("Notification sent successfully to " + user.getEmail());
            return true;
        } catch (MessagingException e) {
            System.out.println("Failed to send notification to " + user.getEmail());
            e.printStackTrace();
        }

        return false;
    }

    public void sendBuySuccessful(Order order){

        String userEmail = user.getEmail();

        if(getEnable()==true) {

            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Buy Order Completed!");
                message.setText(String.valueOf(order.toString())+"\n\n Buy Order Completed");


                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }

            return;
        }
        else{
            System.out.println("Notification is turned off!");
        }

        return;
    }

    public void sendSellSuccessful(Order order){

        String userEmail = user.getEmail();

        if (enable == true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Sell Order Completed!");
                message.setText(String.valueOf(order.toString()) + "\n\nSell Order Completed");

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Notification is turned off!");
        }

        return ;
    }

    public void sendBuyPending(Order order){

        String userEmail = user.getEmail();
        if(enable ==true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Buy Order Pending...");
                message.setText(String.valueOf(order));
                message.setText("\n\nNo orders available. Order is pending.");

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }
        }


        return ;
    }

    public void sendSellPending(Order order){

        String userEmail = user.getEmail();

        if (enable==true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Sell Order Pending...");
                message.setText(String.valueOf(order.toString())+"\n\nNo buy orders available. Order is pending.");

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }

        }
        else{
            System.out.println("Notification is turned off!");
        }

        return ;
    }

    public void sendPending(Order order){

        String userEmail = user.getEmail();

        if (enable==true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Pending Order Placed...");
                message.setText(String.valueOf(order.toString()));

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }

        }
        else{
            System.out.println("Notification is turned off!");
        }

        return ;
    }
    public void complete(Order order) {

        String userEmail = user.getEmail();

        try {
            // set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // create email session with sender's credentials
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                }
            });

            // compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sender@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Sell Order Successful");
            message.setText(String.valueOf(order));

            // send message
            Transport.send(message);

            System.out.println("Notification sent successfully to " + user.getEmail());
            return;
        } catch (MessagingException e) {
            System.out.println("Failed to send notification to " + user.getEmail());
            e.printStackTrace();
        }
    }
    public void cancelOrder(Order order){

        String userEmail = user.getEmail();

        if (enable== true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                message.setSubject("Order Canceled");
                message.setText(String.valueOf(order.toString()));

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + user.getEmail());
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + user.getEmail());
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Notification is turned off!");
        }




        return ;
    }
    public void sendFraud(List<Transaction> transactions){
        String adminEmail= "22057495@siswa.um.edu.my";
        if (enable== true){
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("22004889@siswa.um.edu.my", "pmivbzhspngulziz");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
                message.setSubject("Order Canceled");
                String  text= "";
                for (Transaction transaction: transactions){
                    text+= transaction.toString()+"\n";
                }
                message.setText(text);

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + adminEmail);
                return ;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + adminEmail);
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Notification is turned off!");
        }
    }

//    private double getUserPAndL(User user) {
//     Database database = new Database();
//      return database.retriveProfitLoss(user);
//    }

//    public static void main(String[] args) {
//        // Create NotificationService instance
//        NotificationService notificationService = new NotificationService();
//
//        // Prompt user for recipient's email address
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter recipient's email address: ");
//        String recipientEmail = scanner.nextLine();
//
//        // Configure P&L threshold for the user
//        notificationService.configurePAndLThreshold(recipientEmail, 1000.0);
//
//        // Enable email notification for the user
//        notificationService.enableEmailNotification(recipientEmail);
//
//        // Schedule notification check
//        notificationService.scheduleNotificationCheck();
//
//        // Logging statement indicating program execution
//        System.out.println("Notification service started. Checking for notifications...");
//    }
}

class Notification {
    private String userEmail;
    private String message;

    public Notification(String userEmail, String message) {
        this.userEmail = userEmail;
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getMessage() {
        return message;
}
}
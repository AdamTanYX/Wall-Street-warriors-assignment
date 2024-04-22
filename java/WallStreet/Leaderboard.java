package WallStreet;

import java.util.*;

public class Leaderboard {
    private List<User> users;

    public Leaderboard() {
        Database db = new Database();
        users = db.retriveUserList();
        List<User> usersToRemove = new ArrayList<>();

        for (User user : users) {
            if (user.getName().equalsIgnoreCase("Admin")) {
                usersToRemove.add(user);
            }
        }

        users.removeAll(usersToRemove);

    }

    public void displayLeaderboard() {
        // Sort the users based on their points in descending order
        Collections.sort(users, new PointsComparator().reversed());

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                    Leaderboard                    ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");

        int rank = 1;
        System.out.println("║ Rank                  "  +"Points: "+ "                    ║");
        for (int i = 0; i < Math.min(users.size(), 10); i++) {
            User user = users.get(i);
            String formattedPoints = String.format("%.2f", user.getPoints());
            System.out.println("║ " + rank + ": " + padRight(user.getName(), 20) +   formattedPoints+"                       ║");
            rank++;
        }

        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    // Helper method to pad the string with spaces on the right side
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }


    private static class PointsComparator implements Comparator<User> {
        @Override
        public int compare(User user2, User user1) {
            return Double.compare(user2.getPoints(), user1.getPoints());
        }
    }

    public static void main(String[] args) {
        Leaderboard lb = new Leaderboard();
        lb.displayLeaderboard();
    }


}



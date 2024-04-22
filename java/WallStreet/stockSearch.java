package WallStreet;

import java.util.PriorityQueue;

//this calss is used for GUI
public class stockSearch {
    private Stocklist2 stocklist2;
    private PriorityQueue<Stock> stocks;
    private static final int MIN_SIMILARITY_THRESHOLD = 2;

    public stockSearch() {
        stocklist2 = new Stocklist2();
        stocks = stocklist2.fetchStockList();
    }

    public String searchStocks(String searchTerm) {
        String symbol = null;
        int maxSimilarity = Integer.MIN_VALUE;

        while (!stocks.isEmpty()) {
            Stock stock = stocks.poll();
            int similarity = calculateSimilarity(stock.getName(), searchTerm) + calculateSimilarity(stock.getSymbol(), searchTerm);
            if (similarity > maxSimilarity) {
                symbol = stock.getSymbol();
                maxSimilarity = similarity;
            }
        }

        if (symbol == null || maxSimilarity < MIN_SIMILARITY_THRESHOLD) {
            symbol = null; // Set symbol to null if not found or similarity is low
        }

        return symbol;
    }

    private int calculateSimilarity(String text, String searchTerm) {
        int textLength = text.length();
        int termLength = searchTerm.length();

        int maxSubstringLength = Math.min(textLength, termLength);
        int similarity = 0;

        for (int i = 0; i <= textLength - maxSubstringLength; i++) {
            int substringSimilarity = 0;
            for (int j = 0; j < maxSubstringLength; j++) {
                if (text.charAt(i + j) == searchTerm.charAt(j)) {
                    substringSimilarity++;
                }
            }
            similarity = Math.max(similarity, substringSimilarity);
        }

        return similarity;
    }

    public static void main(String[] args) {
        stockSearch search = new stockSearch();
        String searchTerm = "genting"; // Replace with the actual search term
        String symbol = search.searchStocks(searchTerm);
        if (symbol != null) {
            System.out.println("Most similar symbol: " + symbol);
        } else {
            System.out.println("No similar symbol found.");
        }
    }
}

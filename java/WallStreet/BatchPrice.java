package WallStreet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class BatchPrice {


//    public BatchPrice(){
//        this.fetchData(this.symbolGetter(),"Close");
//        this.getStocks();
//
//    }




    public List<String> getStocksNotAvailable() {
        return stocksNotAvailable;
    }

   private List<String> stocksNotAvailable = new ArrayList<>();

    public List<AStock> getStocklist() {
        return stocks;
    }

    private List<AStock> stocks  = new ArrayList<>();;
  private  Stocklist2 stocklist=new Stocklist2();
   private PriorityQueue<Stock> retrievedListStockList= stocklist.fetchStockList();
   private HashMap<String, PriorityQueue<ComparableDataByDate>> priceMap   = new HashMap<>();


    public HashMap<String, PriorityQueue<ComparableDataByDate>> getPriceMap(){
        return priceMap;
    }

    public boolean fetchData(List<String> symbols, String dataWant ) {
        ExecutorService executor = Executors.newFixedThreadPool(symbols.size());
        List<Future<Boolean>> futures;

        try {
            futures = executor.invokeAll(symbols.stream()
                    .map(symbol -> (Callable<Boolean>)() -> fetchSymbolData(symbol, dataWant))
                    .collect(Collectors.toList()));

            for (Future<Boolean> future : futures) {
                if (!future.get()) {
                    // Handle any failed fetches
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return true; // or false based on your requirements
    }

//    private boolean fetchSymbolData(String symbol, String dataWant) {
//        try {
//            PriorityQueue<ComparableDataByDate> closeDate = new PriorityQueue<>(Comparator.reverseOrder());
//            String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
//            String baseURL = "https://wall-street-warriors-api-um.vercel.app/";
//            String whatToFind = "price";
//            String symbolUrl = "&symbol=" + symbol;
//            URL url = new URL(baseURL + whatToFind + apiKey + symbolUrl);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.connect();
//            int responseCode = conn.getResponseCode();
//
//            if (responseCode == 200) {
//                StringBuilder informationString = new StringBuilder();
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    informationString.append(line);
//                }
//                br.close();
//
//                String jsonString = informationString.toString();
//                JSONObject json = new JSONObject(jsonString);
//                JSONObject symbolJson = json.optJSONObject(symbol);
//
//                if (symbolJson != null) {
//                    JSONObject dataJson = symbolJson.optJSONObject(dataWant);
//                    if (dataJson != null) {
//                        for (Object time : dataJson.keySet()) {
//                            double value = dataJson.getDouble(time.toString());
//                            EpochTimestampConverter eptc = new EpochTimestampConverter(time);
//                            closeDate.offer(new ComparableDataByDate(eptc.convertToDate(), value));
//                        }
//                        synchronized (priceMap) {
//                            priceMap.put(symbol, closeDate);
//                        }
//
//
//                    } else {
//                       System.out.println("Data not found for key: " + dataWant);
//                        return false;
//                    }
//                    return true;
//                } else {
//                    stocksNotAvai.add(symbol);
//                   // System.out.println("Symbol not found: " + symbol);
//                    return false;
//                }
//            } else {
//                throw new RuntimeException("HttpResponseCode: " + responseCode);
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (ProtocolException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private boolean fetchSymbolData(String symbol, String dataWant) {
        int maxRetries = 3; // Maximum number of retries
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                PriorityQueue<ComparableDataByDate> closeDate = new PriorityQueue<>(Comparator.reverseOrder());
                String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
                String baseURL = "https://wall-street-warriors-api-um.vercel.app/";
                String whatToFind = "price";
                String symbolUrl = "&symbol=" + symbol;
                URL url = new URL(baseURL + whatToFind + apiKey + symbolUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int responseCode = conn.getResponseCode();


                if (responseCode == 200) {
                    StringBuilder informationString = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        informationString.append(line);
                    }
                    br.close();

                    String jsonString = informationString.toString();
                    JSONObject json = new JSONObject(jsonString);
                    JSONObject symbolJson = json.optJSONObject(symbol);

                    if (symbolJson != null) {
                        JSONObject dataJson = symbolJson.optJSONObject(dataWant);
                        if (dataJson != null) {
                            for (Object time : dataJson.keySet()) {
                                double value = dataJson.getDouble(time.toString());
                                EpochTimestampConverter eptc = new EpochTimestampConverter(time);
                                closeDate.offer(new ComparableDataByDate(eptc.convertToDate(), value));
                            }
                            synchronized (priceMap) {
                                priceMap.put(symbol, closeDate);
                            }


                        } else {
                            System.out.println("Data not found for key: " + dataWant);
                            return false;
                        }
                        return true;
                    } else {
                        stocksNotAvailable.add(symbol);
                        // System.out.println("Symbol not found: " + symbol);
                        return false;
                    }

                } else if (responseCode == 429) {
                    System.out.println("Too Many Requests. Retrying in 5 seconds...");
                    Thread.sleep(3000); // Wait for 5 seconds before retrying
                    retryCount++;
                } else {
                    throw new RuntimeException("HttpResponseCode: " + responseCode);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
                retryCount++;
            }
        }

        System.out.println("Exceeded maximum number of retries for symbol: " + symbol);
        return false;
    }


    public ArrayList<String> symbolGetter(){
        ArrayList<String> temp= new ArrayList<>();
        PriorityQueue<Stock> stocklist = retrievedListStockList;
        while(stocklist.peek()!=null){
            temp.add(stocklist.peek().getSymbol());
            stocklist.poll();
        }
        return temp;
    }
    public void getStocks() {
        PriorityQueue<Stock> stockList = new PriorityQueue<>(retrievedListStockList);

        while (stockList.peek() != null) {
            Stock ss = stockList.peek();
            String symbol = ss.getSymbol();
            String name = ss.getName();

            // Retrieve the price data from the priceMap using the symbol
            double price = 0.0;
            PriorityQueue<ComparableDataByDate> priceDataQueue = priceMap.get(symbol);
            if (priceDataQueue != null && !priceDataQueue.isEmpty()&&priceDataQueue.peek().getData()!=0) {

                price = priceDataQueue.peek().getData();
                while (Double.isNaN(price)){
                    priceDataQueue.poll();
                    price = priceDataQueue.peek().getData();
                }
                if (!Double.isNaN(price)){
                    this.stocks.add(new AStock(symbol, name, price));
                }

            }

            stockList.poll();
        }
    }

    public List<AStock> getUnavailableStock(){
        PriorityQueue<Stock> stockList = new PriorityQueue<>(retrievedListStockList);
        List<AStock> list = new ArrayList<>();
        while(stockList.peek()!=null){
            if (this.stocksNotAvailable.contains(stockList.peek().getSymbol())){
                    list.add(new AStock(stockList.peek().getSymbol(),stockList.peek().getName()));
            }
        }
        return list;
    }
}

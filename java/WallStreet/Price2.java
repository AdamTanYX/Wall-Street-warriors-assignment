package WallStreet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Price2 {
    private String symbol;
    private String symbolUrl;

    private PriorityQueue<ComparableDataByDate> openDate;
    private PriorityQueue<ComparableDataByDate> highDate;
    private PriorityQueue<ComparableDataByDate> lowDate;
    private PriorityQueue<ComparableDataByDate> closeDate;
    private PriorityQueue<ComparableDataByDate> volumeDate;
    private PriorityQueue<ComparableDataByDate> dividendsDate;
    private PriorityQueue<ComparableDataByDate> ssDate;

    private PriorityQueue<ComparableData> openData;
    private PriorityQueue<ComparableData> highData;
    private PriorityQueue<ComparableData> lowData;
    private PriorityQueue<ComparableData> closeData;
    private PriorityQueue<ComparableData> volumeData;
    private PriorityQueue<ComparableData> dividendsData;
    private PriorityQueue<ComparableData> ssData;
    Stocklist2 stocklist = new Stocklist2();
    PriorityQueue<Stock> stockList = stocklist.fetchStockList();
    Order order;
    public Price2() {
        openDate = new PriorityQueue<>(Comparator.reverseOrder());
        highDate = new PriorityQueue<>(Comparator.reverseOrder());
        lowDate = new PriorityQueue<>(Comparator.reverseOrder());
        closeDate = new PriorityQueue<>(Comparator.reverseOrder());
        volumeDate = new PriorityQueue<>(Comparator.reverseOrder());
        dividendsDate = new PriorityQueue<>(Comparator.reverseOrder());
        ssDate = new PriorityQueue<>(Comparator.reverseOrder());

        openData = new PriorityQueue<>();
        highData = new PriorityQueue<>(Comparator.reverseOrder());
        lowData = new PriorityQueue<>();
        closeData = new PriorityQueue<>();
        volumeData = new PriorityQueue<>();
        dividendsData = new PriorityQueue<>();
        ssData = new PriorityQueue<>();
         order = new Order();

    }

    public boolean fetchData(String symbol) {
        this.symbol = symbol;
        symbolUrl = "&symbol=" + symbol;

        clearQueues();

        try {
            // API URL
            String apiKey = "?apikey=UM-5db9edd90046daabace072a7d8f3a954b799f1cf2a87d04c50cd60eff8d7b846";
            String baseURL = "https://wall-street-warriors-api-um.vercel.app/";
            String whatToFind = "price";
            String days="&days=1";
            URL url = new URL(baseURL + whatToFind + apiKey + symbolUrl+days);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    informationString.append(line);
                }
                br.close();

                try {
                    String jsonString = informationString.toString();
                    JSONObject json = new JSONObject(jsonString);
                    JSONObject symbolJson = json.optJSONObject(symbol);
                    if (symbolJson != null) {
                    getData("Open", openDate, openData, jsonString);
                    getData("High", highDate, highData, jsonString);
                    getData("Low", lowDate, lowData, jsonString);
                    getData("Close", closeDate, closeData, jsonString);
                    getData("Volume", volumeDate, volumeData, jsonString);
                    getData("Dividends", dividendsDate, dividendsData, jsonString);
                    getData("Stock Splits", ssDate, ssData, jsonString);
                        return true;
                    }
                    else {
//                        System.out.println("Price not available for " + symbol);
                    }
                   return false;
                    // Print other data queues...
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getData(String dataWant, PriorityQueue<ComparableDataByDate> date, PriorityQueue<ComparableData> data, String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        JSONObject symbolJson = json.optJSONObject(symbol);
        if (symbolJson != null) {
            JSONObject dataJson = symbolJson.optJSONObject(dataWant);
            if (dataJson != null) {
                for (Object time : dataJson.keySet()) {
                    double value = dataJson.getDouble(time.toString());
                    EpochTimestampConverter eptc = new EpochTimestampConverter(time);
                    date.offer(new ComparableDataByDate(eptc.convertToDate(), value));
                    data.offer(new ComparableData(eptc.convertToDate(), value));
                }
            } else {
                System.out.println("Data not found for key: " + dataWant);
            }
        } else {
            System.out.println("Symbol not found: " + symbol);
        }
    }




//    private void getData(String dataWant, PriorityQueue<ComparableDataByDate> date, PriorityQueue<ComparableData> data, String jsonString) {
//        JSONObject json = new JSONObject(jsonString);
//        JSONObject jsonObj = json.getJSONObject(symbol).getJSONObject(dataWant);
//        for (Object time : jsonObj.keySet()) {
//            double value = jsonObj.getDouble(time.toString());
//            EpochTimestampConverter eptc = new EpochTimestampConverter(time);
//            date.offer(new ComparableDataByDate(eptc.convertToDate(), value));
//            data.offer(new ComparableData(eptc.convertToDate(), value));
//        }
//    }


    public <E> void printQueue(PriorityQueue<E> queue) {
        PriorityQueue<E> tempQueue = new PriorityQueue<>(queue);
        while (!tempQueue.isEmpty()) {
            E element = tempQueue.poll();
            System.out.println(element);
        }
    }

    public void clearQueues() {
        openDate.clear();
        highDate.clear();
        lowDate.clear();
        closeDate.clear();
        volumeDate.clear();
        dividendsDate.clear();
        ssDate.clear();

        openData.clear();
        highData.clear();
        lowData.clear();
        closeData.clear();
        volumeData.clear();
        dividendsData.clear();
        ssData.clear();
    }

    public PriorityQueue<ComparableDataByDate> getOpenDate() {
        return openDate;
    }

    public PriorityQueue<ComparableDataByDate> getHighDate() {
        return highDate;
    }

    public PriorityQueue<ComparableDataByDate> getLowDate() {
        return lowDate;
    }

    public PriorityQueue<ComparableDataByDate> getCloseDate() {
        return closeDate;
    }

    public PriorityQueue<ComparableDataByDate> getVolumeDate() {
        return volumeDate;
    }

    public PriorityQueue<ComparableDataByDate> getDividendsDate() {
        return dividendsDate;
    }

    public PriorityQueue<ComparableDataByDate> getSsDate() {
        return ssDate;
    }

    public PriorityQueue<ComparableData> getOpenData() {
        return openData;
    }

    public PriorityQueue<ComparableData> getHighData() {
        return highData;
    }

    public PriorityQueue<ComparableData> getLowData() {
        return lowData;
    }

    public PriorityQueue<ComparableData> getCloseData() {
        return closeData;
    }

    public PriorityQueue<ComparableData> getVolumeData() {
        return volumeData;
    }

    public PriorityQueue<ComparableData> getDividendsData() {
        return dividendsData;
    }

    public PriorityQueue<ComparableData> getSsData() {
        return ssData;
    }

    public double getPrices() {

        Stock targetStock = null;
        for (Stock stock : stockList) {
            if (stock.getSymbol().equals(this.symbol)) {
                targetStock = stock;
                break;
            }
        }
        if (targetStock != null) {
            // Fetch the price data for the target stock
            // fetchData(targetStock.getSymbol());
            // Retrieve the close price for the target stock
            if(getCloseDate().peek()!=null){
                double targetStockPrice = getCloseDate().peek().getData();
                return targetStockPrice;
            }
            else return 0;

        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Symbol: ");
        String symbol = sc.nextLine();

        Price2 price2 = new Price2();
        price2.fetchData(symbol);

        price2.printQueue(price2.volumeDate);
    }
}

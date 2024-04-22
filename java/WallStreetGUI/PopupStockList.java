package WallStreetGUI;

import WallStreet.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class PopupStockList {

    User user;

    BatchPrice batchPrice;
    Stocklist2  stocklist2 = new Stocklist2();
    PriorityQueue<Stock> stockList = new PriorityQueue<>(stocklist2.fetchStockList());

    List<String> unvailable= new ArrayList<>();


    public  PopupStockList(){
        stocklist2 = new Stocklist2();
        batchPrice=new BatchPrice();
        batchPrice.fetchData(batchPrice.symbolGetter(),"Close");
        batchPrice.getStocks();
        this.priceMap=batchPrice.getPriceMap();
        getStocks(stockList);
        unvailable.addAll(batchPrice.getStocksNotAvailable());
    }


    public ArrayList<AStock> stocks = new ArrayList<AStock>();
    public HashMap<String, PriorityQueue<ComparableDataByDate>> priceMap;
    public void showAStock() {
        // Create a new stage for the pop-up window
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.NONE); // Change the modality to NONE for interaction with other windows
        popupStage.initStyle(StageStyle.DECORATED); // Set the stage style to enable minimize button
        popupStage.setTitle("Stock List");


        // Create a TabPane
        TabPane tabPane = new TabPane();

        // Create the first tab
        Tab tab1 = new Tab("Available Stocks");
        tab1.setContent(availableStock());
        tabPane.getTabs().add(tab1);

        // Create the second tab
        Tab tab2 = new Tab("Unavailable Stocks");
        tab2.setContent(unavailableStock());
        tabPane.getTabs().add(tab2);

        // Create the layout for the pop-up window
        StackPane layout = new StackPane();
        layout.getChildren().add(tabPane);

        // Create the scene and set it on the stage
        Scene scene = new Scene(layout, 400, 600);
        popupStage.setScene(scene);

        // Show the pop-up window
        popupStage.show();
    }

    private TableView<AStock> availableStock() {
        // Create a table with one column
        TableView<AStock> table = new TableView<>();
        TableColumn<AStock,String> symbol= new TableColumn<>("symbol");
        TableColumn<AStock,String> name = new TableColumn<>("name");
        TableColumn<AStock,Double> price= new TableColumn<>("price");

        symbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));


        table.getColumns().addAll(symbol,name,price);
        ObservableList<AStock>   list = FXCollections.observableArrayList(stocks);
        table.setItems(list);

        return table;
    }

    private TableView<String> unavailableStock() {
        // Create a table with one column
        TableView<String> table = new TableView<>();
        TableColumn<String, String> symbol = new TableColumn<>("symbol");

        symbol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        table.getColumns().add(symbol);
        ObservableList<String> list = FXCollections.observableArrayList(unvailable);
        table.setItems(list);

        return table;
    }


    public void getStocks(PriorityQueue<Stock> retrievedListStockList) {
        PriorityQueue<Stock> stockList = new PriorityQueue<>(retrievedListStockList);

        while (!stockList.isEmpty()) {
            Stock stock = stockList.poll();
            String symbol = stock.getSymbol();
            String name=stock.getName();
            PriorityQueue<ComparableDataByDate> priceDataQueue = priceMap.get(symbol);
            if (priceDataQueue != null && !priceDataQueue.isEmpty()) {
                double price = retrieveNonZeroPrice(priceDataQueue);
                if (!Double.isNaN(price)) {
                    this.stocks.add(new AStock(symbol, name,price));
                }
                else{
                    unvailable.add(symbol);
                }
            }

        }
    }

    private double retrieveNonZeroPrice(PriorityQueue<ComparableDataByDate> priceDataQueue) {
        double price = Double.NaN;
        while (priceDataQueue.peek() != null) {
            price = priceDataQueue.poll().getData();
            if (price != 0.0 && !Double.isNaN(price)) {
                break;
            }
        }
        return price;
    }

    }







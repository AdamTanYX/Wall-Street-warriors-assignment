package WallStreetGUI;


import WallStreet.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class ssController implements Initializable {
 private Database database = new Database();


private NotificationService notificationService;

public void setNotificationService(NotificationService notificationService){
 this.notificationService=notificationService;
}
 private User user;

 public void setUser(User user) {
  this.user = user;
 }

 private PortFolio portfolio;

 public void setPortfolio(PortFolio portfolio) {
  this.portfolio = portfolio;
 }

 private Stage stage;
 private Scene scene;
 private Parent root;
 public javafx.scene.layout.HBox HBox;
 String symbol;
 @FXML
 private ChoiceBox<String> price_select;

 private String[] position = {"Market","Limit"};



 @FXML
 private Button SS_button;

 @FXML
 private Button admin_button;

 @FXML
 private ImageView admin_image;

 @FXML
 private TextField buyPrice_field;

 @FXML
 private Label buyPrice_label;

 @FXML
 private Pane buyPrice_pane;

 @FXML
 private TextField buyVolume_field;

 @FXML
 private Label buyVolume_label;

 @FXML
 private Pane buyVolume_pane;

 @FXML
 private Button buy_button;

 @FXML
 private Tab buy_tab;

 @FXML
 private Label companyName_field;

 @FXML
 private Label companyName_label;

 @FXML
 private Pane companyName_pane;

 @FXML
 private Label currentPrice_change;

 @FXML
 private Pane currentPrice_pane;

 @FXML
 private Label currentPrice_panel;

 @FXML
 private Button dashboard_button;

 @FXML
 private ImageView dashboard_image;

 @FXML
 private AnchorPane details_pane;

 @FXML
 private Label eastTotal_pane2;

 @FXML
 private Label estTotal_label;

 @FXML
 private Pane estTotal_pane;

 @FXML
 private AreaChart<String, Number> graph;

 @FXML
 private Button graph_1d;

 @FXML
 private Button graph_1m;

 @FXML
 private Button graph_2m;

 @FXML
 private Button graph_7d;

 @FXML
 private Pane graph_pane;

 @FXML
 private Label high_label;

 @FXML
 private Pane high_pane;

 @FXML
 private Label high_panel;

 @FXML
 private Button leaderboard_button;

 @FXML
 private ImageView leaderboard_image;

 @FXML
 private Label low_label;

 @FXML
 private Pane low_pane;

 @FXML
 private Label low_panel;

 @FXML
 private Label open_label;

 @FXML
 private Label open_panel;

 @FXML
 private Label order_label;

 @FXML
 private ListView<?> order_listview;

 @FXML
 private AnchorPane order_main;

 @FXML
 private ScrollPane order_scrollpane;

 @FXML
 private AnchorPane order_subpane;



 @FXML
 private Button refresh_button;

 @FXML
 private Button report_button;

 @FXML
 private ImageView report_image;

 @FXML
 private ImageView searchBox_image;

 @FXML
 private AnchorPane search_box;

 @FXML
 private AnchorPane searchfield;

 @FXML
 private TextField sellPrice_field;

 @FXML
 private Label sellPrice_label;

 @FXML
 private Pane sellPrice_pane;

 @FXML
 private ChoiceBox<String> sellPrice_select;

 @FXML
 private TextField sellVolume_field;

 @FXML
 private Label sellVolume_label;

 @FXML
 private Pane sellVolume_pane;

 @FXML
 private Button sell_button;

 @FXML
 private Tab sell_tab;

 @FXML
 private AnchorPane side_ankerpane;

 @FXML
 private VBox side_vbox;

 @FXML
 private ImageView ss_image;

 @FXML
 private TextField stockSearch_field;

 @FXML
 private Label trade_label;

 @FXML
 private AnchorPane trade_pane;

 @FXML
 private TabPane trade_tab;

 @FXML
 private Label volume_label;

 @FXML
 private Pane volume_pane;

 @FXML
 private Label volume_panel;

 @FXML
 private CategoryAxis x_axis;

 @FXML
 private NumberAxis y_axis;

 @FXML
 public TextArea trade_status;

 @FXML
 private ChoiceBox<String> pOrder_selector;
 @FXML
 private TableView<Order> pOrder_table;
 @FXML
 private TableColumn<Order, LocalDateTime> date_column;
 @FXML
 private TableColumn<Order, Order.Type> type_column;
 @FXML
 private TableColumn<Order, Integer> volume_column;
 @FXML
 private TableColumn<Order, Stock> symbol_column;
 @FXML
 private TableColumn<Order, Double> price_column;
 @FXML
 private TableColumn<Order, Void> cancel_column;
 @FXML
 private Label BuysuggestedPrice;
 @FXML
 private Label SellsuggestedPrice;
 @FXML
 private Label companySymbol_label;
 @FXML
 private Button details_button;
 @FXML
 private Button technicalGraph_button;

 @FXML
 private Button lot100_button;


 @FXML
 private Button news_button;
 @FXML
 private Label holdings_label;
 @FXML
 private TableView<Order> holdings_table;
 @FXML
 private TableColumn<Order, Double> hBuyPrice_column;
 @FXML
 private TableColumn<Order, String> hSymbol_column;
 @FXML
 private TableColumn<Order, Integer> hVolume_column;
 @FXML
 private Button hRefresh_button;

 @FXML
 private Button checkPending_button;
 @FXML
 void switchtoSS(ActionEvent event) {
    }

public void start(Stage stage){
  this.stage=stage;
  initializeStage();
}

 public void button_1d(ActionEvent actionEvent) {

  graph.getData().clear();
  graphData data = new graphData("1");
  data.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue = data.getCloseDate();
  XYChart.Series<String, Number> series = new XYChart.Series<>();

  while (!queue.isEmpty()) {
   series.getData().add(new XYChart.Data<>(queue.peek().getDate(), queue.peek().getData()));
   queue.poll();
  }

  graph.getData().add(series);

  double minY = Double.POSITIVE_INFINITY;
  double maxY = Double.NEGATIVE_INFINITY;

  for (XYChart.Data<String, Number> dataPoint : series.getData()) {
   double yValue = dataPoint.getYValue().doubleValue();
   if (yValue < minY) {
    minY = yValue;
   }
   if (yValue > maxY) {
    maxY = yValue;
   }
  }

  y_axis.setAutoRanging(false);
  y_axis.setLowerBound(minY-0.15);
  y_axis.setUpperBound(maxY+0.1);

  graphData data2 = new graphData("1");
  PriorityQueue<ComparableDataByDate> queue2= data2.getCloseDate();
  data2.fetchData(symbol);
  double initialPrice= queue2.peek().getData();
//  for (int i=0;i<queue2.size()-1;i++){
//   queue2.poll();
//  }
//  double finalPrice=queue2.peek().getData();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue3 = price2.getCloseDate();
  double finalPrice= queue3.peek().getData();
  DecimalFormat decimalFormat = new DecimalFormat("#.###");
  decimalFormat.setGroupingUsed(false);
  double priceChange= Double.parseDouble(decimalFormat.format(finalPrice-initialPrice));
  double  percentChange = Double.parseDouble(decimalFormat.format(priceChange/initialPrice*100));
  if (priceChange>0){
   currentPrice_change.setText("+"+priceChange+"("+percentChange+"%)"+" past 1 day");
   currentPrice_change.setTextFill(Color.GREEN);
  }
  else{
   currentPrice_change.setText(priceChange+"("+percentChange+"%)"+" past 1 day ");
   currentPrice_change.setTextFill(Color.RED);
  }

 }


 public void button_7d(ActionEvent actionEvent){
   graph.getData().clear();
   graphData data= new graphData("7");
   data.fetchData(symbol);
   PriorityQueue<ComparableDataByDate> queue = data.getCloseDate();
  XYChart.Series<String, Number> series = new XYChart.Series<>();

  while(!queue.isEmpty()){
    series.getData().add(new XYChart.Data(queue.peek().getDate(),queue.peek().getData()));
   queue.poll();
  }

  graph.getData().add(series);
  double minY = Double.POSITIVE_INFINITY;
  double maxY = Double.NEGATIVE_INFINITY;

  for (XYChart.Data<String, Number> dataPoint : series.getData()) {
   double yValue = dataPoint.getYValue().doubleValue();
   if (yValue < minY) {
    minY = yValue;
   }
   if (yValue > maxY) {
    maxY = yValue;
   }
  }

  y_axis.setAutoRanging(false);
  y_axis.setLowerBound(minY-0.15);
  y_axis.setUpperBound(maxY+0.1);

  graphData data2 = new graphData("7");
  PriorityQueue<ComparableDataByDate> queue2= data2.getCloseDate();
  data2.fetchData(symbol);
  double initialPrice= queue2.peek().getData();
  //  for (int i=0;i<queue2.size()-1;i++){
//   queue2.poll();
//  }
//  double finalPrice=queue2.peek().getData();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue3 = price2.getCloseDate();
  double finalPrice= queue3.peek().getData();
  DecimalFormat decimalFormat = new DecimalFormat("#.###");
  decimalFormat.setGroupingUsed(false);
  double priceChange= Double.parseDouble(decimalFormat.format(finalPrice-initialPrice));
  double  percentChange = Double.parseDouble(decimalFormat.format(priceChange/initialPrice*100));
  if (priceChange>0){
   currentPrice_change.setText("+"+priceChange+"("+percentChange+"%)"+" past 7 days");
   currentPrice_change.setTextFill(Color.GREEN);
  }
  else{
   currentPrice_change.setText(priceChange+"("+percentChange+"%)"+" past 7 days");
   currentPrice_change.setTextFill(Color.RED);
  }

 }
 public void button_1m(ActionEvent actionEvent){
  graph.getData().clear();
  graphData data= new graphData("30");
  data.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue = data.getCloseDate();
  XYChart.Series<String, Number> series = new XYChart.Series<>();
  while(!queue.isEmpty()){
   series.getData().add(new XYChart.Data(queue.peek().getDate(),queue.peek().getData()));
   queue.poll();
  }

  graph.getData().add(series);
  double minY = Double.POSITIVE_INFINITY;
  double maxY = Double.NEGATIVE_INFINITY;

  for (XYChart.Data<String, Number> dataPoint : series.getData()) {
   double yValue = dataPoint.getYValue().doubleValue();
   if (yValue < minY) {
    minY = yValue;
   }
   if (yValue > maxY) {
    maxY = yValue;
   }
  }

  y_axis.setAutoRanging(false);
  y_axis.setLowerBound(minY-0.15);
  y_axis.setUpperBound(maxY+0.1);

  graphData data2 = new graphData("30");
  PriorityQueue<ComparableDataByDate> queue2= data2.getCloseDate();
  data2.fetchData(symbol);
  double initialPrice= queue2.peek().getData();
  //  for (int i=0;i<queue2.size()-1;i++){
//   queue2.poll();
//  }
//  double finalPrice=queue2.peek().getData();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue3 = price2.getCloseDate();
  double finalPrice= queue3.peek().getData();
  DecimalFormat decimalFormat = new DecimalFormat("#.###");
  decimalFormat.setGroupingUsed(false);
  double priceChange= Double.parseDouble(decimalFormat.format(finalPrice-initialPrice));
  double  percentChange = Double.parseDouble(decimalFormat.format(priceChange/initialPrice*100));
  if (priceChange>0){
   currentPrice_change.setText("+"+priceChange+"("+percentChange+"%)"+" past 30 days");
   currentPrice_change.setTextFill(Color.GREEN);
  }
  else{
   currentPrice_change.setText(priceChange+"("+percentChange+"%)"+" past 30 days");
   currentPrice_change.setTextFill(Color.RED);
  }
 }

 public void button_2m(ActionEvent actionEvent){
  graph.getData().clear();
  graphData data= new graphData("60");
  data.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue = data.getCloseDate();
  XYChart.Series<String, Number> series = new XYChart.Series<>();
  while(!queue.isEmpty()){
   series.getData().add(new XYChart.Data(queue.peek().getDate(),queue.peek().getData()));
   queue.poll();
  }

  graph.getData().add(series);
  double minY = Double.POSITIVE_INFINITY;
  double maxY = Double.NEGATIVE_INFINITY;

  for (XYChart.Data<String, Number> dataPoint : series .getData()) {
   double yValue = dataPoint.getYValue().doubleValue();
   if (yValue < minY) {
    minY = yValue;
   }
   if (yValue > maxY) {
    maxY = yValue;
   }
  }

  y_axis.setAutoRanging(false);
  y_axis.setLowerBound(minY-0.15);
  y_axis.setUpperBound(maxY+0.1);

  graphData data2 = new graphData("60");
  PriorityQueue<ComparableDataByDate> queue2= data2.getCloseDate();
  data2.fetchData(symbol);
  double initialPrice= queue2.peek().getData();
  //  for (int i=0;i<queue2.size()-1;i++){
//   queue2.poll();
//  }
//  double finalPrice=queue2.peek().getData();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  PriorityQueue<ComparableDataByDate> queue3 = price2.getCloseDate();
  double finalPrice= queue3.peek().getData();
  DecimalFormat decimalFormat = new DecimalFormat("#.###");
  decimalFormat.setGroupingUsed(false);
  double priceChange= Double.parseDouble(decimalFormat.format(finalPrice-initialPrice));
  double  percentChange = Double.parseDouble(decimalFormat.format(priceChange/initialPrice*100));
  if (priceChange>0){
   currentPrice_change.setText("+"+priceChange+"("+percentChange+"%)"+" past 60 days");
   currentPrice_change.setTextFill(Color.GREEN);
  }
  else{
   currentPrice_change.setText(priceChange+"("+percentChange+"%)"+" past 60 days");
   currentPrice_change.setTextFill(Color.RED);
  }

 }

 public XYChart.Data<String, Number> findClosestDataPoint(double mouseX) {
  double closestDistance = Double.MAX_VALUE;
  XYChart.Data<String, Number> closestData = null;

  for (XYChart.Series<String, Number> series : graph.getData()) {
   for (XYChart.Data<String, Number> data : series.getData()) {
    double dataX = x_axis.getDisplayPosition(data.getXValue());
    double distance = Math.abs(dataX - mouseX);

    if (distance < closestDistance) {
     closestDistance = distance;
     closestData = data;
    }
   }
  }

  return closestData;
 }


 public void displayTooltip(MouseEvent event) {
  removeTooltip(); // Remove existing tooltip if any

  double mouseX = event.getX();

  XYChart.Data<String, Number> closestData = findClosestDataPoint(mouseX);
  if (closestData != null) {
   String xValue = closestData.getXValue();
   Number yValue = closestData.getYValue();

   String message = "Time: " + xValue + "\nPrice(RM): " + yValue;

   Tooltip tooltip = new Tooltip(message);
   tooltip.setAutoHide(true);
   tooltip.show(graph, event.getScreenX() + 10, event.getScreenY() + 10);

   // Store the tooltip reference so it can be removed later
   graph.setUserData(tooltip);
  }
 }
 public void removeTooltip() {
  Object userData = graph.getUserData();
  if (userData instanceof Tooltip) {
   ((Tooltip) userData).hide();
   graph.setUserData(null);
  }
 }

// public void getName(){
//  String symbol="4715.KL";
//  String name; P
//  PriorityQueue<SS> queue= stocklist2.fetchStockList() ;
//  while(!queue.isEmpty()){
//   if (queue.peek().getSymbol().equals(symbol)){
//    name=queue.peek().getName();
//    break;
//   }
//   else{
//    queue.poll();
//   }
//
//  }
//  companyName_field.setText(name);
// }

 public void getSymbol(ActionEvent event){
      String searchFieldText=stockSearch_field.getText();
      stockSearch search= new stockSearch();
      this.symbol= search.searchStocks(searchFieldText);;

  String name="";
  Stocklist2 stocklist2= new Stocklist2();
  PriorityQueue<Stock> queue= stocklist2.fetchStockList() ;
  while(!queue.isEmpty()){
   if (queue.peek().getSymbol().equals(symbol)){
    name=queue.peek().getName();
    break;
   }
   else{
    queue.poll();
   }
  }

  Price2 price2=new Price2();
  price2.fetchData(symbol);
  double  close=price2.getCloseDate().peek().getData();
  double volume=price2.getVolumeDate().peek().getData();
  PriorityQueue<ComparableDataByDate> temp = price2.getOpenDate();
   for (int i =0;i<temp.size()-1;i++){
    temp.poll();
   }
  double open=temp.peek().getData();
  double high=price2.getHighData().peek().getData();
  double low=price2.getLowData().peek().getData();

  companySymbol_label.setText(symbol);
  companyName_field.setText(name);
  currentPrice_panel.setText(String.valueOf(close));
  BigDecimal bigDecimal = new BigDecimal(String.valueOf(volume));
  volume_panel.setText(bigDecimal.toPlainString());
  open_panel.setText(String.valueOf(open));
  high_panel.setText(String.valueOf(high));
  low_panel.setText(String.valueOf(low));

  button_1d(event);

 }
 //refresh stock price
 public void refresh(ActionEvent event){
  String name="";
  Stocklist2 stocklist2= new Stocklist2();
  PriorityQueue<Stock> queue= stocklist2.fetchStockList() ;
  while(!queue.isEmpty()){
   if (queue.peek().getSymbol().equals(symbol)){
    name=queue.peek().getName();
    break;
   }
   else{
    queue.poll();
   }
  }
  Price2 price2=new Price2();
  price2.fetchData(symbol);
  double  close=price2.getCloseDate().peek().getData();
  double volume=price2.getVolumeDate().peek().getData();
  double open=price2.getOpenDate().peek().getData();
  double high=price2.getHighDate().peek().getData();
  double low=price2.getLowDate().peek().getData();

  companySymbol_label.setText(symbol);
  companyName_field.setText(name);
  currentPrice_panel.setText(String.valueOf(close));
  volume_panel.setText(String.valueOf(volume));
  open_panel.setText(String.valueOf(open));
  high_panel.setText(String.valueOf(high));
  low_panel.setText(String.valueOf(low));

  button_1d(event);
 }

 public void switchtoDashboard(ActionEvent event){
  try {
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));

   root=loader.load();

   DashBoardController dashBoardController= loader.getController();
   dashBoardController.setUser(user);
   dashBoardController.setPortfolio(portfolio);
   dashBoardController.setNotificationService(notificationService);


   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
   scene=new Scene(root);
   stage.setScene(scene);
   dashBoardController.start(stage);
   stage.show();
  } catch (IOException e) {
   throw new RuntimeException(e);
  }

 }
 public void switchtoLeaderboard(ActionEvent event){
  try {
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/leaderboard.fxml"));

   root =loader.load();

   leaderboardController leaderboardController=loader.getController();
   leaderboardController.setUser(user);
   leaderboardController.setPortfolio(portfolio);
   leaderboardController.setNotificationService(notificationService);


   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
   scene=new Scene(root);
   stage.setScene(scene);
   leaderboardController.start(stage);
   stage.show();

  } catch (IOException e) {
   throw new RuntimeException(e);
  }

 }

 public void switchtoAdmin(ActionEvent event){
  if (!user.getEmail().equals("22004889@siswa.um.edu.my")&&!user.getEmail().equals("22057495@siswa.um.edu.my")){
   showEntryDeniedPopup(event);
  }
  else{
   try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml"));

    root =loader.load();

    adminController adminController=loader.getController();
    adminController.setUser(user);
    adminController.setPortfolio(portfolio);
    adminController.setNotificationService(notificationService);


    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene=new Scene(root);
    stage.setScene(scene);
    adminController.start(stage);
    stage.show();
   } catch (IOException e) {
    throw new RuntimeException(e);
   }
  }

 }
 public void switchtoReport(ActionEvent event){

   try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml"));

    root =loader.load();

    adminController adminController=loader.getController();
    adminController.setUser(user);
    adminController.setPortfolio(portfolio);
    adminController.setNotificationService(notificationService);


    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene=new Scene(root);
    stage.setScene(scene);
    adminController.start(stage);
    stage.show();
   } catch (IOException e) {
    throw new RuntimeException(e);
   }

 }
 public void logout(ActionEvent event){
  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
  alert.setTitle("Logout");
  alert.setHeaderText("Confirmation");
  alert.setContentText("Are you sure you want to log out?");

  // Add buttons to the alert
  alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

  // Handle the user's response
  alert.showAndWait().ifPresent(response -> {
   if (response == ButtonType.YES) {
    notificationService.cancelTimer();
    try {
     root = FXMLLoader.load(getClass().getResource("/login.fxml"));
    } catch (IOException e) {
     throw new RuntimeException(e);
    }
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene=new Scene(root);
    stage.setScene(scene);
    stage.show();
   }
   else {
    alert.close();
   }

  });

 }
 public void initializeStage(){
  stage.setOnCloseRequest(event -> {
   event.consume(); // Consume the close event to prevent the application from closing immediately
   closeConfirmation(stage);
  });
 }

 private void closeConfirmation(Stage primaryStage) {
  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
  alert.setTitle("Confirmation");
  alert.setHeaderText("Close Application");
  alert.setContentText("Are you sure you want to close the application?");

  ButtonType closeButtonType = ButtonType.OK;
  ButtonType cancelButtonType = ButtonType.CANCEL;
  alert.getButtonTypes().setAll(closeButtonType, cancelButtonType);

  alert.showAndWait().ifPresent(buttonType -> {
   if (buttonType == closeButtonType) {
    notificationService.cancelTimer();
    primaryStage.close(); // Close the application if "OK" button is clicked
    System.exit(0);
   }
  });
 }


 int positionNo;
 double buyPrice;
 //but button
 public void buy(ActionEvent event){
  Stocklist2 stocklist = new Stocklist2();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  buyPrice=price2.getCloseDate().peek().getData();
  PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
  TradingEngine3 tradingEngine = new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,this , notificationService );
  TradingApp2 tradingapp = new TradingApp2(tradingEngine);
  PortFolio portFolio=portfolio;
//  tradingEngine.CheckPendingOrder(portFolio);
  if (price_select.getValue().equals("Limit")){
   buyPrice=Double.parseDouble(buyPrice_field.getText());
  }
  tradingapp.setOrder(user,1,positionNo,buyPrice,Integer.parseInt(buyVolume_field.getText()),symbol);
  trade_status.appendText("Checking for pending order...\n");
  tradingEngine.CheckPendingOrder(portFolio);
 }


//sell button
 public void sell(ActionEvent event){
  Stocklist2 stocklist = new Stocklist2();
  Price2 price2 = new Price2();
  price2.fetchData(symbol);
  buyPrice=price2.getCloseDate().peek().getData();
  PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
  TradingEngine3 tradingEngine = new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,this,notificationService);
  TradingApp2 tradingapp = new TradingApp2(tradingEngine);
  PortFolio portFolio=portfolio;
//  tradingEngine.CheckPendingOrder(portFolio);
  if (sellPrice_select.getValue().equals("Limit")){
   buyPrice=Double.parseDouble(sellPrice_field.getText());
  }
  tradingapp.setOrder(user,2,positionNo,buyPrice,Integer.parseInt(sellVolume_field.getText()),symbol);
  trade_status.appendText("Checking for pending order...\n");
  tradingEngine.CheckPendingOrder(portFolio);
 }

 //display suggested price for both sell and buy
 public String displaySuggestedPrice(String symbol) {
  Price2 price2=new Price2();
  price2.fetchData(symbol);

  double stockPrice = price2.getCloseDate().peek().getData(); //get current market stock price
  double acceptableRange = stockPrice * 0.01; // Calculate 1% of the current price
  double lowerBound = stockPrice - acceptableRange;
  double upperBound = stockPrice + acceptableRange;
  return String.format("Suggested price range  : %.4f - %.4f",lowerBound,upperBound) ;
 }

 //choice box for buy
 public void choiceBox(ActionEvent event){

     String choice= price_select.getValue();
     if (choice.equals("Market")){
         positionNo=1;
         buyPrice_field.setVisible(false);
         BuysuggestedPrice.setVisible(false);
     }
     else{
         buyPrice_field.setVisible(true);
         positionNo=2;
         if (symbol!=null){
          BuysuggestedPrice.setVisible(true);
          BuysuggestedPrice.setText(displaySuggestedPrice(symbol));
          BuysuggestedPrice.setTextFill(Color.GREEN);
         }
     }
 }
//choice box for sell
 public void choiceBox2(ActionEvent event){
  String choice= sellPrice_select.getValue();
  if (choice.equals("Market")){
   positionNo=1;
   sellPrice_field.setVisible(false);
   SellsuggestedPrice.setVisible(false);
  }
  else{
   sellPrice_field.setVisible(true);
   positionNo=2;
   if (symbol!= null){
    SellsuggestedPrice.setVisible(true);
    SellsuggestedPrice.setText(displaySuggestedPrice(symbol));
    SellsuggestedPrice.setTextFill(Color.GREEN);
   }

  }
 }

 private String[] pOrderSelectorText={"Personal","Others buy order","Others sell order"};
 @Override
 public void initialize(URL url, ResourceBundle resourceBundle) {
  buyPrice_field.setVisible(false);
  sellPrice_field.setVisible(false);
  BuysuggestedPrice.setVisible(false);
  SellsuggestedPrice.setVisible(false);
  price_select.getItems().addAll(position);
  price_select.setOnAction(this::choiceBox);
  sellPrice_select.getItems().addAll(position);
  sellPrice_select.setOnAction(this::choiceBox2);
  trade_status.setEditable(false);
  pOrder_selector.getItems().addAll(pOrderSelectorText);
  pOrder_selector.setOnAction(this::pOrderchoiceBox);

 }



 public void pOrderchoiceBox(ActionEvent event){
  String selectedOption = pOrder_selector.getValue();
  if (selectedOption.equals("Personal")){
     getPending(event);
  }
  else if(selectedOption.equals("Others buy order")){
   getOPbuy(event);
  }
  else{
   getOPsell(event);
  }
 }


 public void getPending(ActionEvent event){
  ObservableList<Order>  pendingOrders= FXCollections.observableArrayList(database.retrivePendingOrder(user));
  date_column.setCellValueFactory(new PropertyValueFactory<Order,LocalDateTime>("time"));
  symbol_column.setCellValueFactory(new PropertyValueFactory<Order,Stock>("stock"));
  price_column.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
  type_column.setCellValueFactory(new PropertyValueFactory<Order,Order.Type>("type"));
  volume_column.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));

  cancel_column.setCellFactory(createButtonCellFactory());
  pOrder_table.setItems(pendingOrders);
 }

 private Callback<TableColumn<Order, Void>, TableCell<Order, Void>> createButtonCellFactory() {
  return new Callback<>() {
   @Override
   public TableCell<Order, Void> call(TableColumn<Order, Void> column) {
    return new TableCell<>() {
     private final Button button = new Button("cancel");

     {
      button.setOnAction(event -> {
       Order order = getTableView().getItems().get(getIndex());
       Stocklist2 stocklist = new Stocklist2();
       PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
       TradingEngine3 tradingEngine3= new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,ssController.this,notificationService);
       tradingEngine3.cancelPendingOrder2(portfolio, order.getID());
       getPending(event);
      });
     }

     @Override
     protected void updateItem(Void item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
       setGraphic(null);
      } else {
       setGraphic(button);
      }
     }
    };
   }
  };
 }

 //get others pending buy
 public void getOPbuy(ActionEvent event){

   ObservableList<Order>  otherBuyList = FXCollections.observableArrayList(database.retriveBuyPendingOrder(user));
   date_column.setCellValueFactory(new PropertyValueFactory<Order,LocalDateTime>("time"));
   symbol_column.setCellValueFactory(new PropertyValueFactory<Order,Stock>("stock"));
   price_column.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
   type_column.setCellValueFactory(new PropertyValueFactory<Order,Order.Type>("type"));
   volume_column.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));


   cancel_column.setCellFactory(ObuyButton());
   pOrder_table.setItems(otherBuyList);

 }

 //generate others pending buy button
 private Callback<TableColumn<Order, Void>, TableCell<Order, Void>> ObuyButton() {
  return new Callback<>() {
   @Override
   public TableCell<Order, Void> call(TableColumn<Order, Void> column) {
    return new TableCell<>() {
     private final Button button = new Button("Sell");

     {
      button.setOnAction(event -> {
       Stocklist2 stocklist = new Stocklist2();
       Price2 price2 = new Price2();
       PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
       TradingEngine3 tradingEngine = new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,ssController.this ,notificationService);
       TradingApp2 tradingapp = new TradingApp2(tradingEngine);
       PortFolio portFolio=portfolio;
       Order order = getTableView().getItems().get(getIndex());
       tradingapp.setOrder(user,2,2,order.getPrice(),order.getShares(),order.getStock().getSymbol());
       getOPbuy(event);
      });
     }

     @Override
     protected void updateItem(Void item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
       setGraphic(null);
      } else {
       setGraphic(button);
      }
     }
    };
   }
  };
 }

//get others pending sell
 public void getOPsell(ActionEvent event){
  ObservableList<Order>  otherBuyList = FXCollections.observableArrayList(database.retriveSellPendingOrder(user));
  date_column.setCellValueFactory(new PropertyValueFactory<Order,LocalDateTime>("time"));
  symbol_column.setCellValueFactory(new PropertyValueFactory<Order,Stock>("stock"));
  price_column.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
  type_column.setCellValueFactory(new PropertyValueFactory<Order,Order.Type>("type"));
  volume_column.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));

  cancel_column.setCellFactory(OsellButton());
  pOrder_table.setItems(otherBuyList);

 }


//generate others pending button
 private Callback<TableColumn<Order, Void>, TableCell<Order, Void>> OsellButton() {
  return new Callback<>() {
   @Override
   public TableCell<Order, Void> call(TableColumn<Order, Void> column) {
    return new TableCell<>() {
     private final Button button = new Button("Buy");

     {
      button.setOnAction(event -> {
       Stocklist2 stocklist = new Stocklist2();
       Price2 price2 = new Price2();
       PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
       TradingEngine3 tradingEngine = new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,ssController.this ,notificationService);
       TradingApp2 tradingapp = new TradingApp2(tradingEngine);
       PortFolio portFolio=portfolio;
       Order order = getTableView().getItems().get(getIndex());
       tradingapp.setOrder(user,1,2,order.getPrice(),order.getShares(),order.getStock().getSymbol());
       getOPsell(event);
      });
     }

     @Override
     protected void updateItem(Void item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
       setGraphic(null);
      } else {
       setGraphic(button);
      }
     }
    };
   }
  };
 }

 public void displayStocks(){
  PopupStockList popupStockList=new PopupStockList();
  popupStockList.showAStock();
 }


 public void webViewPopup(ActionEvent event){
  String ss = symbol;
  String [] s = ss.split("\\.");
  String url = "https://www.malaysiastock.biz/Corporate-Infomation.aspx?securityCode=";
  WebView webView = new WebView();
  // Load the specified URL in the WebView
  webView.getEngine().load(url+s[0]);

  // Create a new stage for the WebView
  Stage webViewStage = new Stage();
  webViewStage.setScene(new Scene(webView));
  webViewStage.setTitle("Additional info");
  webViewStage.show();


 }
 public void technicalGraph(ActionEvent event){
  String ss = symbol;
  String[] s = ss.split("\\.");
  String baseUrl = "https://www.malaysiastock.biz/Stock-Chart.aspx?securitycode=";
  String securityCode = s[0];
  String modeParam = "mode=1D";
// Encode the security code and mode parameter
  String encodedSecurityCode = null;
  try {
   encodedSecurityCode = URLEncoder.encode(securityCode, "UTF-8");
  } catch (UnsupportedEncodingException e) {
   throw new RuntimeException(e);
  }
  String encodedModeParam = null;
  try {
   encodedModeParam = URLEncoder.encode(modeParam, "UTF-8");
  } catch (UnsupportedEncodingException e) {
   throw new RuntimeException(e);
  }

// Construct the encoded URL
  String encodedUrl = baseUrl + encodedSecurityCode + "&" + encodedModeParam;

  WebView webView = new WebView();
  webView.getEngine().load(encodedUrl);

  Stage webViewStage = new Stage();
  webViewStage.setScene(new Scene(webView));
  webViewStage.setTitle("In-depth graph");

  // Increase the size of the Stage
  webViewStage.setWidth(1300); // Set the width
  webViewStage.setHeight(800); // Set the height
  webViewStage.show();

 }
 private void showEntryDeniedPopup(ActionEvent event) {
  Stage popupStage = new Stage();
  popupStage.initModality(Modality.APPLICATION_MODAL); // Make the popup window modal
  popupStage.setTitle("Entry Denied");

  // Replace `rootNode` with the actual root node of your scene
  Parent root = new VBox(); // Replace VBox with the appropriate root node type (e.g., BorderPane, GridPane, etc.)

  Scene scene = new Scene(root);
  popupStage.setScene(scene);

  Alert alert = new Alert(Alert.AlertType.WARNING);
  alert.initOwner(popupStage);
  alert.setHeaderText(null);
  alert.setContentText("Access Denied");

  alert.showAndWait();

  // Uncomment the line below if you want to close the popup window automatically after showing the alert
  // popupStage.close();
 }

 public void lot100(ActionEvent event) {
  Map<Stock, Integer> lot100pool = database.retriveLotPool();

  TableView<DataEntry> tableView = new TableView<>();

  TableColumn<DataEntry, Stock> stockColumn = new TableColumn<>("Stock");
  stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

  TableColumn<DataEntry, Integer> quantityColumn = new TableColumn<>("Quantity");
  quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

  tableView.getColumns().addAll(stockColumn, quantityColumn);

  ObservableList<DataEntry> tableData = FXCollections.observableArrayList();
  for (Map.Entry<Stock, Integer> entry : lot100pool.entrySet()) {
   tableData.add(new DataEntry(entry.getKey(), entry.getValue()));
  }

  tableView.setItems(tableData);

  Stage tableStage = new Stage();
  tableStage.setScene(new Scene(tableView, 300, 300));
  tableStage.setTitle("500-lot pool");
  tableStage.show();
 }

 public static class DataEntry {
  private Stock stock;
  private Integer quantity;

  public DataEntry(Stock stock, Integer quantity) {
   this.stock = stock;
   this.quantity = quantity;
  }

  public Stock getStock() {
   return stock;
  }

  public Integer getQuantity() {
   return quantity;
  }
 }

 public void newsPopup(ActionEvent event){

  String url = "https://www.malaysiastock.biz/Blog/Blog-Headlines.aspx";
  WebView webView = new WebView();
  // Load the specified URL in the WebView
  webView.getEngine().load(url);

  // Create a new stage for the WebView
  Stage webViewStage = new Stage();
  webViewStage.setScene(new Scene(webView));
  webViewStage.setTitle("News");
  webViewStage.show();
 }

 public void getHoldings(){
  holdings_table.refresh();
  ObservableList<Order>  holdings = FXCollections.observableArrayList(database.retriveHoldingsForGui(user)) ;
  hSymbol_column.setCellValueFactory(new PropertyValueFactory<Order,String>("stock"));
  hVolume_column.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));
  hBuyPrice_column.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));

  holdings_table.getItems().addAll(holdings);
  holdings_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
   if (newSelection != null) {
    // Call the method to handle row selection
    symbol=newSelection.getStock().getSymbol();
    getSymbol2( new ActionEvent());
   }
  });
 }
 public void getSymbol2(ActionEvent event){
  String searchFieldText=stockSearch_field.getText();
  stockSearch search= new stockSearch();


  String name="";
  Stocklist2 stocklist2= new Stocklist2();
  PriorityQueue<Stock> queue= stocklist2.fetchStockList() ;
  while(!queue.isEmpty()){
   if (queue.peek().getSymbol().equals(symbol)){
    name=queue.peek().getName();
    break;
   }
   else{
    queue.poll();
   }
  }

  Price2 price2=new Price2();
  price2.fetchData(symbol);
  double  close=price2.getCloseDate().peek().getData();
  double volume=price2.getVolumeDate().peek().getData();
  PriorityQueue<ComparableDataByDate> temp = price2.getOpenDate();
  for (int i =0;i<temp.size()-1;i++){
   temp.poll();
  }
  double open=temp.peek().getData();
  double high=price2.getHighData().peek().getData();
  double low=price2.getLowData().peek().getData();

  companySymbol_label.setText(symbol);
  companyName_field.setText(name);
  currentPrice_panel.setText(String.valueOf(close));
  volume_panel.setText(String.valueOf(volume));
  open_panel.setText(String.valueOf(open));
  high_panel.setText(String.valueOf(high));
  low_panel.setText(String.valueOf(low));

  button_1d(event);

 }

 public void checkPendingButton(){
  Stocklist2 stocklist = new Stocklist2();
  PriorityQueue<Stock> retrievedStockList = stocklist.fetchStockList();
  TradingEngine3 tradingEngine = new TradingEngine3(new ArrayList<>(retrievedStockList),trade_status,this , notificationService );
  TradingApp2 tradingapp = new TradingApp2(tradingEngine);
  PortFolio portFolio= new PortFolio(user);
  trade_status.appendText("Checking for pending order...\n");
  tradingEngine.CheckPendingOrder(portFolio);

 }

}

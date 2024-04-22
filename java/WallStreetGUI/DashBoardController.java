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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    NotificationService notificationService;

    public void setNotificationService(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    private Database database = new Database();


    private User user;

    private PortFolio portfolio;


    public void setUser(User user) {
        this.user = user;
    }


    public void setPortfolio(PortFolio portfolio){
        this.portfolio=portfolio;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<Order, LocalDateTime> Hdate;

    @FXML
    private TableColumn<Order, Order.Position> Hposition;

    @FXML
    private TableColumn<Order, Double> Hprice;

    @FXML
    private TableColumn<Order, String> Hsymbol;

    @FXML
    private TableColumn<Order, Order.Type> Htype;

    @FXML
    private TableColumn<Order, Integer> Hvolume;

    @FXML
    private Label PL_label;

    @FXML
    private Pane PL_pane;

    @FXML
    private TableColumn<Order, LocalDateTime> Pdate;

    @FXML
    private TableColumn<Order, Double> Pprice;

    @FXML
    private TableColumn<Order, String> Psymbol;

    @FXML
    private TableColumn<Order, Order.Type> Ptype;

    @FXML
    private TableColumn<Order, Integer> Pvolume;

    @FXML
    private Button SS_button;

    @FXML
    private Button admin_button;

    @FXML
    private ImageView admin_image;

    @FXML
    private Label balanceNo;

    @FXML
    private Label balance_label;

    @FXML
    private Pane balance_pane;

    @FXML
    private Pane balance_pane111;

    @FXML
    private Button dashboard_button;

    @FXML
    private ImageView dashboard_image;

    @FXML
    private Label history_label;

    @FXML
    private AnchorPane history_pane;

    @FXML
    private TableView<Order> history_table;

    @FXML
    private AnchorPane info_pane;

    @FXML
    private Button leaderboard_button;

    @FXML
    private ImageView leaderboard_image;

    @FXML
    private Button logout_button;

    @FXML
    private Label name_label;

    @FXML
    private AnchorPane name_pane;

    @FXML
    private ImageView name_pic;

    @FXML
    private Label pOrder_label;

    @FXML
    private AnchorPane pOrder_pane;

    @FXML
    private TableView<Order> pOrder_table;

    @FXML
    private Label plNo;

    @FXML
    private Label point_label;

    @FXML
    private Label pointsNo;

    @FXML
    private Pane points_pane;

    @FXML
    private Label positionNo;

    @FXML
    private Label qualification_label;

    @FXML
    private Button report_button;

    @FXML
    private ImageView report_image;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private ImageView ss_image;

    @FXML
    private TableColumn<Order, Integer> HOvolume;

    @FXML
    private TableColumn<Order, Double> HOprice;

    @FXML
    private TableColumn<Order, String> HOsymbol;

    @FXML
    private TableView<Order> holdings_table;
    @FXML
    private ToggleButton noti_button;

    @FXML
    private Label noti_label;

    public void switchtoSS(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ss.fxml"));

            root =loader.load();

            ssController ssController=loader.getController();
            ssController.setUser(user);
            ssController.setPortfolio(portfolio);
            ssController.setNotificationService(notificationService);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            scene.getStylesheets().add("dashboard_style.css");
            stage.setScene(scene);
            ssController.start(stage);
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

    public void switchtoReport(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/report.fxml"));

            root =loader.load();

            reportController reportController=loader.getController();
            reportController.setUser(user);
            reportController.setPortfolio(portfolio);
            reportController.setNotificationService(notificationService);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            reportController.start(stage);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void start(Stage stage){
            name_label.setText(user.getName());
            qualification_label.setText(user.getStatus());
            if (user.getStatus().equals("qualified")){
                qualification_label.setTextFill(Color.GREEN);
            }
            else{
                qualification_label.setTextFill(Color.GREEN);
            }

            balanceNo.setText(String.valueOf(database.retriveAccbalance(user.getName())));
            plNo.setText(String.valueOf(user.getTotalPnL()));

            pointsNo.setText(String.valueOf(user.getPoints()));



            positionNo.setText(String.valueOf(user.getRanking()));


            notificationService= new NotificationService(user);
            notificationService.scheduleNotificationCheck();
            noti_button.setText(notificationService.getEnable() ? "Enabled" : "Disabled");
             this.stage=stage;
             initializeStage();

            getHistory();
            getPending();
            getHoldings();
    }


    public void getHistory(){
        ObservableList<Order> orderHistory = FXCollections.observableArrayList(database.retriveOrderHistory(user));
        Hdate.setCellValueFactory(new PropertyValueFactory<Order,LocalDateTime>("time"));
        Hsymbol.setCellValueFactory(new PropertyValueFactory<Order,String>("stock"));
        Hprice.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
        Hposition.setCellValueFactory(new PropertyValueFactory<Order, Order.Position>("position"));
        Htype.setCellValueFactory(new PropertyValueFactory<Order, Order.Type>("type"));
        Hvolume.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));

        history_table.setItems(orderHistory);
    }

    public void getPending(){
        ObservableList<Order> pendingOrder = FXCollections.observableArrayList(database.retrivePendingOrder(user));
        Pdate.setCellValueFactory(new PropertyValueFactory<Order,LocalDateTime>("time"));
        Psymbol.setCellValueFactory(new PropertyValueFactory<Order,String>("stock"));
        Pprice.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
        Ptype.setCellValueFactory(new PropertyValueFactory<Order, Order.Type>("type"));
        Pvolume.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));

        pOrder_table.setItems(pendingOrder);

    }


        public void getHoldings() {
           ObservableList<Order> holdings = FXCollections.observableArrayList(database.retriveHoldingsForGui(user));
           HOsymbol.setCellValueFactory(new PropertyValueFactory<Order,String>("stock"));
           HOprice.setCellValueFactory(new PropertyValueFactory<Order,Double>("price"));
           HOvolume.setCellValueFactory(new PropertyValueFactory<Order,Integer>("shares"));


           holdings_table.setItems(holdings);

        }


        public void notiButton(ActionEvent event){
         notificationService.setEnable();
         noti_button.setText(notificationService.getEnable() ? "Enabled" : "Disabled");

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

    private void showEntryDeniedPopup(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Make the popup window modal
        popupStage.setTitle("Access Denied");

        // Replace `rootNode` with the actual root node of your scene
        Parent root = new VBox(); // Replace VBox with the appropriate root node type (e.g., BorderPane, GridPane, etc.)

        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(popupStage);
        alert.setHeaderText(null);
        alert.setContentText("Entry Denied");

        alert.showAndWait();

        // Uncomment the line below if you want to close the popup window automatically after showing the alert
        // popupStage.close();
    }



}
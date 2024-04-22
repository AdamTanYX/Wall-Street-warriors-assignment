package WallStreetGUI;

import WallStreet.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class adminController {

    NotificationService notificationService;

    public void setNotificationService(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    private User user;

    public  void setUser(User user) {
        this.user = user;
    }

    private PortFolio portfolio;

    public void setPortfolio(PortFolio portfolio) {
        this.portfolio = portfolio;
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    Database database = new Database();

    @FXML
    private Button SS_button;

    @FXML
    private TableColumn<User, Void> action_column;

    @FXML
    private TableColumn<User, Void> activity_column;

    @FXML
    private Button admin_button;

    @FXML
    private ImageView admin_image;

    @FXML
    private TableColumn<User, Double> balance_column;

    @FXML
    private Button dashboard_button;

    @FXML
    private ImageView dashboard_image;

    @FXML
    private TableColumn<User, String> email_column;

    @FXML
    private Button leaderboard_button;

    @FXML
    private ImageView leaderboard_image;

    @FXML
    private Button logout_button;

    @FXML
    private TableColumn<User, String> name_column;

    @FXML
    private TableColumn<User, String> password_column;

    @FXML
    private Button report_button;

    @FXML
    private ImageView report_image;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private ImageView ss_image;

    @FXML
    private TableColumn<User, String> status_column;

    @FXML
    private TableView<User> user_table;

    @FXML
    private Button fraudDetect_button;


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
            stage.setScene(scene);
            ssController.start(stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void switchtoDashboard(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));

            root =loader.load();

            DashBoardController dashBoardController=loader.getController();
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


    public void start(Stage stage){
        List<User> userList = database.retriveUserList();


        List<User> usersToRemove = new ArrayList<>();

        for (User user : userList) {
            if (user.getName().equalsIgnoreCase("Admin")) {
                usersToRemove.add(user);
            }
        }

        userList.removeAll(usersToRemove);
        ObservableList<User>  userObservableList= FXCollections.observableArrayList(userList);
        name_column.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        email_column.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        password_column.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        status_column.setCellValueFactory(new PropertyValueFactory<User, String>("status"));
        balance_column.setCellValueFactory(new PropertyValueFactory<User,Double>("accountBalance"));
        action_column.setCellFactory(createButtonAction());
        activity_column.setCellFactory(createButtonActivity());

        user_table.setItems(userObservableList);

        this.stage=stage;
        initializeStage();
    }




    private Callback<TableColumn<User, Void>, TableCell<User, Void>> createButtonAction() {
        return new Callback<>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> column) {
                return new TableCell<>() {
                    private final Button button = new Button("Disqualify");

                    {
                        button.setOnAction(event -> {
                            AdminPanel adminPanel = new AdminPanel();
                            User user = getTableView().getItems().get(getIndex());
                            adminPanel.disqualifyUser(user.getName());
                            start(stage);
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

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> createButtonActivity() {
        return new Callback<>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> column) {
                return new TableCell<>() {
                    private final Button button = new Button("View");

                    {
                        button.setOnAction(event -> {
                            // Get the user associated with the clicked row
                            User user = getTableView().getItems().get(getIndex());

                            // Create and configure the popup window
                            PopupWindow popup = new PopupWindow(user);
                            popup.createTable();

                            // Customize the popup window based on the user data
                            // For example, you can pass the user to the window or display user-specific information
                            // popup.setUser(user);

                            // Show the popup window
                            popup.showAndWait();
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
        popupStage.setTitle("Entry Denied");

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

    @FXML
    private void setFraudDetect(ActionEvent event) {
        FraudDetection fraudDetection= new FraudDetection();
        fraudDetection.detectFraud();
        Stage popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.setTitle("Fraud Detector");
        popUpStage.setMinWidth(250);
        javafx.scene.control.Label label;
        if (!fraudDetection.isFraudDetected()) {
            label = new javafx.scene.control.Label("No Fraud Detected");
        }
        else{
            label = new javafx.scene.control.Label("Fraud Detected \n Details  will available in admin's email");
        }
        // Display "Fraud Detected" message

        javafx.scene.layout.StackPane popUpRoot = new javafx.scene.layout.StackPane(label);
        Scene popUpScene = new Scene(popUpRoot, 200, 150);
        popUpStage.setScene(popUpScene);
        popUpStage.showAndWait();
    }



}

package WallStreetGUI;

import WallStreet.Database;
import WallStreet.NotificationService;
import WallStreet.PortFolio;
import WallStreet.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class leaderboardController {
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

    @FXML
    private ImageView report_image;

    @FXML
    private Button dashboard_button;

    @FXML
    private TableColumn<User, Double> pl_column;

    @FXML
    private TableColumn<User, String> name_column;

    @FXML
    private Button admin_button;

    @FXML
    private Button logout_button;

    @FXML
    private ImageView admin_image;

    @FXML
    private ImageView leaderboard_image;

    @FXML
    private Button SS_button;

    @FXML
    private TableColumn<User, Integer> position_column;

    @FXML
    private ImageView dashboard_image;

    @FXML
    private Button report_button;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private ImageView ss_image;

    @FXML
    private Button leaderboard_button;

    @FXML
    private Label header;

    @FXML
    private TableView<User> rankingTable;

    @FXML
    private TableColumn<User, Double> points_column;

    Database database = new Database();
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
    public void switchtoAdmin(ActionEvent event){
        if (!user.getEmail().equals("22004889@siswa.um.edu.my")||!user.getEmail().equals("22057495@siswa.um.edu.my")){
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
        this.stage=stage;
        initializeStage();
        getLeaderboard();
    }

    public void getLeaderboard() {
        List<User> userList = database.retriveUserList();
        List<User> usersToRemove = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().equalsIgnoreCase("Admin")) {
                usersToRemove.add(user);
            }
        }
        userList.removeAll(usersToRemove);
        // Sort the userList based on the position column in ascending order
        userList.sort(Comparator.comparing(User::getRanking));

        // Get the top 10 users
        List<User> topUsers = userList.stream().limit(10).collect(Collectors.toList());

        ObservableList<User> userObservableList = FXCollections.observableArrayList(topUsers);

        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        pl_column.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Double totalPnL = user.getTotalPnL();
            return new SimpleObjectProperty<>(totalPnL);
        });
        points_column.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Double points = user.getPoints();
            return new SimpleObjectProperty<>(points);
        });
        position_column.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Integer position = user.getRanking();
            return new SimpleObjectProperty<>(position);
        });

        // Set cell factory for name_column to align text in the middle
        name_column.setCellFactory(column -> {
            return new TableCell<User, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setAlignment(Pos.CENTER);
                    } else {
                        setText("");
                    }
                }
            };
        });

        // Set cell factory for pl_column to align text in the middle
        pl_column.setCellFactory(column -> {
            return new TableCell<User, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.toString());
                        setAlignment(Pos.CENTER);
                    } else {
                        setText("");
                    }
                }
            };
        });

        // Set cell factory for points_column to align text in the middle
        points_column.setCellFactory(column -> {
            return new TableCell<User, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.toString());
                        setAlignment(Pos.CENTER);
                    } else {
                        setText("");
                    }
                }
            };
        });

        // Set cell factory for position_column to align text in the middle
        position_column.setCellFactory(column -> {
            return new TableCell<User, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.toString());
                        setAlignment(Pos.CENTER);
                    } else {
                        setText("");
                    }
                }
            };
        });

        rankingTable.getItems().setAll(userObservableList);

        rankingTable.refresh();
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
        alert.setContentText("Access Denied");

        alert.showAndWait();

        // Uncomment the line below if you want to close the popup window automatically after showing the alert
        // popupStage.close();
    }






}

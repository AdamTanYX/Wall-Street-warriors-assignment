package WallStreetGUI;

import WallStreet.Database;
import WallStreet.PortFolio;
import WallStreet.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    private Database database = new Database();

    private User user;

    private PortFolio portfolio;


    TradingEngine3 tradingEngine3= new TradingEngine3();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField email_field;

    @FXML
    private AnchorPane right_pane;

    @FXML
    private Pane password_pane;

    @FXML
    private Button login_button;

    @FXML
    private Label password_label;

    @FXML
    private Button register_button;

    @FXML
    private PasswordField password_field;

    @FXML
    private AnchorPane left_pane;

    @FXML
    private Label email_label;

    @FXML
    private ImageView login_image;

    @FXML
    private ImageView big_image;

    @FXML
    private Pane email_pane;

    @FXML
    private Label big_label;

    @FXML
    private Label login_label;

    @FXML
    private Label login_status;



    public void Login(ActionEvent event){
        if (tradingEngine3.checkCompetitionPeriod()){
            System.out.println("The Competition Has Ended!");
            password_field.clear();
            email_field.clear();
            return;
        }
        if(database.readUser(email_field.getText(),password_field.getText())){
            try {
                user= database.retriveUser(email_field.getText());
                 this.portfolio = new PortFolio(user);
                 if (user.getStatus().equalsIgnoreCase("disqualified")){
                     showEntryDeniedPopup(event);
                     password_field.clear();
                     email_field.clear();
                     return;
                 }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));

                root =loader.load();

                DashBoardController dashBoardController=loader.getController();

                dashBoardController.setUser(user);
                dashBoardController.setPortfolio(portfolio);


                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                String cssFile = getClass().getResource("/dashboard_style.css").toExternalForm();
                scene.getStylesheets().add(cssFile);
                stage.setScene(scene);
                dashBoardController.start(stage);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else{
            login_status.setText("Incorrect password/email. Please try again.");
            login_status.setTextFill(Color.RED);
            password_field.clear();
        }


    }


    public void Register(ActionEvent event){

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
                root =loader.load();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
                primaryStage.close(); // Close the application if "OK" button is clicked
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
        alert.setContentText(" DISQUALIFIED \n PLEASE CONTACT ADMIN\n 22004889@siswa.um.edu.my");

        alert.showAndWait();

        // Uncomment the line below if you want to close the popup window automatically after showing the alert
        // popupStage.close();
    }

}


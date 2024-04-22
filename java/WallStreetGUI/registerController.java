package WallStreetGUI;



import WallStreet.CreateAccount;
import WallStreet.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class registerController {

    CreateAccount createAccount = new CreateAccount();

    Database database= new Database();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField Cpassword_field;

    @FXML
    private Label Cpassword_label;

    @FXML
    private Pane Cpassword_panel;

    @FXML
    private Button back_button;

    @FXML
    private TextField email_field;

    @FXML
    private Label email_label;

    @FXML
    private Pane email_panel;

    @FXML
    private Label invalidPassword;

    @FXML
    private AnchorPane left_pane;

    @FXML
    private TextField name_field;

    @FXML
    private Label name_label;

    @FXML
    private Pane name_pane;

    @FXML
    private Label passNotSame;

    @FXML
    private PasswordField password_field;

    @FXML
    private Label password_label;

    @FXML
    private Pane password_panel;

    @FXML
    private Button register_button;

    @FXML
    private Label register_label;

    @FXML
    private AnchorPane right_pane;

    @FXML
    private ImageView rightimage;


    public void  Register(ActionEvent event){
        invalidPassword.setText("");
        passNotSame.setText("");
        String name=name_field.getText();
        String email=email_field.getText();
        String password=password_field.getText();
        String Cpassword=Cpassword_field.getText();
        if (createAccount.isValidPassword(password)){
            if (password.equals(Cpassword)){
                database.insertUser(name,email,password);
                try {
                    root = FXMLLoader.load(getClass().getResource("/login.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene=new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                passNotSame.setText("Password and Confirm Password not same");
                passNotSame.setTextFill(Color.RED);
            }
        }
        else{
            invalidPassword.setText("Invalid Password");
            invalidPassword.setTextFill(Color.RED);
        }


    }

    public void back(ActionEvent event){
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

}

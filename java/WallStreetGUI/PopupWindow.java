package WallStreetGUI;

import WallStreet.Database;
import WallStreet.Order;
import WallStreet.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class PopupWindow extends Stage {
    User user;
    Database database;
    public PopupWindow(User user) {
        this.user=user;
        database=new Database();
        // Initialize the stage and set any properties you need
        setTitle(user.getName());
        setResizable(false);
    }

    public void createTable() {
        // Create the table view and columns
        TableView<Order> tableView = new TableView<>();
        TableColumn<Order, LocalDateTime> Hdate = new TableColumn<>("Date");
        TableColumn<Order, String> Hsymbol = new TableColumn<>("Stock Symbol");
        TableColumn<Order, Double> Hprice = new TableColumn<>("Price");
        TableColumn<Order, Order.Position> Hposition = new TableColumn<>("Position");
        TableColumn<Order, Order.Type> Htype = new TableColumn<>("Type");
        TableColumn<Order, Integer> Hvolume = new TableColumn<>("Volume");

        // Set up cell value factories for each column
        Hdate.setCellValueFactory(new PropertyValueFactory<>("time"));
        Hsymbol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        Hprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        Hposition.setCellValueFactory(new PropertyValueFactory<>("position"));
        Htype.setCellValueFactory(new PropertyValueFactory<>("type"));
        Hvolume.setCellValueFactory(new PropertyValueFactory<>("shares"));

        // Add the columns to the table view
        tableView.getColumns().addAll(Hdate, Hsymbol, Hprice, Hposition, Htype, Hvolume);

        // Create an ObservableList<Order> containing the order history data
        ObservableList<Order> orderHistory = FXCollections.observableArrayList(database.retriveOrderHistory(user));

        // Set the items of the table view
        tableView.setItems(orderHistory);

        // Create a layout and add the table view
        VBox layout = new VBox(tableView);

        // Create a scene with the layout
        Scene scene = new Scene(layout);

        // Set the scene for the stage
        setScene(scene);
    }

}


package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Window;
import main.entities.Menu;
import main.entities.Restaurant;
import main.entities.Table;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookTableScreenController implements Initializable {
    public Button backBtn;
    public Button nextBtn;
    public ComboBox tableComboBox;
    public ComboBox dinerNumComboBox;
    public Label errorLabel;

    private Table tbl = new Table();
    private Menu menu;
    private Restaurant restaurant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);

            tableComboBox.getItems().addAll("01", "02", "03", "04");
            dinerNumComboBox.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //GUI Inputs
    public void backPressed(ActionEvent actionEvent) {
        try {
            switchToStartScreen();
        } catch (Exception e){
            displayCommonError();
        }
    }
    public void nextPressed(ActionEvent actionEvent) {
        try {
            String tableID = tableComboBox.getSelectionModel().getSelectedItem().toString();
            Integer dinerNum = Integer.parseInt(dinerNumComboBox.getSelectionModel().getSelectedItem().toString());

            if (!checkTableIDValid(tableID)) {
                errorLabel.setText("Error! Table ID is invalid. Please change it.");
                errorLabel.setVisible(true);
            } else if (!checkDinerNumValid(dinerNum)) {
                errorLabel.setText("Error! Number of Diners is invalid. It should be larger than 0.");
                errorLabel.setVisible(true);
            } else {
                this.tbl.setTableID(tableID);
                this.tbl.setDinerNum(dinerNum);
                switchToMenuScreen();
            }
        }
        catch(Exception e){
            errorLabel.setText("Error! Please check inputs. All fields must be filled.");
            errorLabel.setVisible(true);
        }
    }

    //Validating User Data
    private Boolean checkTableIDValid(String tableID) {
        if (tableComboBox.getSelectionModel().isEmpty()) {
            return false;
        }
        return true;
    }
    private Boolean checkDinerNumValid(Integer dinerNum) {
        if (dinerNumComboBox.getSelectionModel().isEmpty() || dinerNum<=0) {
            return false;
        }
        return true;
    }

    //Display GUI
    public void displayCommonError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        errorLabel.setVisible(true);
    }

    //Screen Switching
    public void switchToStartScreen() {
        try {
            Window mainWindow = backBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/StartScreen.fxml"));
            Parent root = loader.load();
            StartScreenController startScreenController = loader.getController();
            startScreenController.passMenu(this.menu);
            startScreenController.passRestaurant(restaurant);

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
            errorLabel.setVisible(true);
        }
    }
    public void switchToMenuScreen() {
        try {
            Window mainWindow = backBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/MenuScreen.fxml"));
            Parent root = loader.load();
            MenuScreenController menuScreenController = loader.getController();
            menuScreenController.passRestaurant(restaurant);
            menuScreenController.passMenu(this.menu);
            menuScreenController.initialiseTable(this.tbl);
            menuScreenController.initialiseCustomer();

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            displayCommonError();
        }
    }

    //Passing Data between Screens
    public void passMenu(Menu menu){
        this.menu = menu;
    }
    public void passRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

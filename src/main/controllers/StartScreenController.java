package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.entities.Item;
import main.entities.Menu;
import main.entities.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {

    public Button orderFoodBtn;
    public Button manageBtn;
    public Label errorLabel;
    private Menu menu = new Menu();
    public Scene scene;
    public Stage primaryStage;

    private Restaurant restaurant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);

            this.menu.addItem(new Item(1,"Springrolls",3.00,100,"Veggie crispy Springrolls", Item.Course.STARTER, new ArrayList<Item.ItemType>(List.of(Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)), "src/resources/images/Springrolls.jpg"));
            this.menu.addItem(new Item(2,"Popcorn Chicken",3.00,57,"12 pieces of crispy popcorn chicken", Item.Course.STARTER, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE)),"src/resources/images/Popcorn Chicken.jpg"));
            this.menu.addItem(new Item(3,"Onion Bhajis",2.50,70,"Crispy Onion Bhajis served with chutney", Item.Course.STARTER, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Onion Bhajis.jpg"));
            this.menu.addItem(new Item(4,"Potato Wedges",2.50,70,"Crispy Potato Wedges", Item.Course.STARTER, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Potato Wedges.jpg"));

            this.menu.addItem(new Item(5,"Fish and Chips",7.00,400,"Haddock Fish with chips", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE)),"src/resources/images/Fish and Chips.jpg"));
            this.menu.addItem(new Item(6,"Soup",5.00,450,"Tomato Chilli Soup", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE,Item.ItemType.VEGETARIAN)),"src/resources/images/Soup.jpg"));
            this.menu.addItem(new Item(7,"Pasta",5.50,423,"Tomato Pasta", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Pasta.jpg"));
            this.menu.addItem(new Item(8,"Spaghetti",5.50,376,"Cheesy Tomato Spaghetti", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Spaghetti.jpg"));
            this.menu.addItem(new Item(9,"Pizza",7.00,474,"Cheesy Pizza", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.VEGETARIAN)),"src/resources/images/Pizza.jpg"));
            this.menu.addItem(new Item(10,"Burger and Chips",6.50,503,"Beef Burger with chips", Item.Course.MAIN, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE)),"src/resources/images/Burger and Chips.jpeg"));

            this.menu.addItem(new Item(11,"Cheesecake",3.00,231,"Strawberry cheesecake", Item.Course.DESSERT, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Strawberry Cheesecake.jpg"));
            this.menu.addItem(new Item(13,"Scones",4.50,102,"Soft scones with jam and cream", Item.Course.DESSERT, new ArrayList<>(List.of(Item.ItemType.VEGETARIAN)),"src/resources/images/Scones.jpg"));
            this.menu.addItem(new Item(13,"Ice Cream",4.00,231,"Vanilla Ice Cream", Item.Course.DESSERT, new ArrayList<>(List.of(Item.ItemType.DAIRY_FREE, Item.ItemType.GLUTEN_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Vanilla Ice Cream.jpg"));
            this.menu.addItem(new Item(14,"Cake",4.50,221,"Strawberry cake with our special delicious chocolate icing", Item.Course.DESSERT, new ArrayList<>(List.of(Item.ItemType.VEGETARIAN)),"src/resources/images/Strawberry Cake.jpg"));

            this.menu.addItem(new Item(15,"Spring Water",2.00,199,"Spring water originating from Scotland Highlands", Item.Course.DRINK, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Water.jpg"));
            this.menu.addItem(new Item(16,"Diet Coke",3.50,21,"Coke with zero-sugar ", Item.Course.DRINK, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Diet Coke.jpg"));
            this.menu.addItem(new Item(17,"Sparkling Mineral Water",2.00,96,"Fizzy lemonade water", Item.Course.DRINK, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Sparkling Water.jpg"));
            this.menu.addItem(new Item(18,"Orange Juice",4.50,114,"Freshly squeezed, cold, juicy oranges", Item.Course.DRINK, new ArrayList<>(List.of(Item.ItemType.GLUTEN_FREE, Item.ItemType.DAIRY_FREE, Item.ItemType.VEGETARIAN)),"src/resources/images/Orange Juice.jpg"));
        } catch (Exception e) {
            errorLabel.setText("Error! Something went wrong. Please ask for assistance.");
            errorLabel.setVisible(true);
        }

    }

    //GUI Inputs
    public void orderFoodPressed(ActionEvent actionEvent) {
        switchToBookScreen();
    }
    public void manageBtnPressed(ActionEvent actionEvent) {
        switchToManageScreen();
    }

    //Screen Switching
    public void switchToBookScreen() {
        try {
            Window mainWindow = orderFoodBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/BookTableScreen.fxml"));
            Parent root = loader.load();
            BookTableScreenController bookTableScreenController = loader.getController();

            bookTableScreenController.passMenu(this.menu);
            bookTableScreenController.passRestaurant(restaurant);

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
            errorLabel.setVisible(true);
        }
    }
    public void switchToManageScreen() {
        try {
            Window mainWindow = manageBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/ManageScreen.fxml"));
            Parent root = loader.load();
            ManageScreenController manageScreenController = loader.getController();

            manageScreenController.passMenu(this.menu);
            manageScreenController.passRestaurant(restaurant);
            manageScreenController.displayMenuItems();

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
            errorLabel.setVisible(true);
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

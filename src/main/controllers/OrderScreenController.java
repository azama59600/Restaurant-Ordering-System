package main.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;
import main.entities.*;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;


public class OrderScreenController implements Initializable {
    public Label titleLabel;
    public VBox paymentMethodVBox;
    public Label sceneHeadNumLabel;
    public Label sceneHeadNameLabel;
    public Button cancelBtn;
    public VBox sendKitchenVBox;
    public Label sendingOrderLabel;
    public VBox mainOrderInfoVBox;
    public VBox receiptFooterVBox;
    public Label errorLabel;
    private Restaurant restaurant;
    public Table tbl;
    public Button finishBtn;
    public Label tableNumberLabel;
    public Label totalDinerLabel;
    public HBox orderHBox;
    public Label receiptTotalCalories;
    public Label receiptTotalCost;
    public Label paymentMethodLabel;
    public Label paymentTextLabel;

    private String paymentMethod;
    private Menu menu;

    private Double cashAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            cancelBtn.managedProperty().bind(cancelBtn.visibleProperty());
            receiptFooterVBox.managedProperty().bind(receiptFooterVBox.visibleProperty());
            paymentMethodVBox.managedProperty().bind(paymentMethodVBox.visibleProperty());
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            mainOrderInfoVBox.managedProperty().bind(mainOrderInfoVBox.visibleProperty());
            sendKitchenVBox.managedProperty().bind(sendKitchenVBox.visibleProperty());

            errorLabel.setVisible(false);
            sendKitchenVBox.setVisible(false);
            cancelBtn.setVisible(false);
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //GUI Inputs
    public void finishBtnPressed(ActionEvent actionEvent) {
        try{
            this.restaurant.addTable(this.tbl);
            switchToStartScreen();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void submitOrderBtnPressed() {
        try{
            mainOrderInfoVBox.setVisible(false);
            sendKitchenVBox.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> sendingOrderLabel.setText("ORDER SENT"));
            pause.play();

            PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
            pause2.setOnFinished(event -> switchToPaymentScreen());
            pause2.play();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void cancelBtnPressed(ActionEvent actionEvent) {
        try{
            switchToMenuScreen();
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //Display GUI
    public void displayReceipt(){
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        tableNumberLabel.setText("Table Number: " + this.tbl.getTableID());
        totalDinerLabel.setText("Diner Number: "+this.tbl.getDinerNum());
        displayOrderVBox();
        receiptTotalCalories.setText(this.tbl.calculateOrderCalories() + "Cal");
        receiptTotalCost.setText(gb.format(this.tbl.calculateOrderCost()));
    }
    public void displayPaymentMethod() {
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        paymentMethodLabel.setText("Payment Method: " + this.paymentMethod);
        if (this.paymentMethod.equals("CASH")) {
            Double change =  cashAmount - this.tbl.calculateOrderCost();
            paymentTextLabel.setText("Amount Paid: " + gb.format(cashAmount) + ", Change given: " + gb.format(change));
        } else if (this.paymentMethod.equals("CARD")) {
            paymentTextLabel.setText("Amount Paid: " + gb.format(this.tbl.calculateOrderCost()));
        }

        receiptFooterVBox.setVisible(true);
        paymentMethodVBox.setVisible(true);
    }
    private void displayOrderVBox() {
        orderHBox.getChildren().clear();

        Integer dinerNum = this.tbl.getDinerNum();
        ArrayList<Customer> customerList = this.tbl.getCustomers();

        Iterator<Customer> it = customerList.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();

            displayCustomerOrder(customer);
        }
    }
    public void displayCustomerOrder(Customer customer) {
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        ArrayList<Item> itemList = customer.getMeal();
        Iterator<Item> it2 = itemList.iterator();

        TableView mealTable = new TableView();
        TableColumn<Item,String> itemColumn = new TableColumn<Item,String>("Item");
        TableColumn<Item,Double> priceColumn = new TableColumn<Item,Double>("Price");
        TableColumn<Item,Integer> caloriesColumn = new TableColumn<Item,Integer>("Calories");
        itemColumn.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<Item,Integer>("calories"));
        mealTable.getColumns().addAll(itemColumn,priceColumn,caloriesColumn);

        while (it2.hasNext()) {
            Item item = it2.next();

            mealTable.getItems().add(item);
        }

        VBox customerVBox = new VBox();
        Label mealNumber = new Label("Meal " + customer.getCustomerID());
        Region region = new Region();
        VBox mealStatsVBox = new VBox();
        Label mealCalories = new Label(customer.calculateMealCalories()+"Cal");
        Label mealPrice = new Label(gb.format(customer.calculateMealPrice()));


        customerVBox.getChildren().add(mealNumber);
        customerVBox.getChildren().add(mealTable);
        customerVBox.getChildren().add(region);
        mealStatsVBox.getChildren().add(mealCalories);
        mealStatsVBox.getChildren().add(mealPrice);
        customerVBox.getChildren().add(mealStatsVBox);


        customerVBox.getStyleClass().add("item-vertical-card");
        customerVBox.getStyleClass().add("near-black-background-color");
        mealNumber.getStyleClass().add("item-header");
        mealTable.getStyleClass().add("no-border-table-view");
        mealCalories.getStyleClass().add("item-calories-text");
        mealPrice.getStyleClass().add("item-price-text");

        mealNumber.setMaxWidth(1.7976931348623157E308);

        customerVBox.setVgrow(region, Priority.ALWAYS);
        customerVBox.setVgrow(mealNumber, Priority.ALWAYS);

        mealStatsVBox.setAlignment(Pos.CENTER_RIGHT);

        customerVBox.setMinWidth(250);
        customerVBox.setMaxWidth(250);
        customerVBox.setPrefWidth(250);

        orderHBox.getChildren().add(customerVBox);
        orderHBox.setMargin(customerVBox,new Insets(0,12,0,0));
        customerVBox.setMargin(mealStatsVBox,new Insets(6,6,4,6));
    }
    public void displayCommonError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        errorLabel.setVisible(true);
    }

    //Update GUI
    public void setConfirmDisplay() {
        sceneHeadNumLabel.setText("02");
        sceneHeadNameLabel.setText("CHOOSE MEAL");
        titleLabel.setText("Confirm your Order");
        finishBtn.setText("Submit Order");
        finishBtn.setOnAction(event -> submitOrderBtnPressed());

        cancelBtn.setVisible(true);
    }

    //Screen Switching
    public void switchToMenuScreen() {
        try {
            Window mainWindow = cancelBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/MenuScreen.fxml"));
            Parent root = loader.load();
            MenuScreenController menuScreenController = loader.getController();

            menuScreenController.passRestaurant(restaurant);
            menuScreenController.passMenu(this.menu);
            menuScreenController.passTable(this.tbl);
            menuScreenController.initialiseCustomer();
            menuScreenController.updateAfterMealChange();

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            displayCommonError();
        }
    }
    public void switchToStartScreen() {
        try {
            Window mainWindow = finishBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/StartScreen.fxml"));
            Parent root = loader.load();
            StartScreenController startScreenController = loader.getController();
            startScreenController.passRestaurant(restaurant);

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            displayCommonError();
        }
    }
    public void switchToPaymentScreen() {
        try{
            Window mainWindow = finishBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/PaymentScreen.fxml"));
            Parent root = loader.load();
            PaymentScreenController paymentScreenController = loader.getController();
            paymentScreenController.passRestaurant(this.restaurant);

            paymentScreenController.passTable(this.tbl);
            paymentScreenController.initialiseTableGUI();

            mainWindow.getScene().setRoot(root);
        }catch(IOException e) {
            displayCommonError();
        }
    }

    //Passing Data between Screens
    public void passPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod ;
    }
    public void passRestaurantAndTable(Restaurant restaurant, Table tbl){
        System.out.println(restaurant);
        System.out.println(tbl);
        this.restaurant = restaurant;
        this.tbl = tbl;
    }
    public void passMenu(Menu menu){
        this.menu = menu;
    }
    public void passCashAmount (Double amount) {
        this.cashAmount = amount;
    }
}

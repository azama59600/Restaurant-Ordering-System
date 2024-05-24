package main.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;
import main.entities.Restaurant;
import main.entities.Table;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class PaymentScreenController implements Initializable {
    public Button cashBtn;
    public Button cardBtn;
    public VBox cashVBox;
    public VBox cardVBox;
    public Button payBtn;
    public Label errorLabel;
    public Region separateErrorRegion;
    public TextField cashReceivedTextField;
    public HBox continueHBox;
    public Label cashErrorLabel;
    public Button continueBtn;
    public HBox inputCashAmountHBox;
    public Label cashStatusLabel;
    public Label cashAcceptedLabel;
    public Label cashReceivedLabel;
    public HBox waiterOnlyHBox;

    private Restaurant restaurant;
    public Table tbl;
    public Label paymentAmount;
    public Label cardErrorLabel;
    public TextField holderNameTextField;
    public TextField cardNumTextField;
    public TextField expDayTextField;
    public TextField expMonthTextField;
    public TextField secCodeTextField;

    public GridPane cardInputGridPane;
    public VBox paymentCompleteVBox;
    public Label connectingLabel;
    public HBox payHBox;
    public HBox payOptionsHBox;
    public HBox viewReceiptHBox;
    public Button viewReceiptBtn;

    private String paymentMethod;
    private Double cashAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            waiterOnlyHBox.managedProperty().bind(waiterOnlyHBox.visibleProperty());
            cashReceivedLabel.managedProperty().bind(cashReceivedLabel.visibleProperty());
            cashReceivedTextField.managedProperty().bind(cashReceivedTextField.visibleProperty());
            separateErrorRegion.managedProperty().bind(separateErrorRegion.visibleProperty());
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            payOptionsHBox.managedProperty().bind(payOptionsHBox.visibleProperty());
            cashVBox.managedProperty().bind(cashVBox.visibleProperty());
            cashAcceptedLabel.managedProperty().bind(cashAcceptedLabel.visibleProperty());
            inputCashAmountHBox.managedProperty().bind(inputCashAmountHBox.visibleProperty());
            cardVBox.managedProperty().bind(cardVBox.visibleProperty());
            cardInputGridPane.managedProperty().bind(cardInputGridPane.visibleProperty());
            paymentCompleteVBox.managedProperty().bind(paymentCompleteVBox.visibleProperty());
            viewReceiptHBox.managedProperty().bind(viewReceiptHBox.visibleProperty());
            cardErrorLabel.managedProperty().bind(cardErrorLabel.visibleProperty());
            continueHBox.managedProperty().bind(continueHBox.visibleProperty());
            payHBox.managedProperty().bind(payHBox.visibleProperty());

            cashAcceptedLabel.setVisible(false);
            separateErrorRegion.setVisible(false);
            errorLabel.setVisible(false);
        } catch (Exception e) {
            System.out.println(e);
            displayUnexpectedError();
        }
    }

    //GUI Inputs
    public void cashBtnPressed(ActionEvent actionEvent){
        try {
            removeAllActiveStyle();
            cashBtn.getStyleClass().add("active-tab-btn");

            cardVBox.setVisible(false);
            cashVBox.setVisible(true);
            this.paymentMethod = "CASH";
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void cardBtnPressed(ActionEvent actionEvent){
        try {
            removeAllActiveStyle();
            cardBtn.getStyleClass().add("active-tab-btn");

            cashVBox.setVisible(false);
            cardVBox.setVisible(true);
            viewReceiptHBox.setVisible(false);
            this.paymentMethod = "CARD";
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void continueBtnPressed(ActionEvent actionEvent) {
        try {
            if (checkCashInputValid()) {
                payOptionsHBox.setVisible(false);
                waiterOnlyHBox.setVisible(false);
                continueHBox.setVisible(false);
                cashStatusLabel.setVisible(false);
                cashReceivedLabel.setVisible(false);
                cashReceivedTextField.setVisible(false);
                cashAcceptedLabel.setVisible(true);
                viewReceiptHBox.setVisible(true);
                this.cashAmount = Double.parseDouble(cashReceivedTextField.getText());
                paymentAmount.setText("Amount to Pay: £0.00");
            } else {
                cashErrorLabel.setVisible(true);
            }
        }
        catch(Exception e){
            displayUnexpectedError();
        }
    }
    public void payBtnPressed(ActionEvent actionEvent) {
        try {
            String holderName = holderNameTextField.getText();
            String cardNum = cardNumTextField.getText();
            cardNum = cardNum.replaceAll("\\s+","");
            String expDay = expDayTextField.getText();
            String expMonth = expMonthTextField.getText();
            String secCode = secCodeTextField.getText();

            if (checkCardInputValid(holderName,cardNum,expDay,expMonth,secCode)) {
                payOptionsHBox.setVisible(false);
                payHBox.setVisible(false);
                cardInputGridPane.setVisible(false);
                paymentCompleteVBox.setVisible(true);
                connectingLabel.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> connectingLabel.setText("PAYMENT MADE"));
                pause.play();

                PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
                pause2.setOnFinished(event -> viewReceiptHBox.setVisible(true));
                pause2.play();

                paymentAmount.setText("Amount to Pay: £0.00");
            } else {
                cardErrorLabel.setVisible(true);
            }
        }
        catch(Exception e){
            cardErrorLabel.setText("Error! Please check your card details.");
            cardErrorLabel.setVisible(true);
        }
    }
    public void viewReceiptPressed(ActionEvent actionEvent) {
        try {
            switchToReceiptScreen();
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }

    //Validating User Data
    public Boolean checkCashInputValid() {
        if (cashReceivedTextField.getText().isEmpty()) {
            cashErrorLabel.setText("Error! Please enter the amount of cash received.");
            return false;
        } else if (!checkStringIsDouble(cashReceivedTextField.getText())) {
            cashErrorLabel.setText("Error! Invalid amount entered. Please enter a number");
            return false;
        } else if (Double.parseDouble(cashReceivedTextField.getText()) <= 0.0) {
            cashErrorLabel.setText("Error! Money entered should be positive and greater than 0.");
            return false;
        } else if ((BigDecimal.valueOf(Double.parseDouble(cashReceivedTextField.getText())).scale() > 2)) {
            cashErrorLabel.setText("Error! Invalid amount entered. Should be up to two decimal places (e.g. £1.02)");
            return false;
        } else if (Double.parseDouble(cashReceivedTextField.getText()) < this.tbl.calculateOrderCost()) {
            cashErrorLabel.setText("Error! Insufficient amount entered. Please try again.");
            return false;
        }
        return true;
    }
    public Boolean checkCardInputValid(String holderName, String cardNum, String expDay, String expMonth, String secCode) {
        String expDate = expMonth + expDay;
        if (30 < holderName.length()) {
            cardErrorLabel.setText("Error! Holder's Name should not be larger than 30 characters");
            return false;
        } else if (!(cardNum.length() == 15) && !(cardNum.length() == 16)) {
            cardErrorLabel.setText("Error! Card Number length is wrong, it should be either 15 or 16 digits");
            return false;
        } else if (!(expDay.length() == 2) || !(expMonth.length() == 2)) {
            cardErrorLabel.setText("Error! Expiration date length is is invalid, it should 4 digits (2 digits each)");
            return false;
        } else if (!(secCode.length() == 3) && !(secCode.length() == 4)) {
            cardErrorLabel.setText("Error! Security digit length is invalid, it should 3 or 4 digits");
            return false;
        }

        ArrayList<String> testList = new ArrayList<String>();
        testList.add(cardNum);
        testList.add(expDate);
        testList.add(secCode);
        for (String testStr : testList) {
            for (int i=0; i<testStr.length(); i++) {
                Boolean isDigit = Character.isDigit(testStr.charAt(i));
                if (!isDigit) {
                    cardErrorLabel.setText("Error! Invalid characters entered. Make sure Card Number, Exp. Date and Sec. Code are numbers");
                    return false;
                }

            }
        }
        return true;
    }
    private Boolean checkStringIsDouble(String input){
        try {
            Double amount = Double.parseDouble(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Display GUI
    public void displayUnexpectedError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        separateErrorRegion.setVisible(true);
        errorLabel.setVisible(true);
    }

    //Update GUI
    public void removeAllActiveStyle(){
        cashBtn.getStyleClass().remove("active-tab-btn");
        cardBtn.getStyleClass().remove("active-tab-btn");
    }

    //Screen Switching
    public void switchToReceiptScreen() {
        try {
            Window mainWindow = viewReceiptBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/OrderScreen.fxml"));
            Parent root = loader.load();
            OrderScreenController orderScreenController = loader.getController();
            orderScreenController.passRestaurantAndTable(this.restaurant,this.tbl);
            orderScreenController.passPaymentMethod(paymentMethod);
            if (paymentMethod.equals("CASH")) {
                orderScreenController.passCashAmount(this.cashAmount);
            }
            orderScreenController.displayReceipt();
            orderScreenController.displayPaymentMethod();

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            displayUnexpectedError();
        }
    }

    //Passing Data between Screens
    public void passRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void passTable(Table tbl){
        this.tbl = tbl;
    }
    public void initialiseTableGUI(){
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        String orderCost = gb.format(this.tbl.calculateOrderCost());
        paymentAmount.setText("Amount to Pay: "+orderCost);
    }
}

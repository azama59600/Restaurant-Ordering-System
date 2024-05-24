package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;
import main.entities.Item;
import main.entities.Menu;
import main.entities.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class ManageScreenController implements Initializable {
    public Button closeBtn;
    //edit menu vbox
    public VBox editMenuVBox;
    public VBox itemsVBox;
    public Button addItemBtn;
    //appearance vbox
    public VBox appearanceVBox;
    public Button editMenuNavBtn;
    public Button appearanceNavBtn;
    public VBox statsVBox;
    public VBox manageMainVBox;
    public Button statsNavBtn;
    public ScrollPane menuItemsScrollPane;
    public Label totalOrderNumberLabel;
    public Label totalItemNumberLabel;
    public Label starterTotalItemLabel;
    public Label mainsTotalItemLabel;
    public Label dessertTotalItemLabel;
    public Label drinkTotalItemLabel;
    public Label itemFrequencyTitleLabel;
    public ChoiceBox chooseThemeChoiceBox;

    private final String coreStyle = getClass().getResource("/resources/css/style.css").toExternalForm();
    private final String redDarkTheme = getClass().getResource("/resources/css/redDarkTheme.css").toExternalForm();
    private final String blueLightTheme = getClass().getResource("/resources/css/blueLightTheme.css").toExternalForm();
    public VBox statisticsLeftVBox;
    public Label totalCostNumberLabel;
    public Label starterTotalCostLabel;
    public Label mainsTotalCostLabel;
    public Label dessertTotalCostLabel;
    public Label drinkTotalCostLabel;
    public HBox statsContentHBox;
    public Label noOrdersLabel;
    public VBox orderQuantityContentVBox;
    public HBox orderQuantityContentHBox;
    public VBox chartTableVBox;
    public VBox chartVBox;
    public Label errorLabel;

    private Menu menu;
    private Restaurant restaurant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            manageMainVBox.managedProperty().bind(manageMainVBox.visibleProperty());
            editMenuVBox.managedProperty().bind(editMenuVBox.visibleProperty());
            appearanceVBox.managedProperty().bind(appearanceVBox.visibleProperty());
            statsVBox.managedProperty().bind(statsVBox.visibleProperty());
            statsContentHBox.managedProperty().bind(statsContentHBox.visibleProperty());
            noOrdersLabel.managedProperty().bind(noOrdersLabel.visibleProperty());
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());

            errorLabel.setVisible(false);
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //GUI Inputs
    public void closeBtnPressed(ActionEvent actionEvent) {
        try {
            switchToStartScreen();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void addItemBtnPressed(ActionEvent actionEvent) {
        try{
            ObservableList<String> currStylesheets = addItemBtn.getScene().getStylesheets();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view/AddItemPopUp.fxml"));
            Scene inputScene = null;
            try {
                inputScene = new Scene(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String styleSheet : currStylesheets ) {
                String fileName = styleSheet.substring(styleSheet.lastIndexOf('/'));
                String relativeFilePath = "/resources/css/"+fileName;
                inputScene.getStylesheets().add(getClass().getResource(relativeFilePath).toExternalForm());
            }

            AddItemPopUpController addItemPopUpController = loader.getController();
            addItemPopUpController.setParent(ManageScreenController.this);
            addItemPopUpController.passMenu(menu);

            Stage addItemStage = new Stage();
            addItemStage.setScene(inputScene);
            addItemStage.setWidth(420);
            addItemStage.initModality(Modality.APPLICATION_MODAL);
            addItemStage.show();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void editMenuNavBtnPressed(ActionEvent actionEvent) {
        try{
            manageMainVBox.setVisible(true);
            setAllContentInvisible();
            removeAllActiveStyle();

            editMenuVBox.setVisible(true);
            editMenuNavBtn.getStyleClass().add("active-tab-btn");
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void appearanceNavBtnPressed(ActionEvent actionEvent) {
        try{
            chooseThemeChoiceBox.getItems().clear();
            chooseThemeChoiceBox.getItems().addAll("Light", "Dark");

            ObservableList<String> currStylesheets = addItemBtn.getScene().getStylesheets();
            String lastCSS = currStylesheets.get(currStylesheets.size() - 1);
            String fileName = lastCSS.substring(lastCSS.lastIndexOf('/')).substring(1);
            System.out.println("filename: "+fileName);
            if (fileName.equals("blueLightTheme.css")) {
                chooseThemeChoiceBox.setValue("Light");
            } else if (fileName.equals("redDarkTheme.css")) {
                chooseThemeChoiceBox.setValue("Dark");
            }

            chooseThemeChoiceBox.setOnAction(event -> changeTheme());

            manageMainVBox.setVisible(true);
            setAllContentInvisible();
            removeAllActiveStyle();

            appearanceVBox.setVisible(true);
            appearanceNavBtn.getStyleClass().add("active-tab-btn");
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void statsNavBtnPressed(ActionEvent actionEvent) {
        try {
            NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);
            Integer totalOrders = restaurant.calculateTotalOrders();

            if (totalOrders < 1) {
                statsContentHBox.setVisible(false);
                noOrdersLabel.setVisible(true);
            } else {
                statsContentHBox.setVisible(true);
                noOrdersLabel.setVisible(false);
            }

            Integer totalItemsOrdered = restaurant.calculateTotalItemsOrdered();

            Integer startersTotalOrdersNumber = getCourseTypeFreq(Item.Course.STARTER);
            Integer mainsTotalOrdersNumber = getCourseTypeFreq(Item.Course.MAIN);
            Integer dessertTotalOrdersNumber = getCourseTypeFreq(Item.Course.DESSERT);
            Integer drinksTotalOrdersNumber = getCourseTypeFreq(Item.Course.DRINK);

            double starterTotalCostNumber = getCourseTypeFreqCost(Item.Course.STARTER);
            double mainsTotalCostNumber = getCourseTypeFreqCost(Item.Course.MAIN);
            double dessertTotalCostNumber = getCourseTypeFreqCost(Item.Course.DESSERT);
            double drinksTotalCostNumber= getCourseTypeFreqCost(Item.Course.DRINK);

            double totalItemsCost = starterTotalCostNumber+mainsTotalCostNumber+dessertTotalCostNumber+drinksTotalCostNumber;

            DecimalFormat df = new DecimalFormat("##.##%");
            double starterFreqPercent;
            double mainsFreqPercent;
            double dessertFreqPercent;
            double drinksFreqPercent;
            if (totalItemsOrdered != 0) {
                starterFreqPercent = ((double) startersTotalOrdersNumber / totalItemsOrdered);
                System.out.println("Starter Percent= " + startersTotalOrdersNumber + "/" + totalItemsOrdered + "=" + starterFreqPercent);
                mainsFreqPercent = ((double) mainsTotalOrdersNumber / totalItemsOrdered);
                dessertFreqPercent = ((double) dessertTotalOrdersNumber / totalItemsOrdered);
                drinksFreqPercent = ((double) drinksTotalOrdersNumber / totalItemsOrdered);
            } else {
                starterFreqPercent = 0.0;
                mainsFreqPercent = 0.0;
                dessertFreqPercent = 0.0;
                drinksFreqPercent = 0.0;
            }

            double starterCostPercent;
            double mainsCostPercent;
            double dessertCostPercent;
            double drinksCostPercent;
            if (totalItemsOrdered != 0) {
                starterCostPercent = starterTotalCostNumber / totalItemsCost;
                mainsCostPercent = mainsTotalCostNumber / totalItemsCost;
                dessertCostPercent = dessertTotalCostNumber / totalItemsCost;
                drinksCostPercent = drinksTotalCostNumber / totalItemsCost;
            } else {
                starterCostPercent = 0.0;
                mainsCostPercent = 0.0;
                dessertCostPercent = 0.0;
                drinksCostPercent = 0.0;
            }

            totalOrderNumberLabel.setText("Total Orders: " + totalOrders);
            totalItemNumberLabel.setText("Total Items Ordered: " + totalItemsOrdered);
            starterTotalItemLabel.setText("Starters Ordered: " + startersTotalOrdersNumber + " (" + df.format(starterFreqPercent) + ")");
            mainsTotalItemLabel.setText("Mains Ordered: " + mainsTotalOrdersNumber + " (" + df.format(mainsFreqPercent) + ")");
            dessertTotalItemLabel.setText("Desserts Ordered: " + dessertTotalOrdersNumber + " (" + df.format(dessertFreqPercent) + ")");
            drinkTotalItemLabel.setText("Drinks Ordered: " + drinksTotalOrdersNumber + " (" + df.format(drinksFreqPercent) + ")");

            totalCostNumberLabel.setText("Total Cost of Orders: " + gb.format(totalItemsCost));
            starterTotalCostLabel.setText("Starters Cost: " + gb.format(starterTotalCostNumber) + " (" + df.format(starterCostPercent) + ")");
            mainsTotalCostLabel.setText("Mains Cost: " + gb.format(mainsTotalCostNumber) + " (" + df.format(mainsCostPercent) + ")");
            dessertTotalCostLabel.setText("Desserts Cost: " + gb.format(dessertTotalCostNumber) + " (" + df.format(dessertCostPercent) + ")");
            drinkTotalCostLabel.setText("Drinks Cost: " + gb.format(drinksTotalCostNumber) + " (" + df.format(drinksCostPercent) + ")");

            if (restaurant.getOrderList().size() != 0) {
                displayItemFreqPieChart();
            }

            manageMainVBox.setVisible(true);
            setAllContentInvisible();
            removeAllActiveStyle();

            statsVBox.setVisible(true);
            statsNavBtn.getStyleClass().add("active-tab-btn");
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //Display GUI
    public void displayMenuItems(){
        itemsVBox.getChildren().clear();

        ArrayList<Item> itemList = menu.getItemList();
        Iterator<Item> it = itemList.iterator();
        int count = 0;
        while (it.hasNext()){
            count += 1;
            Item item = it.next();

            displayItem(count,item);
        }
    }
    public void displayItem(int count, Item item){
        HBox itemHBox = new HBox();

        Label itemIdLabel = new Label(item.getItemID().toString());
        Label itemNameLabel = new Label(item.getName());
        Region region = new Region();
        Button deleteItemBtn = new Button("Delete");
        deleteItemBtn.setOnAction(event -> displayDeletePopUp(item));

        itemIdLabel.setAlignment(Pos.CENTER_LEFT);
        itemIdLabel.setContentDisplay(ContentDisplay.CENTER);
        itemNameLabel.setAlignment(Pos.CENTER_LEFT);
        itemNameLabel.setContentDisplay(ContentDisplay.CENTER);

        itemIdLabel.setMaxHeight(1.7976931348623157E308);
        itemNameLabel.setMaxHeight(1.7976931348623157E308);

        deleteItemBtn.getStyleClass().add("row-btn");

        itemHBox.setHgrow(region, Priority.ALWAYS);

        //top, right, bottom, left
        itemHBox.setMargin(itemIdLabel,new Insets(0,7,0,7));

        itemHBox.getChildren().add(itemIdLabel);
        itemHBox.getChildren().add(itemNameLabel);
        itemHBox.getChildren().add(region);
        itemHBox.getChildren().add(deleteItemBtn);

        if (count==1) {
            itemHBox.getStyleClass().add("first-horizontal-row");
        } else {
            itemHBox.getStyleClass().add("horizontal-row");
        }

        itemsVBox.getChildren().add(itemHBox);

    }
    private void displayDeletePopUp(Item item) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Item");
        deleteAlert.setContentText("Are you sure you want to delete this item?");

        DialogPane alertPane = deleteAlert.getDialogPane();
        alertPane.setHeaderText(null);
        alertPane.getStyleClass().add("alert-pane");

        ObservableList<String> currStylesheets = addItemBtn.getScene().getStylesheets();
        String lastCSS = currStylesheets.get(currStylesheets.size() - 1);
        String fileName = lastCSS.substring(lastCSS.lastIndexOf('/')).substring(1);
        System.out.println("filename: "+fileName);
        if (fileName.equals("blueLightTheme.css")) {
            alertPane.getStylesheets().add(
                    getClass().getResource("/resources/css/blueLightTheme.css").toExternalForm());
        } else if (fileName.equals("redDarkTheme.css")) {
            alertPane.getStylesheets().add(
                    getClass().getResource("/resources/css/redDarkTheme.css").toExternalForm());
        }


        Optional<ButtonType> result = deleteAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            this.menu.removeItem(item);
            displayMenuItems();
        }
    }
    public void displayItemFreqPieChart(){
        chartTableVBox.getChildren().clear();
        chartVBox.getChildren().clear();

        ObservableList<PieChart.Data> itemDataList = FXCollections.observableArrayList();

        Iterator<Item> it = menu.getItemList().iterator();
        while (it.hasNext()) {
            Item item = it.next();

            Integer itemFrequency = restaurant.calculateItemFrequency(item);
            System.out.println(itemFrequency);
            String name = item.getName();
            itemDataList.add(new PieChart.Data(name, itemFrequency));
        }

        PieChart itemFreqPieChart = new PieChart(itemDataList) {
            {
                ScrollPane scrollPane = new ScrollPane(getLegend());
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);
                scrollPane.maxWidthProperty().bind(widthProperty());
                setLegend(scrollPane);
            }
        };
        itemFreqPieChart.getStyleClass().add("chart");
        itemFreqPieChart.setPrefHeight(600);
        itemFreqPieChart.setMaxHeight(1.7976931348623157E308);
        itemFreqPieChart.setMaxWidth(1.7976931348623157E308);

        TableView itemOrderedTableView = new TableView<>(itemDataList);
        itemOrderedTableView.setEditable(true);
        itemOrderedTableView.setMaxHeight(1.7976931348623157E308);

        TableColumn<PieChart.Data, String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setPrefWidth(120);
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(false);

        TableColumn<PieChart.Data, Number> totalColumn = new TableColumn<>("Total");
        totalColumn.setPrefWidth(120);
        totalColumn.setCellValueFactory(new PropertyValueFactory("pieValue"));
        totalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        itemOrderedTableView.getColumns().addAll(nameColumn,totalColumn);


        chartVBox.getChildren().add(itemFreqPieChart);
        chartTableVBox.getChildren().add(itemOrderedTableView);

        chartTableVBox.setVgrow(itemOrderedTableView, Priority.ALWAYS);
        chartVBox.setVgrow(itemFreqPieChart, Priority.ALWAYS);
    }
    public void displayCommonError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        errorLabel.setVisible(true);
    }

    //Logic
    public Integer getCourseTypeFreq(Item.Course courseType){
        Integer itemTypeFrequency = 0;

        ArrayList<Item> filteredItemList = menu.filterCourseTypeItems(courseType);

        Iterator<Item> it = filteredItemList.iterator();
        while (it.hasNext()) {
            Item item = it.next();

            Integer itemFreq = restaurant.calculateItemFrequency(item);
            Double itemFreqPrice = itemFreq * item.getPrice();
            itemTypeFrequency += itemFreq;
            System.out.println("item freq: " + itemTypeFrequency);
        }

        return itemTypeFrequency;
    }
    public Double getCourseTypeFreqCost(Item.Course courseType){
        Double itemTypeCost = 0.0;

        ArrayList<Item> filteredItemList = menu.filterCourseTypeItems(courseType);
        Iterator<Item> it = filteredItemList.iterator();
        while (it.hasNext()) {
            Item item = it.next();

            Integer itemFreq = restaurant.calculateItemFrequency(item);
            Double itemFreqPrice = itemFreq * item.getPrice();
            itemTypeCost += itemFreqPrice;
        }

        return itemTypeCost;
    }
    public void changeTheme(){
        try {
            String theme = chooseThemeChoiceBox.getSelectionModel().getSelectedItem().toString();
            System.out.println(theme);

            Scene inputScene = chooseThemeChoiceBox.getScene();

            if (theme.equals("Light")){
                inputScene.getStylesheets().clear();
                inputScene.getStylesheets().add(coreStyle);
                inputScene.getStylesheets().add(blueLightTheme);
            } else if (theme.equals("Dark")){
                inputScene.getStylesheets().clear();
                inputScene.getStylesheets().add(coreStyle);
                inputScene.getStylesheets().add(redDarkTheme);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Update GUI
    public void setAllContentInvisible(){
        editMenuVBox.setVisible(false);
        appearanceVBox.setVisible(false);
        statsVBox.setVisible(false);
    }
    public void removeAllActiveStyle(){
        editMenuNavBtn.getStyleClass().remove("active-tab-btn");
        appearanceNavBtn.getStyleClass().remove("active-tab-btn");
        statsNavBtn.getStyleClass().remove("active-tab-btn");
    }

    //Screen Switching
    public void switchToStartScreen() {
        try {
            Window mainWindow = closeBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/StartScreen.fxml"));
            Parent root = loader.load();
            StartScreenController startScreenController = loader.getController();
            startScreenController.passRestaurant(restaurant);
            startScreenController.passMenu(this.menu);

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            displayCommonError();
        }
    }

    //Passing Data between Screens
    public void passRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void passMenu(Menu menu){
        this.menu = menu;
    }
}

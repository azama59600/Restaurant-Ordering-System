package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Window;
import main.entities.*;
import main.entities.Menu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MenuScreenController implements Initializable {
    public Button backBtn;
    public Button submitBtn;
    public Label currentDinerLabel;
    public Label errorLabel;
    public TableView <Item> orderTableView;
    public TableColumn <Item,String> itemColumn;
    public TableColumn <Item,Double> priceColumn;
    public TableColumn <Item,Integer> caloriesColumn;
    public Button prevCustomerBtn;
    public Button nextCustomerBtn;
    public Label totalCaloriesLabel;
    public Label totalCostLabel;
    public Button starterBtn;
    public Button mainBtn;
    public Button dessertBtn;
    public Button drinkBtn;
    public VBox chooseMealVBox;
    public VBox orderingVBox;
    public CheckBox vegetarianCheckBox;
    public CheckBox glutenFreeCheckBox;
    public CheckBox dairyFreeCheckBox;
    public Button randomBtn;
    public Button sortBtn;
    public Button filterBtn;
    public HBox filtersHBox;
    public HBox sortHBox;
    public HBox menuHBox;
    public HBox menuToolsHBox;
    public ChoiceBox sortByComboBox;
    public ChoiceBox orderByComboBox;

    private Restaurant restaurant;
    private Table tbl;
    private Menu menu;
    private Customer currentCustomer;
    private Integer customerPos = 1;
    private Item.Course course;
    private List<Item.ItemType> filterlist = new ArrayList<Item.ItemType>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            orderingVBox.managedProperty().bind(orderingVBox.visibleProperty());
            chooseMealVBox.managedProperty().bind(chooseMealVBox.visibleProperty());
            sortHBox.managedProperty().bind(sortHBox.visibleProperty());
            filtersHBox.managedProperty().bind(filtersHBox.visibleProperty());
            menuToolsHBox.managedProperty().bind(menuToolsHBox.visibleProperty());

            errorLabel.setVisible(false);
            menuToolsHBox.setVisible(false);

            itemColumn.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));
            caloriesColumn.setCellValueFactory(new PropertyValueFactory<Item,Integer>("calories"));

            sortByComboBox.getItems().addAll("Name", "Calories", "Price");
            orderByComboBox.getItems().addAll("","ASC", "DESC");

            Label emptyTableMessage = new Label("No items have been added");
            emptyTableMessage.getStyleClass().add("empty-table-view-text");
            orderTableView.setPlaceholder(emptyTableMessage);
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //GUI Inputs
    public void backPressed(ActionEvent actionEvent) {
        try {
            switchToBookScreen();
        } catch (Exception e){
            displayCommonError();
        }
    }
    public void submitBtnPressed(ActionEvent actionEvent) {
        try{
            NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

            if (this.tbl.checkForEmptyMeals()){
                errorLabel.setText("Error! Empty meal(s). Please add items to all customer meals.");
                errorLabel.setVisible(true);
            } else {
                switchToOrderScreen();
            }
        } catch (Exception e) {
            displayCommonError();
        }

    }

    public void addBtnPressed(Item item, Label quantityLabel) {
        try {
            this.currentCustomer.addItem(item);
            quantityLabel.setText("" + currentCustomer.calculateItemFrequency(item));
            updateAfterMealChange();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void removeBtnPressed(Item item, Label quantityLabel) {
        try {
            this.currentCustomer.removeItem(item);
            quantityLabel.setText(""+currentCustomer.calculateItemFrequency(item));
            updateAfterMealChange();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    private void updateQuantityBtnPressed(Item item, Label quantityLabel) {
        try{
            System.out.println(""+currentCustomer.calculateItemFrequency(item));
            quantityLabel.setText(""+currentCustomer.calculateItemFrequency(item));
            updateAfterMealChange();
        } catch (Exception e) {
            displayCommonError();
        }
    }

    public void sortBtnPressed(ActionEvent actionEvent) {
        try {
            filtersHBox.setVisible(false);
            if (!sortHBox.isVisible()) {
                sortHBox.setVisible(true);
                sortBtn.getStyleClass().add("active-tab-btn");
                filterBtn.getStyleClass().remove("active-tab-btn");
                sortByComboBox.setOnAction(event -> sortComboBoxPressed());
                orderByComboBox.setOnAction(event -> sortComboBoxPressed());
            } else {
                sortBtn.getStyleClass().remove("active-tab-btn");
                sortHBox.setVisible(false);
            }
        }
        catch (Exception e){
            displayCommonError();
        }
    }
    public void sortComboBoxPressed() {
        if (!sortByComboBox.getSelectionModel().isEmpty() && !orderByComboBox.getSelectionModel().isEmpty()) {
            String sortBy = sortByComboBox.getSelectionModel().getSelectedItem().toString();
            String orderBy = orderByComboBox.getSelectionModel().getSelectedItem().toString();

            System.out.println("SORT BY: " + sortBy);
            System.out.println("ORDER BY: " + orderBy);

            this.menu.sortMenu(sortBy, orderBy);
            displayMenu();
        }
    }
    public void filterBtnPressed(ActionEvent actionEvent) {
        try {
            sortHBox.setVisible(false);
            if (!filtersHBox.isVisible()) {
                filtersHBox.setVisible(true);
                filterBtn.getStyleClass().add("active-tab-btn");
                sortBtn.getStyleClass().remove("active-tab-btn");
            } else {
                filtersHBox.setVisible(false);
                filterBtn.getStyleClass().remove("active-tab-btn");
            }
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void vegetarianCheckBoxPressed(ActionEvent actionEvent) {
        try {
            if(vegetarianCheckBox.isSelected()){
                this.filterlist.add(Item.ItemType.VEGETARIAN);
            } else {
                this.filterlist.remove(Item.ItemType.VEGETARIAN);
            }
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void glutenFreeCheckBoxPressed(ActionEvent actionEvent) {
        try {
            if(glutenFreeCheckBox.isSelected()){
                this.filterlist.add(Item.ItemType.GLUTEN_FREE);
            } else {
                this.filterlist.remove(Item.ItemType.GLUTEN_FREE);
            }
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void dairyFreeCheckBoxPressed(ActionEvent actionEvent) {
        try{
            if(dairyFreeCheckBox.isSelected()){
                this.filterlist.add(Item.ItemType.DAIRY_FREE);
            } else {
                this.filterlist.remove(Item.ItemType.DAIRY_FREE);
            }
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void randomBtnPressed(ActionEvent actionEvent) {
        try{
            this.currentCustomer.getMeal().clear();

            ArrayList<Item> itemList = menu.getItemList();

            for (Item.Course currCourse : Item.Course.values()) {

                List<Item> courseItems = itemList.stream().filter(item -> item.getCourse() == currCourse).collect(Collectors.toList());

                Item randomItem = courseItems.get(new Random().nextInt(courseItems.size()));
                this.currentCustomer.addItem(randomItem);
            }

            updateAfterMealChange();
            starterBtn.fire();
        } catch (Exception e) {
            displayCommonError();
        }
    }

    public void prevCustomerBtnPressed(ActionEvent actionEvent) {
        try {
            this.customerPos = customerPos-1;
            switchCustomer();
            updateQuantityLabels();
            updateOrderTableView();
            updateTotalCaloriesLabel();
            updateTotalCostLabel();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void nextCustomerBtnPressed(ActionEvent actionEvent) {
        try{
            this.customerPos = customerPos+1;
            switchCustomer();
            // UPDATE QUANTITY LABELS
            updateQuantityLabels();
            updateOrderTableView();
            updateTotalCaloriesLabel();
            updateTotalCostLabel();
        } catch (Exception e) {
            displayCommonError();
        }
    }

    public void starterBtnPressed(ActionEvent actionEvent) {
        try {
            removeAllActiveStyle();
            starterBtn.getStyleClass().add("active-tab-btn");

            this.course = Item.Course.STARTER;
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void mainBtnPressed(ActionEvent actionEvent) {
        try {
            removeAllActiveStyle();
            mainBtn.getStyleClass().add("active-tab-btn");

            this.course = Item.Course.MAIN;
            displayMenu();
        } catch (Exception e) {
            System.out.println(e);
            displayCommonError();
        }
    }
    public void dessertBtnPressed(ActionEvent actionEvent) {
        try {
            removeAllActiveStyle();
            dessertBtn.getStyleClass().add("active-tab-btn");

            this.course = Item.Course.DESSERT;
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }
    public void drinkBtnPressed(ActionEvent actionEvent) {
        try {
            removeAllActiveStyle();
            drinkBtn.getStyleClass().add("active-tab-btn");

            this.course = Item.Course.DRINK;
            displayMenu();
        } catch (Exception e) {
            displayCommonError();
        }
    }

    //Display GUI
    public void displayMenu(){
        menuToolsHBox.setVisible(true);
        menuHBox.getChildren().clear();

        ArrayList<Item> itemList = menu.getItemList();
        Iterator<Item> it = itemList.iterator();
        while (it.hasNext()){
            Item item = it.next();

            if (item.getCourse().equals(this.course)){
                if(this.filterlist.isEmpty()){
                    displayMenuItem(item);
                    updateQuantityLabels();
                } else if (item.getTypes().containsAll(this.filterlist)){
                    displayMenuItem(item);
                    updateQuantityLabels();
                }
            }

        }
    }
    public void displayMenuItem(Item item){
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        VBox itemVBox = new VBox();
        HBox itemHeadHBox = new HBox();
        VBox itemDescVBox = new VBox();
        HBox itemFilterHBox = new HBox();
        VBox itemStatsVBox = new VBox();
        HBox itemQuantityHBox = new HBox();

        Label nameLabel = new Label(item.getName());

        FileInputStream input = null;
        try {
            input = new FileInputStream(item.getImageLocation());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Label vegLabel = new Label("Vegetarian");
        Label glutenLabel = new Label("Gluten");
        Label dairyLabel = new Label("Dairy");

        vegLabel.setVisible(false);
        glutenLabel.setVisible(false);
        dairyLabel.setVisible(false);
        if (item.getTypes().contains(Item.ItemType.VEGETARIAN)) {
            vegLabel.setVisible(true);
        }
        if (!item.getTypes().contains(Item.ItemType.GLUTEN_FREE)) {
            glutenLabel.setVisible(true);
        }
        if (!item.getTypes().contains(Item.ItemType.DAIRY_FREE)) {
            dairyLabel.setVisible(true);
        }

        itemFilterHBox.managedProperty().bind(itemFilterHBox.visibleProperty());
        vegLabel.managedProperty().bind(vegLabel.visibleProperty());
        glutenLabel.managedProperty().bind(glutenLabel.visibleProperty());
        dairyLabel.managedProperty().bind(dairyLabel.visibleProperty());

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(itemVBox.widthProperty().subtract(2));
        imageView.setPreserveRatio(true);

        Label descriptionLabel = new Label(item.getDescription());
        Region region = new Region();
        Label caloriesLabel = new Label(item.getCalories().toString()+"Cal");
        Label priceLabel = new Label(gb.format(item.getPrice()));
        Button minusBtn = new Button("-");
        Label quantityLabel = new Label("0");
        Button addBtn = new Button("+");

        Button updateQuantityBtn = new Button();

        minusBtn.setOnAction(event -> removeBtnPressed(item, quantityLabel));
        addBtn.setOnAction(event -> addBtnPressed(item,quantityLabel));
        updateQuantityBtn.setOnAction(event -> updateQuantityBtnPressed(item, quantityLabel));

        updateQuantityBtn.setId("updateQuantityBtnId");

        itemHeadHBox.setMaxWidth(1.7976931348623157E308);
        nameLabel.setMaxWidth(1.7976931348623157E308);
        itemFilterHBox.setMaxWidth(1.7976931348623157E308);
        itemQuantityHBox.setMaxWidth(1.7976931348623157E308);
        minusBtn.setMaxWidth(1.7976931348623157E308);
        addBtn.setMaxWidth(1.7976931348623157E308);

        itemVBox.setVgrow(region, Priority.ALWAYS);
        itemHeadHBox.setHgrow(nameLabel, Priority.ALWAYS);
        itemQuantityHBox.setHgrow(minusBtn, Priority.ALWAYS);
        itemQuantityHBox.setHgrow(addBtn, Priority.ALWAYS);

        descriptionLabel.setWrapText(true);
        descriptionLabel.maxWidthProperty().bind(itemVBox.widthProperty());

        vegLabel.setAlignment(Pos.CENTER);
        glutenLabel.setAlignment(Pos.CENTER);
        dairyLabel.setAlignment(Pos.CENTER);
        itemStatsVBox.setAlignment(Pos.BASELINE_RIGHT);

        itemHeadHBox.getChildren().add(nameLabel);
        itemDescVBox.getChildren().add(descriptionLabel);
        itemFilterHBox.getChildren().add(vegLabel);
        itemFilterHBox.getChildren().add(glutenLabel);
        itemFilterHBox.getChildren().add(dairyLabel);
        itemStatsVBox.getChildren().add(caloriesLabel);
        itemStatsVBox.getChildren().add(priceLabel);
        itemQuantityHBox.getChildren().add(minusBtn);
        itemQuantityHBox.getChildren().add(quantityLabel);
        itemQuantityHBox.getChildren().add(addBtn);
        itemQuantityHBox.getChildren().add(updateQuantityBtn);

        itemVBox.getChildren().add(itemHeadHBox);
        itemVBox.getChildren().add(imageView);
        itemVBox.getChildren().add(itemDescVBox);
        itemVBox.getChildren().add(region);
        itemVBox.getChildren().add(itemFilterHBox);
        itemVBox.getChildren().add(itemStatsVBox);
        itemVBox.getChildren().add(itemQuantityHBox);

        itemVBox.getStyleClass().add("item-vertical-card");
        nameLabel.getStyleClass().add("item-header");
        itemDescVBox.getStyleClass().add("standard-padding");
        itemFilterHBox.getStyleClass().add("filters-box");
        vegLabel.getStyleClass().add("filter-label");
        glutenLabel.getStyleClass().add("filter-label");
        glutenLabel.getStyleClass().add("allergy-warning");
        dairyLabel.getStyleClass().add("filter-label");
        dairyLabel.getStyleClass().add("allergy-warning");
        itemStatsVBox.getStyleClass().add("standard-padding");
        caloriesLabel.getStyleClass().add("item-calories-text");
        priceLabel.getStyleClass().add("item-price-text");

        itemVBox.setMinWidth(250);
        itemVBox.setMaxWidth(250);
        itemVBox.setPrefWidth(250);
        updateQuantityBtn.setMinWidth(0);
        updateQuantityBtn.setPrefWidth(0);
        updateQuantityBtn.setMaxWidth(0);
        updateQuantityBtn.setMinHeight(0);
        updateQuantityBtn.setPrefHeight(0);
        updateQuantityBtn.setMaxHeight(0);

        itemStatsVBox.setSpacing(0);
        itemFilterHBox.setSpacing(3);

        menuHBox.getChildren().add(itemVBox);
        menuHBox.setMargin(itemVBox,new Insets(0,6,0,0));
        itemVBox.setMargin(itemQuantityHBox,new Insets(6,0,1,0));
        itemQuantityHBox.setMargin(quantityLabel,new Insets(0,6,0,6));
    }
    public void displayCommonError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        errorLabel.setVisible(true);
    }

    //Logic
    public void switchCustomer() {
        checkCustomerPos();
        ArrayList<Customer> customerList = this.tbl.getCustomers();
        Iterator<Customer> it = customerList.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            if (customer.getCustomerID().equals(customerPos)) {
                this.currentCustomer = customer;
            }
        }
        updateCurrentDinerLabel();
    }
    public void checkCustomerPos() {
        if (this.customerPos<1) {
            this.customerPos = this.tbl.getDinerNum();
        } else if (this.tbl.getDinerNum()<this.customerPos) {
            this.customerPos = 1;
        }
    }
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }
    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

    //Update GUI
    public void updateAfterMealChange(){
        updateOrderTableView();
        updateTotalCaloriesLabel();
        updateTotalCostLabel();
    }
    public void updateQuantityLabels(){
        for (Node node : getAllNodes(menuHBox)) {
            try {
                Button button = (Button) node.lookup("#updateQuantityBtnId");
                button.fire();
            } catch (Exception e) {
            }
        }
    }
    public void updateOrderTableView() {
        orderTableView.getItems().clear();

        ArrayList<Item> itemList = this.currentCustomer.getMeal();
        Iterator<Item> it2 = itemList.iterator();
        while (it2.hasNext()) {
            Item item = it2.next();
            orderTableView.getItems().add(item);
        }
    }
    public void updateTotalCaloriesLabel(){
        String totalCalories = Integer.toString(currentCustomer.calculateMealCalories());
        totalCaloriesLabel.setText(totalCalories+"Cal");
    }
    public void updateTotalCostLabel(){
        NumberFormat gb = NumberFormat.getCurrencyInstance (Locale.UK);

        String totalCost = gb.format(currentCustomer.calculateMealPrice());
        totalCostLabel.setText(totalCost);
    }
    public void updateCurrentDinerLabel(){
        String customerNum = Integer.toString(this.currentCustomer.getCustomerID());
        currentDinerLabel.setText("Meal "+customerNum+" of "+this.tbl.getDinerNum() + " (Table "+this.tbl.getTableID()+")");
    }
    public void removeAllActiveStyle(){
        starterBtn.getStyleClass().remove("active-tab-btn");
        mainBtn.getStyleClass().remove("active-tab-btn");
        dessertBtn.getStyleClass().remove("active-tab-btn");
        drinkBtn.getStyleClass().remove("active-tab-btn");
    }

    //Screen Switching
    public void switchToBookScreen() {
        try{
            Window mainWindow = backBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/BookTableScreen.fxml"));
            Parent root = loader.load();
            BookTableScreenController bookTableScreenController = loader.getController();
            bookTableScreenController.passMenu(this.menu);
            bookTableScreenController.passRestaurant(restaurant);

            mainWindow.getScene().setRoot(root);
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void switchToOrderScreen() {
        try {
            Window mainWindow = submitBtn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/view/OrderScreen.fxml"));
            Parent root = loader.load();
            OrderScreenController orderScreenController = loader.getController();
            orderScreenController.passRestaurantAndTable(this.restaurant,this.tbl);
            orderScreenController.passMenu(this.menu);
            orderScreenController.setConfirmDisplay();
            orderScreenController.displayReceipt();

            mainWindow.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Passing Data between Screens
    public void passRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void passMenu(Menu menu){
        this.menu = menu;
    }
    public void initialiseTable(Table tbl){
        tbl.addCustomers(tbl.getDinerNum());
        passTable(tbl);
    }
    public void passTable(Table tbl) {
        this.tbl = tbl;
    }
    public void initialiseCustomer(){
        switchCustomer();
        updateTotalCaloriesLabel();
        updateTotalCostLabel();
    }
}
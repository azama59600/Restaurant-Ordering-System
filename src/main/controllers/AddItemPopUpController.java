package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Item;
import main.entities.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddItemPopUpController implements Initializable {

    public TextField idTextField;
    public TextField nameTextField;
    public TextField priceTextField;
    public TextField caloriesTextField;
    public TextArea descriptionTextField;
    public CheckBox starterCheckBox;
    public CheckBox mainCheckBox;
    public CheckBox dessertCheckBox;
    public CheckBox drinkCheckBox;
    public CheckBox vegetarianCheckBox;
    public CheckBox glutenFreeCheckBox;
    public CheckBox dairyFreeCheckBox;
    public TextField imageTextField;
    public Button cancelBtn;
    public Button addBtn;
    public Label errorLabel;
    private ManageScreenController parent;
    private Menu menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }

    //GUI inputs
    public void cancelBtnPressed(ActionEvent actionEvent) {
        try {
            ((Stage)cancelBtn.getScene().getWindow()).close();
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void addBtnPressed(ActionEvent actionEvent) {
        try{
            Integer id = Integer.parseInt(idTextField.getText());
            String name = nameTextField.getText();
            Double price = Double.parseDouble(priceTextField.getText());
            Integer calories = Integer.parseInt(caloriesTextField.getText());
            String description = descriptionTextField.getText();
            String imageLocation = imageTextField.getText();

            if(name.isEmpty() || description.isEmpty() || imageLocation.isEmpty()) {
                errorLabel.setVisible(true);
                errorLabel.setText("Error! All text fields must be filled.");
            } else if (!checkPriceValid(price)) {
                errorLabel.setVisible(true);
                errorLabel.setText("Error! Invalid price. Should be 0 or greater and max 2 d.p.");
            } else if (calories < 0) {
                errorLabel.setVisible(true);
                errorLabel.setText("Error! Invalid calories. Should be 0 or greater");
            } else if (!checkImageLocationValid(imageLocation)) {
                errorLabel.setVisible(true);
                errorLabel.setText("Error! Cannot find Image location.");
            } else if (copyImageToImagesFolder(imageLocation) == "FAILED") {
                errorLabel.setVisible(true);
                errorLabel.setText("Error! Could not retrieve Image");
            } else {
                Path newImagePath = Paths.get(imageLocation);
                imageLocation = "src/resources/images/"+newImagePath.getFileName().toString();
                this.menu.addItem(new Item(id,name,price,calories,description,getCourse(),getTypes(),imageLocation));
                ((Stage)cancelBtn.getScene().getWindow()).close();
                parent.displayMenuItems();
            }
        } catch (Exception e) {
            errorLabel.setText("Error! Please Check your Inputs");
            errorLabel.setVisible(true);
        }

    }
    public void starterCheckBoxPressed(ActionEvent actionEvent) {
        try {
            deselectAllCourseCheckBoxes();
            starterCheckBox.setSelected(true);
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void mainCheckBoxPressed(ActionEvent actionEvent) {
        try{
            deselectAllCourseCheckBoxes();
            mainCheckBox.setSelected(true);
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void dessertCheckBoxPressed(ActionEvent actionEvent) {
        try {
            deselectAllCourseCheckBoxes();
            dessertCheckBox.setSelected(true);
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }
    public void drinkCheckBoxPressed(ActionEvent actionEvent) {
        try {
            deselectAllCourseCheckBoxes();
            drinkCheckBox.setSelected(true);
        } catch (Exception e) {
            displayUnexpectedError();
        }
    }

    //Validating User Data
    private Boolean checkPriceValid(Double price) {
        if (BigDecimal.valueOf(price).scale() > 2) {
            return false;
        } else if (price < 0) {
            return false;
        }
        return true;
    }
    private Boolean checkImageLocationValid(String imageLocation) {
        File file = new File(imageLocation);

        if (!file.exists()){
            return false;
        }
        return true;
    }

    //Logic
    private ArrayList<Item.ItemType> getTypes() {
        ArrayList<Item.ItemType> types = new ArrayList<>();
        if(vegetarianCheckBox.isSelected()){
            types.add(Item.ItemType.VEGETARIAN);
            return types;
        }
        if (glutenFreeCheckBox.isSelected()) {
            types.add(Item.ItemType.GLUTEN_FREE);
            return types;
        }
        if (dairyFreeCheckBox.isSelected()) {
            types.add(Item.ItemType.DAIRY_FREE);
            return types;
        }
        return null;
    }
    private Item.Course getCourse() {
        if(starterCheckBox.isSelected()){
            Item.Course course = Item.Course.STARTER;
            return course;
        } else if (mainCheckBox.isSelected()) {
            Item.Course course = Item.Course.MAIN;
            return course;
        } else if (dessertCheckBox.isSelected()) {
            Item.Course course = Item.Course.DESSERT;
            return course;
        } else if (drinkCheckBox.isSelected()) {
            Item.Course course = Item.Course.DRINK;
            return course;
        }
        return null;
    }
    private String copyImageToImagesFolder(String imageLocation) {
        File source = new File(imageLocation);
        File destination = new File("src/resources/images/");

        try {
            FileUtils.copyFileToDirectory(source, destination);
            return "PASSED";
        } catch (IOException e) {
            return "FAILED";
        }
    }

    //Display GUI
    public void displayUnexpectedError(){
        errorLabel.setText("Error! Could not perform action. Please ask for assistance if it persists.");
        errorLabel.setVisible(true);
    }

    //Update GUI
    private void deselectAllCourseCheckBoxes() {
        mainCheckBox.setSelected(false);
        starterCheckBox.setSelected(false);
        dessertCheckBox.setSelected(false);
        drinkCheckBox.setSelected(false);
    }

    //Passing Data between Screens
    public void setParent(ManageScreenController p) {
        this.parent = p;
    }
    public void passMenu(Menu menu) {
        this.menu = menu;
    }



}

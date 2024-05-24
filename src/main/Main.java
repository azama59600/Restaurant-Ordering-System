package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.controllers.StartScreenController;
import main.entities.Restaurant;
import main.entities.Table;

public class Main extends Application {

    private final String coreStyle = getClass().getResource("/resources/css/style.css").toExternalForm();
    private final String redDarkTheme = getClass().getResource("/resources/css/redDarkTheme.css").toExternalForm();
    private final String blueLightTheme = getClass().getResource("/resources/css/blueLightTheme.css").toExternalForm();

    private Restaurant restaurant = new Restaurant();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/view/StartScreen.fxml"));
        Parent root = loader.load();
        StartScreenController startScreenController = loader.getController();
        startScreenController.passRestaurant(restaurant);

        Scene scene = new Scene(root, 1000, 670);
        scene.getStylesheets().add(coreStyle);
        scene.getStylesheets().add(blueLightTheme);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

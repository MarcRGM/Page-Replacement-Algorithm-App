package com.pagereplacementalgorithm;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Page Replacement Algorithm");
        stage.setResizable(false);


         // Load the splash screen FXML
        Parent splash = FXMLLoader.load(App.class.getResource("splash.fxml"));

        Scene splashScene = new Scene(splash);

        stage.setScene(splashScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.DECORATED);
        stage.show();


        // Load the app FXML
        PauseTransition delay = new PauseTransition(Duration.seconds(5)); //
        delay.setOnFinished(event -> {
            try {
                Parent app = FXMLLoader.load(App.class.getResource("app.fxml"));

                Scene mainScene = new Scene(app);
                stage.setScene(mainScene);
                stage.setResizable(false);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();

    }

    public static void main(String[] args) {
        launch();
    }
}
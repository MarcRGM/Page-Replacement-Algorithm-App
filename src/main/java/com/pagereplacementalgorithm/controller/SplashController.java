package com.pagereplacementalgorithm.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    Text titleText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleText.setY(-200);
        TranslateTransition fall = new TranslateTransition(Duration.seconds(1), titleText);
        fall.setToY(94);

        Timeline bounce = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(titleText.translateYProperty(), 94)),
            new KeyFrame(Duration.seconds(0.1), new KeyValue(titleText.translateYProperty(), 74)),
            new KeyFrame(Duration.seconds(0.2), new KeyValue(titleText.translateYProperty(), 94)),
            new KeyFrame(Duration.seconds(0.3), new KeyValue(titleText.translateYProperty(), 84)),
            new KeyFrame(Duration.seconds(0.4), new KeyValue(titleText.translateYProperty(), 94))
        );

        SequentialTransition animation = new SequentialTransition(fall, bounce);
        animation.play();
    }
}
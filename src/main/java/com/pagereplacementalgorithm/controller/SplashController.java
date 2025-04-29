package com.pagereplacementalgorithm.controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    Rectangle loading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(6), loading);
        slide.setToX(1000);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {
            slide.playFromStart();
        });

        delay.play();

    }
}
package com.pagereplacementalgorithm.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    Label label1, label2, label3, label4, label5, label6, label7, label8, label9, label10,
    label11, label12, label13, label14, label15, label16, label17,label18, label19, label20, label21, label22, label23, label24;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label[] labels = {label1, label2, label3, label4, label5, label6, label7, label8, label9, label10,
                label11, label12, label13, label14, label15, label16, label17,
                label18, label19, label20, label21, label22, label23, label24};

        Timeline timeline = new Timeline();
        int delayBetween = 200; // ms between bounces

        for (int i = 0; i < labels.length; i++) {
            Label label = labels[i];

            // Bounce up
            KeyFrame up = new KeyFrame(
                Duration.millis(i * delayBetween),
                e -> {
                    TranslateTransition upTrans = new TranslateTransition(Duration.millis(250), label);
                    upTrans.setByY(-30); // move up
                    upTrans.setAutoReverse(true);
                    upTrans.setCycleCount(2); // up then back down
                    upTrans.setInterpolator(Interpolator.EASE_BOTH);
                    upTrans.play();
                }
            );

            timeline.getKeyFrames().add(up);
        }

        timeline.play();

    }
}
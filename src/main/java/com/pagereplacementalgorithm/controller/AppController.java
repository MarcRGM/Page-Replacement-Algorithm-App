package com.pagereplacementalgorithm.controller;

import com.pagereplacementalgorithm.algorithms.PageReplacement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    Random random = new Random();
    int[] referenceString = new int[20];
    Timeline timeline = new Timeline();
    Double delay = 0.3;

    @FXML
    private ComboBox<String> algoCombo;

    @FXML
    private Slider frameSlider;

    @FXML
    private Label frameNum, algoInfo, algoLabel;

    @FXML
    private Button generateButton, startButton, restartButton;

    @FXML
    VBox infoVBox;

    @FXML
    HBox refBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCategories();

        // Update the label in real time as the slider changes
        frameSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            frameNum.setText(String.valueOf(newValue.intValue()));
        });

    }

    private void setCategories() {
        algoCombo.getItems().setAll(Arrays.stream(PageReplacement.algoCategories.values())
                .map(PageReplacement.algoCategories::getDisplayName)
                .toArray(String[]::new));
    }

    @FXML
    private void generateClicked() {
        for (int i = 0; i < 20; i++) {
            referenceString[i] = random.nextInt(10); // 0-9
        }

        refBox.getChildren().clear(); // clear old boxes
        for (int num : referenceString) {
            Label box = new Label(String.valueOf(num));
            box.getStyleClass().add("number-box");
            refBox.getChildren().add(box);
        }

    }

    @FXML
    private void startClicked() {
        String selectedName = algoCombo.getValue();

        PageReplacement.algoCategories selectedEnum = Arrays.stream(PageReplacement.algoCategories.values())
            .filter(e -> e.getDisplayName().equals(selectedName))
            .findFirst()
            .orElse(null);

        if (selectedEnum != null) {
            infoVBox.setVisible(true);
            algoLabel.setText(selectedEnum.getDisplayName());
            algoInfo.setText(selectedEnum.getInfo());
        }

        for (int i = 0; i < refBox.getChildren().size(); i++) {
            int index = i;

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * delay), e -> {
                Label label = (Label) refBox.getChildren().get(index);
                label.getStyleClass().add("green-box");
            });

            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();

    }

    @FXML
    private void restartClicked() {}

}
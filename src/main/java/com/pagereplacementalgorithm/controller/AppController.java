package com.pagereplacementalgorithm.controller;

import com.pagereplacementalgorithm.PageResult;
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
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
    private Label frameNum, algoInfo, algoLabel, numFaults;

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
        algoCombo.setValue(PageReplacement.algoCategories.values()[0].getDisplayName());
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

        startButton.setDisable(false);
    }

    @FXML
    private void startClicked() {
        String selectedName = algoCombo.getValue();
        AtomicInteger faultCount = new AtomicInteger();

        PageReplacement.algoCategories selectedEnum = Arrays.stream(PageReplacement.algoCategories.values())
            .filter(e -> e.getDisplayName().equals(selectedName))
            .findFirst()
            .orElse(null);

        if (selectedEnum != null) {
            infoVBox.setVisible(true);
            algoLabel.setText(selectedEnum.getDisplayName());
            algoInfo.setText(selectedEnum.getInfo());

            // Disable buttons during animation
            startButton.setDisable(true);
            restartButton.setDisable(true);

            int delay = 1; // seconds
            Timeline timeline = new Timeline();

            List<PageResult> results;

            switch (selectedEnum) {
                case FIFO:
                    results = PageReplacement.runFIFO(referenceString, (int) frameSlider.getValue());
                    break;
                case LRU:

                    break;
                case OPTIMAL:

                    break;
                default:
                    return;
            }

            for (int i = 0; i < refBox.getChildren().size(); i++) {
                int index = i;
                PageResult result = results.get(i);

                KeyFrame keyFrame = new KeyFrame(Duration.seconds(index * delay), e -> {
                    Label label = (Label) refBox.getChildren().get(index);
                    if (result.isFault) {
                        faultCount.incrementAndGet(); // Increment fault count atomically
                        label.getStyleClass().add("red-box");
                        numFaults.setText(String.valueOf(faultCount.get()));  // Update fault count display
                    } else {
                        label.getStyleClass().add("green-box");
                    }
                });

                timeline.getKeyFrames().add(keyFrame);
            }

            timeline.setOnFinished(e -> restartButton.setDisable(false));
            timeline.play();
        }
    }

    @FXML
    private void restartClicked() {
        restartButton.setDisable(true);
        algoLabel.setVisible(false);
        algoInfo.setVisible(false);
        numFaults.setVisible(false);
        refBox.getChildren().clear(); // clear old boxes
    }

}
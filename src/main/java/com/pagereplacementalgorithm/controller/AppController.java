package com.pagereplacementalgorithm.controller;

import com.pagereplacementalgorithm.algorithms.PageResult;
import com.pagereplacementalgorithm.algorithms.PageReplacement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AppController implements Initializable {

    Random random = new Random();
    int[] referenceString = new int[20];

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
    HBox refBox, framesContainer;


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
        numFaults.setVisible(true);
        generateButton.setDisable(true);
        algoCombo.setDisable(true);
        frameSlider.setDisable(true);
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
            algoLabel.setVisible(true);
            algoInfo.setVisible(true);

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
                    results = PageReplacement.runLRU(referenceString, (int) frameSlider.getValue());
                    break;
                case OPTIMAL:
                    results = PageReplacement.runOptimal(referenceString, (int) frameSlider.getValue());
                    break;
                default:
                    return;
            }

            List<HBox> frameRows = new ArrayList<>();
            framesContainer.getChildren().clear(); // Clear old frames
            framesContainer.setAlignment(Pos.CENTER); // center VBox columns horizontally

            int framesPerColumn = 5;
            int totalSteps = results.size();
            int numColumns = (int) Math.ceil((double) totalSteps / framesPerColumn);

            for (int col = 0; col < numColumns; col++) {
                VBox column = new VBox(6); // spacing between rows in a column
                column.getStyleClass().add("frames-column");

                for (int row = 0; row < framesPerColumn; row++) {
                    int index = col * framesPerColumn + row; // Logic for moving the frame sets
                    if (index >= totalSteps) break;

                    PageResult result = results.get(index);

                    HBox frameRow = new HBox(5); // one full row = [index] + [frame boxes]
                    frameRow.getStyleClass().add("frame-row");
                    frameRow.setVisible(false); // hide until keyframe

                    // Step number box
                    Label stepLabel = new Label(String.valueOf(index + 1));
                    stepLabel.getStyleClass().add("step-box"); // new CSS class
                    frameRow.getChildren().add(stepLabel);

                    // Add each frame value
                    for (int frameVal : result.frameState) {
                        Label frameBox = new Label(frameVal == -1 ? "" : String.valueOf(frameVal));
                        frameBox.getStyleClass().add("frame-box");

                        frameRow.getChildren().add(frameBox);
                    }

                    frameRows.add(frameRow);
                    column.getChildren().add(frameRow);
                }

                framesContainer.getChildren().add(column);
            }

            for (int i = 0; i < refBox.getChildren().size(); i++) {
                int index = i;
                PageResult result = results.get(i);

                KeyFrame keyFrame = new KeyFrame(Duration.seconds(index * delay), e -> {
                Label label = (Label) refBox.getChildren().get(index);
                if (result.isFault) {
                    faultCount.incrementAndGet();
                    label.getStyleClass().add("red-box");
                    numFaults.setText(String.valueOf(faultCount.get()));
                } else {
                    label.getStyleClass().add("green-box");
                }

                HBox row = frameRows.get(index);
                row.setVisible(true);

                // Offset by 1 for the stepLabel in the HBox
                int offset = 1;
                for (int j = 0; j < result.frameState.size(); j++) {
                    Label frameBox = (Label) row.getChildren().get(j + offset);
                    int value = result.frameState.get(j);
                    frameBox.getStyleClass().removeAll("green-box", "red-box"); // Clean old highlight

                    if (result.isFault) {
                        // If there is a fault
                        if (value == referenceString[index]) {
                            frameBox.getStyleClass().add("red-box");
                        }
                    } else {
                        // If there is no fault
                        if (value == referenceString[index]) {
                            frameBox.getStyleClass().add("green-box");
                        }
                    }
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
        framesContainer.getChildren().clear();
        restartButton.setDisable(true);
        generateButton.setDisable(false);
        algoCombo.setDisable(false);
        frameSlider.setDisable(false);
        algoLabel.setVisible(false);
        algoInfo.setVisible(false);
        numFaults.setVisible(false);
        refBox.getChildren().clear(); // clear old boxes
    }

}
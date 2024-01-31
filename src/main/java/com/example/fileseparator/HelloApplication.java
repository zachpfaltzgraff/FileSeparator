package com.example.fileseparator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private String selectedFilePath;
    private int i = 0;
    private String[] fileExtensions = new String[5];
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        Button chooseFileBtn = getStartingFolder(stage, gridPane);

        gridPane.add(chooseFileBtn, 0, 0);
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setMargin(chooseFileBtn, new Insets(15, 0, 0, 15));

        borderPane.getChildren().add(gridPane);

        Scene scene = new Scene(borderPane);
        stage.setTitle("File Separator");
        stage.setScene(scene);
        stage.show();
        stage.setMaxHeight(400);
        stage.setMaxWidth(500);
    }

    private Button getStartingFolder(Stage stage, GridPane gridPane) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Button chooseFileBtn = new Button();
        chooseFileBtn.setText("Choose Starting Folder");
        chooseFileBtn.setMinWidth(225);
        chooseFileBtn.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected File Path: " + selectedFilePath);
                chooseFileBtn.setDisable(true);

                Label label = new Label("Folder Was Selected");
                label.setMinWidth(110);
                gridPane.setMargin(label, new Insets(15, 0, 0, 0));
                gridPane.add(label, 1, 0);

                selectFileExtensions(stage, gridPane);
            }
        });
        return chooseFileBtn;
    }

    private void selectFileExtensions(Stage stage, GridPane gridPane) {
        Button addExtensionBtn = new Button();
        addExtensionBtn.setMinWidth(225);
        addExtensionBtn.setText("Add File Extension (Max 5)");
        gridPane.setMargin(addExtensionBtn, new Insets(0, 0, 0, 15));
        addExtensionBtn.setOnAction(e -> {
            if (!addExtensionBtn.isDisable()) {
                Label label = new Label("Write Extension in the format \"extension\":");
                gridPane.setMargin(label, new Insets(0, 0, 0, 15));
                label.setMinWidth(225);

                TextField textField = new TextField();
                textField.setPromptText("jpeg");
                textField.setMaxWidth(110);
                gridPane.setMargin(textField, new Insets(0, 0, 0, 15));

                Button submitBtn = new Button();
                submitBtn.setText("Submit");
                submitBtn.setMinWidth(110);
                gridPane.setMargin(submitBtn, new Insets(0, 0, 0, 15));
                submitBtn.setOnAction(event -> {
                    submit(event, textField, i);

                    submitBtn.setDisable(true);
                });


                gridPane.add(label, 0, i + 2);
                gridPane.add(textField, 1, i + 2);
                gridPane.add(submitBtn, 2, i + 2);

                i++;
                if (i >= 5) {
                    addExtensionBtn.setDisable(true);
                }
            }
        });
        gridPane.add(addExtensionBtn, 0, 1);

    }

    private void submit(ActionEvent event, TextField textField, int i) {
        fileExtensions[i] = textField.getText();

        System.out.println(fileExtensions[i]);
    }

    public static void main(String[] args) {
        launch();
    }
}
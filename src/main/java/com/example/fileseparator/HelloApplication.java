package com.example.fileseparator;

import javafx.application.Application;
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
import java.io.InputStream;

public class HelloApplication extends Application {
    private String selectedFilePath;
    private int i = 2;
    private String[] Extension = new String[5];
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
        stage.setMaxWidth(400);
    }

    private Button getStartingFolder(Stage stage, GridPane gridPane) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Button chooseFileBtn = new Button();
        chooseFileBtn.setText("Choose Starting Folder");
        chooseFileBtn.setMinWidth(250);
        chooseFileBtn.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected File Path: " + selectedFilePath);
                chooseFileBtn.setDisable(true);

                Label label = new Label("File Was Selected");
                label.setMinWidth(250);
                gridPane.setMargin(label, new Insets(15, 0, 0, 0));
                gridPane.add(label, 1, 0);

                selectFileExtensions(stage, gridPane);
            }
        });
        return chooseFileBtn;
    }

    private void selectFileExtensions(Stage stage, GridPane gridPane) {
        Button addExtensionBtn = new Button();
        addExtensionBtn.setMinWidth(250);
        addExtensionBtn.setText("Add File Extension (Max 5)");
        gridPane.setMargin(addExtensionBtn, new Insets(0, 0, 0, 15));
        addExtensionBtn.setOnAction(e -> {

            if (!addExtensionBtn.isDisable()) {
                Label label = new Label("Write Extension in the format \"extension\":");
                gridPane.setMargin(label, new Insets(0, 0, 0, 15));
                label.setMinWidth(250);
                TextField textField = new TextField();
                textField.setMaxWidth(100);

                gridPane.add(label, 0, i);
                gridPane.add(textField, 1, i);

                i++;
                
                if (i >= 7) {
                    addExtensionBtn.setDisable(true);
                }
            }
        });
        gridPane.add(addExtensionBtn, 0, 1);


    }

    public static void main(String[] args) {
        launch();
    }
}
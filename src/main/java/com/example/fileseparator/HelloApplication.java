package com.example.fileseparator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class HelloApplication extends Application {
    private String parentFilePath;
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
        GridPane.setMargin(chooseFileBtn, new Insets(15, 0, 0, 15));

        borderPane.getChildren().add(gridPane);

        Scene scene = new Scene(borderPane);
        stage.setTitle("File Separator");
        stage.setScene(scene);
        stage.show();
        stage.setMaxHeight(500);
        stage.setMaxWidth(600);
    }

    /**
     * This function gets the starting folder that the user wants to separate the contents of
     *
     * @param stage the stage of the gui
     * @param gridPane the gridpane for the gui
     * @return the file choose button
     */
    private Button getStartingFolder(Stage stage, GridPane gridPane) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Button chooseFileBtn = new Button();
        chooseFileBtn.setText("Choose Starting Folder");
        chooseFileBtn.setMinWidth(225);
        chooseFileBtn.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            if (selectedFile != null) {
                parentFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected File Path: " + parentFilePath);
                chooseFileBtn.setDisable(true);

                Label label = new Label("Folder Was Selected");
                label.setMinWidth(110);
                GridPane.setMargin(label, new Insets(15, 0, 0, 0));
                gridPane.add(label, 1, 0);

                selectFileExtensions(stage, gridPane);
            }
        });
        return chooseFileBtn;
    }

    /**
     * This function lets users select the extensions (up to 5) that they want to separate
     * in the given folder they specified earlier
     *
     * @param stage the stage of the gui
     * @param gridPane the gridpane used in the gui, to add new buttons
     */
    private void selectFileExtensions(Stage stage, GridPane gridPane) {
        Button addExtensionBtn = new Button();
        addExtensionBtn.setMinWidth(225);
        addExtensionBtn.setText("Add File Extension (Max 5)");
        GridPane.setMargin(addExtensionBtn, new Insets(0, 0, 0, 15));
        addExtensionBtn.setOnAction(e -> {
            if (!addExtensionBtn.isDisable()) {
                addExtensionBtn.setDisable(true);
                Label label = new Label("Write Extension in the format \"extension\":");
                GridPane.setMargin(label, new Insets(0, 0, 0, 15));
                label.setMinWidth(225);

                TextField textField = new TextField();
                textField.setPromptText("jpeg");
                textField.setMaxWidth(110);
                GridPane.setMargin(textField, new Insets(0, 0, 0, 15));

                Button submitBtn = new Button();
                submitBtn.setText("Submit");
                submitBtn.setMinWidth(110);
                GridPane.setMargin(submitBtn, new Insets(0, 0, 0, 15));
                submitBtn.setOnAction(event -> {
                    submit(textField, i);

                    submitBtn.setDisable(true);
                    addExtensionBtn.setDisable(false);
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

        Button separateFilesBtn = new Button();
        separateFilesBtn.setMinWidth(110);
        separateFilesBtn.setText("Separate Files");
        GridPane.setMargin(separateFilesBtn, new Insets(0, 0, 0, 15));
        separateFilesBtn.setOnAction(e -> {
            addExtensionBtn.setDisable(true);

            createSubFolders();

            try {
                separateFiles(stage);
            } catch (IOException ex) {
                System.out.println("Error in separating files");
                throw new RuntimeException(ex);
            }

            showSuccess(stage);
        });
        gridPane.add(separateFilesBtn, 1, 1);

    }

    /**
     * Function that adds the extension into the extensions array
     *
     * @param textField the textfield of the extension
     * @param i index for the extensions array
     */
    private void submit(TextField textField, int i) {
        fileExtensions[i - 1] = textField.getText();

        System.out.println("Extension " + i + ": " + fileExtensions[i - 1]);
    }

    /**
     * This function creates the subfolders needed to separate the wanted files
     */
    private void createSubFolders() {
        int k = 0;
        while (fileExtensions[k] != null) {
            File subFolder = new File(parentFilePath, fileExtensions[k]);

            if (subFolder.exists()) {
                System.out.println("Subfolder already exists");
            }
            else {
                boolean success = subFolder.mkdir();

                if (success) {
                    System.out.println("subfolder created successfully");
                }
                else {
                    System.out.println("failed to create subfolder");
                }
            }
            k++;
        }
    }

    /**
     * this function separates the files
     * @param stage the stage
     * @throws IOException for organizeFile
     */
    private void separateFiles(Stage stage) throws IOException {
        File parentFile = new File(parentFilePath);
        File[] unsortedFiles = parentFile.listFiles();

        if (unsortedFiles != null) {
            for(File file : unsortedFiles) {
                if (file.isFile()) {
                    String extension = getFileExtension(file.getName());

                    organizeFile(file, extension, parentFile.toPath());
                }
            }
        }
    }

    /**
     * this function will get the file extension
     *
     * @param fileName the file to find the extension of
     * @return the file extension
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        else {
            return "";
        }
    }

    /**
     * This will organize the files
     *
     * @param file the file to move
     * @param extension the file extension
     * @param destinationPath the destination of the file
     * @throws IOException for the destinationPath
     */
    private void organizeFile(File file, String extension, Path destinationPath) throws IOException {
        for (int k = 0; k < 5; k++) {
            if (extension.equalsIgnoreCase(fileExtensions[k])) {
                Path subFolderPath = destinationPath.resolve(fileExtensions[k]);
                Files.createDirectories(subFolderPath);
                Files.move(file.toPath(), subFolderPath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Successfully moved file: " + file);
            }
        }
    }


    /**
     * This function shows the user that it was a success and gives them a box to close the program
     */
    private void showSuccess(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");

        Label label = new Label("Files are separated");

        Font customFont = Font.font("Fredo", 32);
        label.setTextFill(Color.BLACK);
        label.setFont(customFont);
        alert.setGraphic(label);

        alert.getDialogPane().setMinSize(400, 110);
        alert.getDialogPane().setMaxSize(400, 110);

        ButtonType closeButtonType = new ButtonType("Close");
        alert.getButtonTypes().setAll(closeButtonType);
        alert.showAndWait().ifPresent(buttonType -> {
            stage.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
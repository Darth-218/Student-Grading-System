package edu.nu.sgm.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainViewController {
    private Stage stage;
    private Scene scene;
    private Dialog dialog;
    private Parent root;

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // public void switchToScene2(ActionEvent event) {
    // try {
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-student.fxml"));
    //     javafx.scene.control.DialogPane dialogPane = loader.load();

    //     javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
    //     dialog.setDialogPane(dialogPane);
    //     dialog.setTitle("Add Student");
    //     dialog.initOwner(((javafx.scene.Node)event.getSource()).getScene().getWindow());
    //     dialog.showAndWait();
    // } catch (IOException e) {
    //     e.printStackTrace();
    //     Alert alert = new Alert(AlertType.ERROR, "Failed to load Add Student dialog.");
    //     alert.showAndWait();
    // }

    public void switchToScene2(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("student-view.fxml"));
            stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


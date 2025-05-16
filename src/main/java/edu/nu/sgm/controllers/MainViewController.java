package edu.nu.sgm.controllers;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MainViewController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/edu/nu/sgm/views/main-view.fxml"));
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void SwitchTOAddStudent(ActionEvent event) {
        try {
            // Load the dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-student.fxml"));
            DialogPane dialogPane = loader.load();
            AddStudentController controller = loader.getController();
            
            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Student");
            
            // Set the owner window
            dialog.initOwner(((javafx.scene.Node)event.getSource()).getScene().getWindow());
            // Set the dialog in the controller
            controller.setDialog(dialog);

            // Show dialog and wait for response
            Optional<ButtonType> result = dialog.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Student was added successfully
                Alert alert = new Alert(AlertType.INFORMATION, "Student added successfully!");
                alert.show();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Failed to load Add Student dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
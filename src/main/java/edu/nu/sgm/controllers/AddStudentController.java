package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class AddStudentController {
    
    private StudentService studentservice = new StudentService();
    
    @FXML
    private TextField sf_name;  // matches fx:id in FXML
    @FXML
    private TextField sl_name;  // matches fx:id in FXML
    
    private Dialog<ButtonType> dialog;  // reference to the dialog
    
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }
    
    @FXML
    private void handleConfirm() {
        String firstName = sf_name.getText().trim();
        String lastName = sl_name.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
        boolean success = addStudent(firstName, lastName);
        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add student.");
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialog.setResult(ButtonType.CANCEL);
        dialog.close();
    }
    
    public boolean addStudent(String first_name, String last_name) {
        Student student = new Student(0, first_name, last_name, "");
        return studentservice.addstudent(student);
    }
}
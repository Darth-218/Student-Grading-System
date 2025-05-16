package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class AddStudentController {
    
    private StudentService studentService = new StudentService();
    private Student currentStudent;
    
    @FXML private TextField sf_name;
    @FXML private TextField sl_name;
    
    private Dialog<ButtonType> dialog;
    
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }
    
    public void setStudentData(Student student) {
        this.currentStudent = student;
        sf_name.setText(student.getFirstName());
        sl_name.setText(student.getLastName());
    }
    
    @FXML
    private void handleConfirm() {
        String firstName = sf_name.getText().trim();
        String lastName = sl_name.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }
        
        boolean success;
        if (currentStudent != null) {
            // Editing existing student
            currentStudent.setName(firstName);
            currentStudent.setLastName(lastName);
            success = studentService.updateStudent(currentStudent);
        } else {
            // Adding new student
            success = addStudent(firstName, lastName);
        }
        
        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            showAlert("Database Error", "Failed to save student.");
        }
    }
    
    @FXML
    private void handleCancel() {
        dialog.setResult(ButtonType.CANCEL);
        dialog.close();
    }
    
    private boolean addStudent(String first_name, String last_name) {
        Student student = new Student(0, first_name, last_name, "");
        student.setEmail(student.generateStudentEmail());
        return studentService.addstudent(student);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
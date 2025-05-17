/**
 * @file AddStudentController.java
 * @brief Controller for adding or editing a student via the add-student dialog.
 */

package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

/**
 * @class AddStudentController
 * @brief Handles logic for adding or editing a student from the dialog.
 */
public class AddStudentController {
    
    private StudentService studentService = new StudentService();
    private Student currentStudent;
    
    @FXML private TextField sf_name;
    @FXML private TextField sl_name;
    
    private Dialog<ButtonType> dialog;

    /**
     * @brief Sets the dialog reference for this controller.
     * @param dialog The dialog instance.
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    /**
     * @brief Sets the student data for editing.
     * @param student The student to edit.
     */
    public void setStudentData(Student student) {
        this.currentStudent = student;
        studentService.updateStudent(student);
        sf_name.setText(student.getFirstName());
        sl_name.setText(student.getLastName());
    }

    /**
     * @brief Handles the confirm button click event.
     *        Validates input and adds/updates the student.
     */
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
            currentStudent.setName(firstName);
            currentStudent.setLastName(lastName);
            success = studentService.updateStudent(currentStudent);
        } else {
            success = addStudent(firstName, lastName);
        }
        
        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            showAlert("Database Error", "Failed to save student.");
        }
    }

    /**
     * @brief Handles the cancel button click event.
     *        Closes the dialog.
     */
    @FXML
    private void handleCancel() {
        dialog.setResult(ButtonType.CANCEL);
        dialog.close();
    }

    /**
     * @brief Adds a new student.
     * @param first_name First name.
     * @param last_name Last name.
     * @return true if student was added successfully.
     */
    private boolean addStudent(String first_name, String last_name) {
        Student student = new Student(0, first_name, last_name, "");
        return studentService.addstudent(student);
    }

    /**
     * @brief Shows an alert dialog.
     * @param title Dialog title.
     * @param message Dialog message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
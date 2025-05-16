package edu.nu.sgm.controllers;

import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class EnrollStudents {
    private EnrollmentService es = new EnrollmentService();
    private CourseService cs = new CourseService();
    private StudentService ss = new StudentService();

    @FXML
    private TextField s_id;

    private Dialog<ButtonType> dialog;

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    @FXML
    private void handleConfirm() {

        if (s_id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        int id = Integer.parseInt(s_id.getText().trim());

        boolean success = es.enrollStudent(ss.getStudentById(id), course);
        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add course.");
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
}


package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.services.EnrollmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class EnrollCourse {

    private EnrollmentService es = new EnrollmentService();
    private CourseService cs = new CourseService();

    @FXML
    private TextField c_code;

    private Dialog<ButtonType> dialog;

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    @FXML
    private void handleConfirm() {

        if (c_code.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        int id = Integer.parseInt(c_code.getText().trim());

        boolean success = es.enrollStudent(student, cs.getCourseById(id));
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

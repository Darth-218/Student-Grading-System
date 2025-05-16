
package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.services.EnrollmentService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class EnrollCourse {

    private EnrollmentService ES = new EnrollmentService();

    @FXML
    private TextField c_name; // matches fx:id in FXML
    @FXML
    private TextField c_code; // matches fx:id in FXML
    @FXML
    private TextField c_instractor; // matches fx:id in FXML
    @FXML
    private TextField c_credits; // matches fx:id in FXML

    private Dialog<ButtonType> dialog; // reference to the dialog

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    @FXML
    private void handleConfirm() {
        int code = Integer.parseInt(c_code.getText().trim());

        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        boolean success = ES.enrollStudent(student, selectedCourse);
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

    public boolean addCourse(String code, String name, String instructor, int credits) {
        Course course = new Course(0, code, name, instructor, credits);
        return courseService.addCourse(course);
    }
}

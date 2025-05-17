package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
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
    private Student student;

    @FXML
    private TextField c_code;

    private Dialog<ButtonType> dialog;

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void setStudent(Student student) {
        this.student = student;
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

        // Check if course exists before enrolling
        Course course = cs.getCourseById(id);
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Student not found.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        if (course == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Course not found.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }


        boolean success = es.enrollStudent(student, course);
        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to enroll student.");
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

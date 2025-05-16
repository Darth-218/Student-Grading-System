package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.EnrollmentService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class StudentViewController {

    private Student student;

    @FXML
    private ComboBox<Course> courseComboBox; // Populate this with available courses

    @FXML
    private Button enrollButton;

    private EnrollmentService enrollmentService = new EnrollmentService();

    public void setStudent(Student student) {
        this.student = student;
    }

    @FXML
    private void handleEnroll() {
        Course selectedCourse = 
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a course to enroll.");
            alert.showAndWait();
            return;
        }
        

        boolean success = enrollmentService.enrollStudent(student, selectedCourse);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student enrolled successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enrollment failed.");
            alert.showAndWait();
        }
    }
}
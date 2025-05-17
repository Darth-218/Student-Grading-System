/**
 * @file EnrollStudents.java
 * @brief Controller for enrolling a student in a course from the course view dialog.
 */

package edu.nu.sgm.controllers;

import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.StudentService;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

/**
 * @class EnrollStudents
 * @brief Handles the dialog for enrolling a student in a course.
 */
public class EnrollStudents {
    private EnrollmentService enrollment_service = new EnrollmentService();
    private StudentService student_service = new StudentService();
    private Course course;

    @FXML
    private TextField s_id;

    private Dialog<ButtonType> dialog;

    /**
     * @brief Sets the dialog reference for this controller.
     * @param dialog The dialog instance.
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    /**
     * @brief Sets the course to enroll the student in.
     * @param course The course to enroll in.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @brief Handles the confirm button click event.
     *        Enrolls the student in the course if valid.
     */
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

        // Fix: Ensure the correct student object is passed and course is not null
        Student student = student_service.getStudentById(id);
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

        boolean success = enrollment_service.enrollStudent(student, course);
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

    /**
     * @brief Handles the cancel button click event.
     *        Closes the dialog.
     */
    @FXML
    private void handleCancel() {
        dialog.setResult(ButtonType.CANCEL);
        dialog.close();
    }
}

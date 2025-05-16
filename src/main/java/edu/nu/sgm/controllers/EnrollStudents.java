/**
 * @file EnrollStudents.java
 * @brief Controller for enrolling a student in a course from the course view dialog.
 */

package edu.nu.sgm.controllers;

import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.StudentService;
import edu.nu.sgm.models.Course;
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
    private EnrollmentService es = new EnrollmentService();
    private CourseService cs = new CourseService();
    private StudentService ss = new StudentService();

    @FXML
    private TextField s_id;

    private Dialog<ButtonType> dialog;

    private Course course;

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

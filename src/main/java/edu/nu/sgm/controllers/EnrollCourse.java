/**
 * @file EnrollCourse.java
 * @brief Controller for enrolling a student in a course via dialog.
 */

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

/**
 * @class EnrollCourse
 * @brief Handles the dialog for enrolling a student in a course.
 */
public class EnrollCourse {

    private EnrollmentService es = new EnrollmentService();
    private CourseService cs = new CourseService();
    private Student student;

    @FXML
    private TextField c_code;

    private Dialog<ButtonType> dialog;

    /**
     * @brief Sets the dialog reference for this controller.
     * @param dialog The dialog instance.
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    /**
     * @brief Sets the student to enroll.
     * @param student The student to enroll.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @brief Handles the confirm button click event.
     *        Enrolls the student in the course if valid.
     */
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
        Course course;
        try {
            course = cs.getCourseById(id);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Course not found.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        boolean success = es.enrollStudent(student, course);
        if (success) {
            // Update the student view TableView if possible
            if (student != null) {
                Object controllerObj = null;
                if (dialog != null && dialog.getOwner() != null && dialog.getOwner().getScene() != null) {
                    controllerObj = dialog.getOwner().getScene().getRoot().getProperties().get("controller");
                }
                if (controllerObj instanceof edu.nu.sgm.controllers.StudentViewController svc) {
                    svc.refreshView();
                }
            }
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

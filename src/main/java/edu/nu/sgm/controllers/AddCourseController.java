/**
 * @file AddCourseController.java
 * @brief Controller for adding a new course via the add-course dialog.
 * @author 
 */

package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

/**
 * @class AddCourseController
 * @brief Handles the logic for adding a new course from the dialog.
 */
public class AddCourseController {

    private CourseService courseService = new CourseService();
    private Course current_course; // Track the course being edited

    @FXML
    private TextField c_name; // matches fx:id in FXML
    @FXML
    private TextField c_code; // matches fx:id in FXML
    @FXML
    private TextField c_instructor; // matches fx:id in FXML
    @FXML
    private TextField c_credits; // matches fx:id in FXML

    private Dialog<ButtonType> dialog; // reference to the dialog

    /**
     * @brief Sets the dialog reference for this controller.
     * @param dialog The dialog instance.
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void setCourseData(Course course) {
        this.current_course = course;
        c_name.setText(course.getTitle());
        c_code.setText(course.getCourseCode());
        c_instructor.setText(course.getInstructor());
        c_credits.setText(String.valueOf(course.getCreditHours()));
    }

    /**
     * @brief Handles the confirm button click event.
     *        Validates input and adds or updates the course.
     */
    @FXML
    private void handleConfirm() {
        String name = c_name.getText().trim();
        String code = c_code.getText().trim();
        String instructor = c_instructor.getText().trim();
        String creditsStr = c_credits.getText().trim();
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Credit hours must be an integer.");
            return;
        }

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() || creditsStr.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        boolean success;
        if (current_course != null) {
            current_course.setTitle(name);
            current_course.setCourseCode(code);
            current_course.setInstructor(instructor);
            current_course.setCreditHours(credits);
            success = courseService.updateCourse(current_course);
        } else {
            success = addCourse(name, code, instructor, credits);
        }

        if (success) {
            dialog.setResult(ButtonType.OK);
            dialog.close();
        } else {
            showAlert("Database Error", "Failed to save course.");
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
     * @brief Adds a course using the provided details.
     * @param name Course name.
     * @param code Course code.
     * @param instructor Instructor name.
     * @param credits Credit hours.
     * @return true if course was added successfully.
     */
    public boolean addCourse(String name, String code, String instructor, int credits) {
        Course course = new Course(0, code, name, instructor, credits);
        return courseService.addCourse(course);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
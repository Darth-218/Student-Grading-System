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

    @FXML
    private TextField c_name; // matches fx:id in FXML
    @FXML
    private TextField c_code; // matches fx:id in FXML
    @FXML
    private TextField c_instractor; // matches fx:id in FXML
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

    /**
     * @brief Handles the confirm button click event.
     *        Validates input and adds the course.
     */
    @FXML
    private void handleConfirm() {
        String name = c_name.getText().trim();
        String code = c_code.getText().trim();
        String instructor = c_instractor.getText().trim();
        String creditsStr = c_credits.getText().trim();

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() || creditsStr.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Credit Hours must be a number.");
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        boolean success = addCourse(name, code, instructor, credits);
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
}
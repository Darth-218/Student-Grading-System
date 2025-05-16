/**
 * @file CourseViewController.java
 * @brief Controller for displaying and managing course details and enrolled students.
 */

package edu.nu.sgm.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Collections;

/**
 * @class CourseViewController
 * @brief Handles the course detail view, including enrolled students.
 */
public class CourseViewController implements Initializable {
    @FXML
    private Text c_name;
    @FXML
    private Text c_code;
    @FXML
    private Text c_instractor;
    @FXML
    private Text c_credits;
    @FXML
    private Text c_totals;

    @FXML
    private TableView<Student> enrolledStudentsTable;
    @FXML
    private TableColumn<Student, String> s_name;
    @FXML
    private TableColumn<Student, String> s_id;
    @FXML
    private TableColumn<Student, String> f_grade;
    @FXML
    private TableColumn<Student, String> c_gpa;

    @FXML
    private Button c_edit;
    @FXML
    private Button c_remove;
    @FXML
    private Button s_enroll;
    @FXML
    private Button back;

    private Course course;
    private EnrollmentService enrollmentService = new EnrollmentService();
    private CourseService courseService = new CourseService();
    private StudentService studentService = new StudentService();

    private ObservableList<Student> enrolledStudents = FXCollections.observableArrayList();

    /**
     * @brief Initializes the controller and sets up the table columns.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (enrolledStudentsTable == null) return;
        // Set up columns
        if (s_name != null)
            s_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        if (s_id != null)
            s_id.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getId())));
        if (f_grade != null)
            f_grade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));
        if (c_gpa != null)
            c_gpa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));

        enrolledStudentsTable.setItems(enrolledStudents);
    }

    /**
     * @brief Sets the course and updates the view with course details.
     * @param course The course to display.
     */
    public void setCourse(Course course) {
        this.course = course;
        if (c_name != null) c_name.setText(course.getTitle());
        if (c_code != null) c_code.setText(course.getCourseCode());
        if (c_instractor != null) c_instractor.setText(course.getInstructor());
        if (c_credits != null) c_credits.setText(String.valueOf(course.getCreditHours()));
        // Set c_totals if you have enrollment data
        if (c_totals != null) {
            var students = courseService.getStudents(course);
            c_totals.setText(String.valueOf(students.size()));
            enrolledStudents.setAll(students);
        }
    }

    /**
     * @brief Handles the edit course button action.
     */
    @FXML
    private void handleEditCourse() {
        // Implement edit course dialog logic here
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Edit Course not implemented.");
        alert.showAndWait();
    }

    /**
     * @brief Handles the remove course button action.
     */
    @FXML
    private void handleRemoveCourse() {
        if (course == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this course?");
        confirm.setHeaderText(null);
        confirm.setTitle("Remove Course");
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                boolean success = courseService.removeCourse(course);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course removed successfully!");
                    alert.showAndWait();
                    handleBackButton();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to remove course.");
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * @brief Handles the enroll student button action.
     */
    @FXML
    private void handleEnrollStudent() {
        // Implement enroll student dialog logic here
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enroll Student not implemented.");
        alert.showAndWait();
    }

    /**
     * @brief Handles the back button action.
     */
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/main-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to return to main view: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

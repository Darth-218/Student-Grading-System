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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Collections;
import java.util.List;

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
    private Text c_instructor;
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
    private CourseService courseService = new CourseService();

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

        enrolledStudentsTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                try {
                    openGradesView(newSelection);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR, "Failed to open grades view: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * @brief Sets the course and updates the view with course details.
     * @param course The course to display.
     */
    public void setCourse(Course course) {
        this.course = course;
        if (c_name != null) c_name.setText(course.getTitle());
        if (c_code != null) c_code.setText(course.getCourseCode());
        if (c_instructor != null) c_instructor.setText(course.getInstructor());
        if (c_credits != null) c_credits.setText(String.valueOf(course.getCreditHours()));
        // Set c_totals if you have enrollment data
        if (c_totals != null) {
            List<Student> students = courseService.getStudents(course);
            if (students == null) {
                c_totals.setText("0");
                enrolledStudents.setAll(Collections.emptyList());
            } else {
                c_totals.setText(String.valueOf(students.size()));
                enrolledStudents.setAll(students);
            }
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
     * @brief Opens the enroll student dialog and handles course enrollment.
     */
    @FXML
    private void switchToEnrollCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/student-to-course.fxml"));
            DialogPane dialogPane = loader.load();
            EnrollStudents controller = loader.getController();
            controller.setCourse(course); // Pass the current course
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Enroll Student");
            controller.setDialog(dialog);
            dialog.initOwner(s_enroll.getScene().getWindow());
            dialog.showAndWait();
            // Refresh enrolled students after dialog
            if (course != null) {
                var students = courseService.getStudents(course);
                enrolledStudents.setAll(students != null ? students : Collections.emptyList());
                if (c_totals != null) {
                    c_totals.setText(String.valueOf(students != null ? students.size() : 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Enroll Student dialog: " + e.getMessage());
            alert.showAndWait();
        }
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

    /**
     * @brief Opens the grades view for the selected course and current student.
     * @param course The selected course.
     */
    private void openGradesView(Student student) throws IOException {
        if (student == null || course == null) return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/grades-view.fxml"));
            Parent root = loader.load();
            GradesViewController controller = loader.getController();
            controller.setStudentAndCourse(student, course);
            Stage stage = (Stage) enrolledStudentsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Grades for " + course.getTitle());
    }
}

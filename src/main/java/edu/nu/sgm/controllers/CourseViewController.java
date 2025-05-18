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
import edu.nu.sgm.services.GradeItemService;
import edu.nu.sgm.services.EnrollmentService;
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
    private TableView<Student> enrolled_students_table;
    @FXML
    private TableColumn<Student, String> s_name;
    @FXML
    private TableColumn<Student, String> s_id;
    @FXML
    private TableColumn<Student, String> f_grade;

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
    private Button c_edit;
    @FXML
    private Button c_remove;
    @FXML
    private Button s_enroll;
    @FXML
    private Button back;

    private Course course;
    private CourseService course_service = new CourseService();
    private GradeItemService gradeitem_service = new GradeItemService();
    private EnrollmentService enrollment_service = new EnrollmentService();

    private ObservableList<Student> enrolled_students = FXCollections.observableArrayList();

    /**
     * @brief Initializes the controller and sets up the table columns.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        s_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        s_id.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getId())));
        f_grade.setCellValueFactory(data -> {
            Student student = data.getValue();
            if (student != null && course != null) {
                var enrollment = enrollment_service.getEnrollment(student, course);
                if (enrollment != null) {
                    double grade = gradeitem_service.calculateTotalGrade(enrollment);
                    return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", grade));
                }
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        enrolled_students_table.setItems(enrolled_students);

        enrolled_students_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                try {
                    openGradesView(newSelection);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to open grades view: " + e.getMessage());
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
            List<Student> students = course_service.getStudents(course);
            if (students == null) {
                c_totals.setText("0");
                enrolled_students.setAll(Collections.emptyList());
            } else {
                c_totals.setText(String.valueOf(students.size()));
                enrolled_students.setAll(students);
            }
        }
    }

    /**
     * @brief Opens the enroll student dialog and handles course enrollment.
     */
    @FXML
    private void handleEnrollStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/student-to-course.fxml"));
            DialogPane dialogPane = loader.load();
            EnrollStudents controller = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Enroll Student");
            controller.setDialog(dialog);
            controller.setCourse(course);
            dialog.initOwner(s_enroll.getScene().getWindow());
            dialog.showAndWait();
            if (course != null) {
                var students = course_service.getStudents(course);
                enrolled_students.setAll(students != null ? students : Collections.emptyList());
                if (c_totals != null) {
                    c_totals.setText(String.valueOf(students != null ? students.size() : 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Enroll Student dialog: " + e.getMessage());
        }
    }

    /**
     * @brief Handles the edit course button action.
     */
    @FXML
    private void handleEditCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-course.fxml"));
            DialogPane dialogPane = loader.load();
            AddCourseController controller = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Course");
            controller.setDialog(dialog);
            controller.setCourseData(course);
            dialog.initOwner(c_edit.getScene().getWindow());
            dialog.showAndWait();
            Course updated = course_service.getCourseById(course.getId());
            if (updated != null) {
                setCourse(updated);
            } else {
                handleBackButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Edit Course dialog: " + e.getMessage());
            
        }
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
                boolean success = course_service.removeCourse(course);
                if (success) {
                    showAlert("Success", "Course removed successfully!");
                    handleBackButton();
                } else {
                    showAlert("Error", "Failed to remove course.");
                }
            }
        });
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
            Stage stage = (Stage) enrolled_students_table.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Grades for " + course.getTitle());
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
            showAlert("Error", "Failed to return to main view: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}

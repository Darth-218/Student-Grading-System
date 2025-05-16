package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentViewController implements Initializable {

    private Student student;

    @FXML
    private ComboBox<Course> courseComboBox;

    @FXML
    private Button enrollButton;

    @FXML
    private TableView<Course> enrolledCoursesTable;
    @FXML
    private TableColumn<Course, String> c_name;
    @FXML
    private TableColumn<Course, String> c_code;
    @FXML
    private TableColumn<Course, String> f_grade;
    @FXML
    private TableColumn<Course, String> c_gpa;

    @FXML
    private Button c_enroll;

    private EnrollmentService enrollmentService = new EnrollmentService();
    private StudentService studentService = new StudentService();

    private ObservableList<Course> enrolledCourses = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up columns (adjust property names as needed)
        c_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        c_code.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCourseCode()));
        // If you have grade and gpa properties, adjust accordingly:
        f_grade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));
        c_gpa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));

        enrolledCoursesTable.setItems(enrolledCourses);
    }

    public void setStudent(Student student) {
        this.student = student;
        // Load and display enrolled courses for this student
        enrolledCourses.setAll(studentService.getCourses(student));
    }

    @FXML
    private void handleEnroll() {
        Course selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a course to enroll.");
            alert.showAndWait();
            return;
        }

        boolean success = enrollmentService.enrollStudent(student, selectedCourse);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student enrolled successfully!");
            alert.showAndWait();
            // Refresh the enrolled courses table
            enrolledCourses.setAll(studentService.getCourses(student));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enrollment failed.");
            alert.showAndWait();
        }
    }
}
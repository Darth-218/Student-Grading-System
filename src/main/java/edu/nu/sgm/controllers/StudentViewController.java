/**
 * @file StudentViewController.java
 * @brief Controller for displaying and managing student details and enrolled courses.
 */

package edu.nu.sgm.controllers;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Enrollment;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.GradeItemService;
import edu.nu.sgm.services.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @class StudentViewController
 * @brief Handles the student detail view, including enrolled courses.
 */
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

    @FXML
    private Button s_edit;
    @FXML
    private Button back;

    @FXML
    private javafx.scene.text.Text s_name;
    @FXML
    private javafx.scene.text.Text s_id;
    @FXML
    private javafx.scene.text.Text s_email;
    @FXML
    private javafx.scene.text.Text cc_gpa;

    private EnrollmentService enrollmentService = new EnrollmentService();
    private StudentService studentService = new StudentService();
    private GradeItemService gradeitemService = new GradeItemService();

    private ObservableList<Course> enrolledCourses = FXCollections.observableArrayList();

    /**
     * @brief Initializes the controller and sets up the table columns.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up columns (adjust property names as needed)
        c_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        c_code.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCourseCode()));
        f_grade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));
        c_gpa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("0"));

        // Initialize enrolledCoursesTable with the student's enrolled courses if student is set
        if (student != null) {
            var courses = studentService.getCourses(student);
            enrolledCourses.setAll(courses);
        }
        enrolledCoursesTable.setItems(enrolledCourses);
    }

    /**
     * @brief Sets the student and updates the view with student details.
     * @param student The student to display.
     */
    public void setStudent(Student student) {
        this.student = student;
        if (s_name != null)
            s_name.setText(student.getFirstName() + " " + student.getLastName());
        if (s_id != null)
            s_id.setText(String.valueOf(student.getId()));
        if (s_email != null)
            s_email.setText(student.getEmail());
        if (cc_gpa != null) {
            Double cgpa = getCGPA();
            cc_gpa.setText(String.format("%.2f", cgpa));
        }
        // Load and display enrolled courses for this student
        List<Course> courses = studentService.getCourses(student);
        if (courses == null || courses.size() == 0) {
            enrolledCourses.setAll(Collections.emptyList());
        } else {
            enrolledCourses.setAll(courses);
        }
    }

    /**
     * @brief Calculates the CGPA for the current student.
     * @return The CGPA value.
     */
    private double getCGPA() {
        try {
            List<Course> courses = studentService.getCourses(student);
            if (courses.isEmpty()) return 0.0;

            double totalGPA = 0.0;
            int count = 0;
            
            for (Course course : courses) {
                Enrollment enrollment = new Enrollment(0, student.getId(), course.getId());
                Double grade = gradeitemService.calculateTotalGrade(enrollment);
                totalGPA += (grade / 25); // Convert percentage to GPA (100% = 4.0)
                count++;
            }
            
            double cgpa = count > 0 ? totalGPA / count : 0.0;
            return cgpa;
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * @brief Handles enrolling the student in a selected course.
     */
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

    /**
     * @brief Opens the enroll course dialog and handles student enrollment in a course.
     */
    @FXML
    private void switchToEnrollCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/course-to-student.fxml"));
            DialogPane dialogPane = loader.load();
            EnrollCourse controller = loader.getController();
            controller.setStudent(student); // Pass the current student
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Enroll Course");
            controller.setDialog(dialog);
            dialog.initOwner(c_enroll.getScene().getWindow());
            dialog.showAndWait();
            // Refresh all student UI after dialog (including CGPA and courses)
            setStudent(student);
            // Update the enrolledCoursesTable with the latest courses
            enrolledCourses.setAll(studentService.getCourses(student));
            enrolledCoursesTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Enroll Course dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief Handles removing the student.
     */
    @FXML
    private void handleRemoveStudent() {
        if (student == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this student?");
        confirm.setHeaderText(null);
        confirm.setTitle("Remove Student");
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                boolean success = studentService.removeStudent(student);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student removed successfully!");
                    alert.showAndWait();
                    // Go back to main view
                    handleBackButton();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to remove student.");
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * @brief Handles returning to the main view.
     */
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/main-view.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) back.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to return to main view: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief Handles generating the student's report card.
     */
    @FXML
    private void handleGenerateReport() {
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No student selected.");
            alert.showAndWait();
            return;
        }
        String report = enrollmentService.generateReportCard(student);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, report.isEmpty() ? "No report available." : report);
        alert.setTitle("Report Card");
        alert.setHeaderText("Report Card for " + student.getFirstName() + " " + student.getLastName());
        alert.showAndWait();
    }

    /**
     * @brief Handles editing the student details.
     */
    @FXML
    private void handleEditStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-student.fxml"));
            DialogPane dialogPane = loader.load();
            AddStudentController controller = loader.getController();
            // Ensure setStudent exists in AddStudentController
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Student");
            controller.setDialog(dialog);
            dialog.initOwner(s_edit.getScene().getWindow());
            dialog.showAndWait();
            // Refresh student info after editing, only if not deleted
            Student updated = studentService.getStudentById(student.getId());
            if (updated != null) {
                setStudent(updated);
            } else {
                // Student was deleted, go back to main view
                handleBackButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Edit Student dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
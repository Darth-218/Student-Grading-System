/**
 * @file StudentViewController.java
 * @brief Controller for displaying and managing student details and enrolled courses.
 */

package edu.nu.sgm.controllers;

import java.io.IOException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @class StudentViewController
 * @brief Handles the student detail view, including enrolled courses.
 */
public class StudentViewController implements Initializable {

    @FXML
    private TableView<Course> enrolled_courses_table;
    @FXML
    private TableColumn<Course, String> c_name;
    @FXML
    private TableColumn<Course, String> c_code;
    @FXML
    private TableColumn<Course, String> f_grade;
    @FXML
    private TableColumn<Course, String> c_gpa;

    @FXML
    private Text s_name;
    @FXML
    private Text s_id;
    @FXML
    private Text s_email;
    @FXML
    private Text cc_gpa;

    @FXML
    private Button c_enroll;
    @FXML
    private Button s_edit;
    @FXML
    private Button back;

    private Student student;
    private EnrollmentService enrollment_service = new EnrollmentService();
    private StudentService student_service = new StudentService();
    private GradeItemService gradeitem_service = new GradeItemService();

    private ObservableList<Course> enrolled_courses = FXCollections.observableArrayList();

    /**
     * @brief Initializes the controller and sets up the table columns.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        c_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        c_code.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCourseCode()));
        f_grade.setCellValueFactory(data -> {
            Course course = data.getValue();
            if (student != null && course != null) {
                Enrollment enrollment = enrollment_service.getEnrollment(student, course);
                if (enrollment != null) {
                    double grade = gradeitem_service.calculateTotalGrade(enrollment);
                    return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", grade));
                }
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        c_gpa.setCellValueFactory(data -> {
            Course course = data.getValue();
            if (student != null && course != null) {
                Enrollment enrollment = enrollment_service.getEnrollment(student, course);
                if (enrollment != null) {
                    double grade = gradeitem_service.calculateTotalGrade(enrollment);
                    double gpa = course.getCreditHours() > 0 ? grade / course.getCreditHours() : 0.0;
                    return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", gpa));
                }
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        enrolled_courses_table.setItems(enrolled_courses);

        enrolled_courses_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
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
        List<Course> courses = student_service.getCourses(student);
        if (courses == null || courses.size() == 0) {
            enrolled_courses.setAll(Collections.emptyList());
        } else {
            enrolled_courses.setAll(courses);
        }
    }

    /**
     * @brief Opens the enroll course dialog and handles student enrollment in a course.
     */
    @FXML
    private void handleEnrollCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/course-to-student.fxml"));
            DialogPane dialogPane = loader.load();
            EnrollCourse controller = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Enroll Course");
            controller.setDialog(dialog);
            controller.setStudentData(student);
            dialog.initOwner(c_enroll.getScene().getWindow());
            dialog.showAndWait();
            enrolled_courses.setAll(student_service.getCourses(student));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Enroll Course dialog: " + e.getMessage());
        }
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
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Student");
            controller.setDialog(dialog);
            controller.setStudentData(student);
            dialog.initOwner(s_edit.getScene().getWindow());
            dialog.showAndWait();
            Student updated = student_service.getStudentById(student.getId());
            if (updated != null) {
                setStudent(updated);
            } else {
                handleBackButton();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Edit Student dialog: " + e.getMessage());
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
                boolean success = student_service.removeStudent(student);
                if (success) {
                    showAlert("Success", "Student removed successfully!");
                    handleBackButton();
                } else {
                    showAlert("Error", "Failed to remove student.");
                }
            }
        });
    }

    /**
     * @brief Opens the grades view for the selected course and current student.
     * @param course The selected course.
     */
    private void openGradesView(Course course) throws IOException {
        if (student == null || course == null) return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/grades-view.fxml"));
            Parent root = loader.load();
            GradesViewController controller = loader.getController();
            controller.setStudentAndCourse(student, course);
            Stage stage = (Stage) enrolled_courses_table.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Grades for " + course.getTitle());
    }

    /**
     * @brief Handles returning to the main view.
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
        String report = enrollment_service.generateReportCard(student);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, report.isEmpty() ? "No report available." : report);
        alert.setTitle("Report Card");
        alert.setHeaderText("Report Card for " + student.getFirstName() + " " + student.getLastName());
        alert.showAndWait();
    }

    /**
     * @brief Calculates the CGPA for the current student.
     * @return The CGPA value.
     */
    private double getCGPA() {
        try {
            List<Course> courses = student_service.getCourses(student);
            if (courses == null || courses.isEmpty()) return 0.0;

            double totalWeightedGPA = 0.0;
            double totalCredits = 0.0;

            for (Course course : courses) {
                Enrollment enrollment = enrollment_service.getEnrollment(student, course);
                if (enrollment != null && course.getCreditHours() > 0) {
                    double grade = gradeitem_service.calculateTotalGrade(enrollment);
                    // Convert percentage to GPA (assuming 100% = 4.0)
                    double gpa = (grade / 100.0) * 4.0;
                    totalWeightedGPA += gpa * course.getCreditHours();
                    totalCredits += course.getCreditHours();
                }
            }

            return totalCredits > 0 ? totalWeightedGPA / totalCredits : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
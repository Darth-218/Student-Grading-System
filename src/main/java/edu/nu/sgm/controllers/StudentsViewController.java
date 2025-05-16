package edu.nu.sgm.controllers;

import edu.nu.sgm.models.*;
import edu.nu.sgm.services.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentsViewController {
    // UI Components
    @FXML private Text s_name;
    @FXML private Text s_id;
    @FXML private Text s_email;
    @FXML private Text cc_gpa;
    @FXML private Button back;
    @FXML private Button s_remove;
    @FXML private Button report_gen;
    @FXML private Button c_enroll;
    @FXML private Button s_edit;
    
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> c_name;
    @FXML private TableColumn<Course, String> c_code;
    @FXML private TableColumn<Course, String> f_grade;
    @FXML private TableColumn<Course, Double> c_gpa;
    
    // Services
    private final StudentService studentService = new StudentService();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final GradeItemService gradeItemService = new GradeItemService();
    private final CourseService courseService = new CourseService();
    
    private Student currentStudent;

    // Initialization method called by FXML loader
    public void initialize() {
        // Initialize table columns
        c_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        c_code.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseCode()));
        f_grade.setCellValueFactory(data -> {
            try {
                Enrollment enrollment = new Enrollment(0, currentStudent.getId(), data.getValue().getId());
                Double grade = gradeItemService.calculateTotalGrade(enrollment);
                return new SimpleStringProperty(String.format("%.2f%%", grade));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });
        c_gpa.setCellValueFactory(data -> {
            try {
                Enrollment enrollment = new Enrollment(0, currentStudent.getId(), data.getValue().getId());
                Double grade = gradeItemService.calculateTotalGrade(enrollment);
                return new SimpleDoubleProperty(grade / 25).asObject(); // Convert % to GPA (100% = 4.0)
            } catch (SQLException e) {
                return new SimpleDoubleProperty(0.0).asObject();
            }
        });
    }

    // Set the student data when view is opened
    public void setStudentData(Student student) {
        this.currentStudent = student;
        updateUI();
        loadStudentCourses();
    }

    // Update the UI with student information
    private void updateUI() {
        s_name.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
        s_id.setText(String.valueOf(currentStudent.getId()));
        s_email.setText(currentStudent.getEmail());
        cc_gpa.setText(String.format("CGPA: %.2f", calculateCGPA()));
    }

    // Load the student's enrolled courses
    private void loadStudentCourses() {
        try {
            List<Course> courses = studentService.getCourses(currentStudent);
            ObservableList<Course> courseItems = FXCollections.observableArrayList(courses);
            coursesTable.setItems(courseItems);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load courses: " + e.getMessage()).show();
        }
    }

    // Calculate the student's CGPA
    private double calculateCGPA() {
        try {
            List<Course> courses = studentService.getCourses(currentStudent);
            if (courses.isEmpty()) return 0.0;

            double totalGPA = 0.0;
            int count = 0;
            
            for (Course course : courses) {
                Enrollment enrollment = new Enrollment(0, currentStudent.getId(), course.getId());
                Double grade = gradeItemService.calculateTotalGrade(enrollment);
                totalGPA += (grade / 25); // Convert percentage to GPA (100% = 4.0)
                count++;
            }
            
            double cgpa = count > 0 ? totalGPA / count : 0.0;
            currentStudent.setCGPA(cgpa);
            return cgpa;
        } catch (SQLException e) {
            return 0.0;
        }
    }

    // Handle back button action
    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    // Handle remove student action
    @FXML
    private void handleRemoveStudent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Student");
        alert.setContentText("Are you sure you want to delete " + currentStudent.getFirstName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (studentService.removeStudent(currentStudent)) {
                handleBackButton();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete student").show();
            }
        }
    }

    // Handle generate report action
    @FXML
    private void handleGenerateReport() {
        try {
            String report = enrollmentService.generateReportCard(currentStudent);
            if (report == null || report.isEmpty()) {
                throw new Exception("No courses enrolled");
            }

            TextArea textArea = new TextArea(report);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(textArea, 500, 400));
            stage.setTitle("Report Card - " + currentStudent.getFirstName());
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, 
                     "Failed to generate report: " + e.getMessage())
                .show();
        }
    }

    // Handle enroll course action
    @FXML
    private void handleEnrollCourse() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/course-to-student.fxml"));
        Parent root = loader.load();
        
        CourseToStudentController controller = loader.getController();
        controller.setStudent(currentStudent);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Enroll Course");
        stage.showAndWait();
        
        // Refresh data after enrollment
        refreshData();
    }

    // Handle edit student action
    @FXML
    private void handleEditStudent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-student.fxml"));
        DialogPane dialogPane = loader.load();
        
        AddStudentController controller = loader.getController();
        controller.setStudentData(currentStudent);
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Student");
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            refreshData();
        }
    }

    // Refresh all data from database
    private void refreshData() {
        currentStudent = studentService.getStudentById(currentStudent.getId());
        updateUI();
        loadStudentCourses();
    }
}
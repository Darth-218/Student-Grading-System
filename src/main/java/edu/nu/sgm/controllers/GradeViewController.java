package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.GradeItemService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

public class GradeViewController {
    private Student student;
    private GradeItem gradeItem;
    private Course course;

    @FXML private Text s_name;
    @FXML private Text c_name;
    @FXML private Text c_code;
    @FXML private Text g_name;
    @FXML private Text g_score;
    @FXML private Text g_s_max;
    @FXML private Text g_weight;
    @FXML private Text g_w_max;
    @FXML private Text f_grade;
    @FXML private Text c_gpa;
    @FXML private Text c_gpa_label;
    @FXML private TextArea g_feedback;
    @FXML private Button g_edit;
    @FXML private Button g_remove;
    @FXML private Button back;

    private final EnrollmentService enrollment_service = new EnrollmentService();
    private final GradeItemService gradeItem_service = new GradeItemService();

    public void setStudentAndCourse(Student student, Course course) {
        this.student = student;
        this.course = course;
        updateView();
    }

    public void setGradeItem(GradeItem gradeItem) {
        this.gradeItem = gradeItem;
        updateView();
    }

    @FXML
    public void initialize() {
        if (g_edit != null) {
            g_edit.setOnAction(_ -> handleEditGrade());
        }
        updateView();
    }

    private void updateView() {
        if (student != null && s_name != null)
            s_name.setText(student.getFirstName() + " " + student.getLastName());
        if (course != null && c_name != null)
            c_name.setText(course.getTitle());
        if (course != null && c_code != null)
            c_code.setText(course.getCourseCode());
        if (gradeItem != null) {
            if (g_name != null) g_name.setText(gradeItem.getTitle());
            if (g_score != null) g_score.setText(String.format("%.2f",gradeItem.getScore()));
            if (g_s_max != null) g_s_max.setText("/" + gradeItem.getMaxScore());
            if (g_weight != null) g_weight.setText(String.format("%.2f", gradeItem.getWeight() * gradeItem.getScore() / gradeItem.getMaxScore()));
            if (g_w_max != null) g_w_max.setText("/" + String.format("%.2f", gradeItem.getWeight()));
            if (g_feedback != null) g_feedback.setText(gradeItem.getFeedback() != null ? gradeItem.getFeedback() : "No feedback");
        }
        // Optionally, show total/final grade and GPA for this enrollment
        if (student != null && course != null) {
            var enrollment = enrollment_service.getEnrollment(student, course);
            if (enrollment != null) {
                double totalGrade = gradeItem_service.calculateTotalGrade(enrollment);
                if (f_grade != null) f_grade.setText(String.format("%.2f", totalGrade));
                if (c_gpa != null) c_gpa.setText(String.format("%.2f", totalGrade / course.getCreditHours()));
            }
        }
    }

    @FXML
    private void handleEditGrade() {
        if (gradeItem == null || student == null || course == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-grade.fxml"));
            javafx.scene.control.DialogPane dialogPane = loader.load();
            AddGradeController controller = loader.getController();
            javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Grade");
            controller.setDialog(dialog);
            controller.setStudentAndCourse(student, course);
            controller.setGradeItem(gradeItem);
            dialog.initOwner(g_edit.getScene().getWindow());
            dialog.showAndWait();
            updateView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Edit Grade dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/grades-view.fxml"));
            Parent root = loader.load();
            GradesViewController controller = loader.getController();
            controller.setStudentAndCourse(student, course);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Grades for " + (course != null ? course.getTitle() : ""));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to return to grades view: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRemove() {
        if (gradeItem == null || student == null || course == null) return;
        var enrollment = enrollment_service.getEnrollment(student, course);
        if (enrollment == null) return;
        boolean success = gradeItem_service.removeGradeItem(gradeItem, enrollment);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Grade removed successfully!");
            alert.showAndWait();
            handleBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to remove grade.");
            alert.showAndWait();
        }
    }
}

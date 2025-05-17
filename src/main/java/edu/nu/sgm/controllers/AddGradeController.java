package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Enrollment;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.EnrollmentService;
import edu.nu.sgm.services.GradeItemService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class AddGradeController {

    @FXML private TextField g_name;
    @FXML private TextField g_cat;
    @FXML private TextField g_s_max;
    @FXML private TextField g_score;
    @FXML private TextField g_w_max;

    private Dialog<ButtonType> dialog;
    private Student student;
    private Course course;
    private Enrollment enrollment;

    private final EnrollmentService enrollment_service = new EnrollmentService();
    private final GradeItemService gradeitem_service = new GradeItemService();

    private GradeItem current_gradeitem = null;

    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    public void setStudentAndCourse(Student student, Course course) {
        this.student = student;
        this.course = course;
        // Find or create the enrollment for this student and course
        this.enrollment = enrollment_service.getEnrollment(this.student, this.course);
        if (this.enrollment == null) {
            // Should not happen if UI is correct, but handle gracefully
            showAlert("Enrollment Error", "Student is not enrolled in this course.");
            if (dialog != null) {
                dialog.setResult(ButtonType.CANCEL);
                dialog.close();
            }
        }
    }

    public void setGradeItem(GradeItem gradeItem) {
        this.current_gradeitem = gradeItem;
        if (gradeItem != null) {
            g_name.setText(gradeItem.getTitle());
            g_cat.setText(gradeItem.getCategory());
            g_score.setText(String.valueOf(gradeItem.getScore()));
            g_s_max.setText(String.valueOf(gradeItem.getMaxScore()));
            g_w_max.setText(String.valueOf(gradeItem.getWeight()));
        }
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleConfirm() {
        // Validate input
        String title = g_name.getText() != null ? g_name.getText().trim() : "";
        String category = g_cat.getText() != null ? g_cat.getText().trim() : "";
        String scoreStr = g_score.getText() != null ? g_score.getText().trim() : "";
        String maxScoreStr = g_s_max.getText() != null ? g_s_max.getText().trim() : "";
        String weightStr = g_w_max.getText() != null ? g_w_max.getText().trim() : "";

        if (title.isEmpty() || category.isEmpty() || scoreStr.isEmpty() || maxScoreStr.isEmpty() || weightStr.isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        double score, maxScore, weight;
        try {
            score = Double.parseDouble(scoreStr);
            maxScore = Double.parseDouble(maxScoreStr);
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Score, Max Score, and Weight must be numbers.");
            return;
        }

        if (maxScore <= 0) {
            showAlert("Input Error", "Max Score must be greater than 0.");
            return;
        }
        if (score < 0 || score > maxScore) {
            showAlert("Input Error", "Score must be between 0 and Max Score.");
            return;
        }
        if (weight < 0 || weight > 100) {
            showAlert("Input Error", "Weight must be between 0 and 100.");
            return;
        }

        // Create and add the grade item
        boolean success;
        if (current_gradeitem != null) {
            current_gradeitem.setTitle(title);
            current_gradeitem.setCategory(category);
            current_gradeitem.setScore(score, maxScore);
            current_gradeitem.setWeight(weight);
            // if (g_feedback != null) editingGradeItem.setFeedback(g_feedback.getText());
            success = gradeitem_service.updateGradeItem(current_gradeitem);
        } else {
            GradeItem gradeItem = new GradeItem(0, title, category, score, maxScore, "", weight);
            success = gradeitem_service.addGradeItem(enrollment, gradeItem);
        }

        if (success) {
            if (dialog != null) {
                dialog.setResult(ButtonType.OK);
                dialog.close();
            }
        } else {
            showAlert("Database Error", "Failed to add grade item.");
        }
    }

    @FXML
    private void handleCancel() {
        if (dialog != null) {
            dialog.setResult(ButtonType.CANCEL);
            dialog.close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}

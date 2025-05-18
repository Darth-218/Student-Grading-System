package edu.nu.sgm.controllers;

import edu.nu.sgm.models.Student;

import java.io.IOException;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.models.Enrollment;
import edu.nu.sgm.services.GradeItemService;
import edu.nu.sgm.services.EnrollmentService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;

public class GradesViewController {
    private Student student;
    private Course course;

    @FXML
    private TableView<GradeItem> grades_table;
    @FXML
    private TableColumn<GradeItem, String> titleCol;
    @FXML
    private TableColumn<GradeItem, String> g_cat;
    @FXML
    private TableColumn<GradeItem, String> g_content;
    @FXML
    private TableColumn<GradeItem, String> g_score;
    @FXML
    private TableColumn<GradeItem, Number> g_m_score;

    private ObservableList<GradeItem> gradeitems = FXCollections.observableArrayList();

    private final GradeItemService gradeitem_service = new GradeItemService();
    private final EnrollmentService enrollment_service = new EnrollmentService();

    @FXML private Button g_add;
    @FXML private Button back;
    @FXML private Text s_name;
    @FXML private Text c_name;
    @FXML private Text c_code;
    @FXML private Text f_grade;
    @FXML private Text c_gpa;

    public void setStudentAndCourse(Student student, Course course) {
        this.student = student;
        this.course = course;
        updateHeader();
        loadGrades();
        if (grades_table != null) {
            grades_table.refresh();
        }
    }

    private void loadGrades() {
        Enrollment enrollment = enrollment_service.getEnrollment(student, course);
        if (enrollment == null) {
            gradeitems.clear();
            if (grades_table != null) grades_table.refresh();
            return;
        }
        gradeitems.setAll(gradeitem_service.getGrades(enrollment));
        if (grades_table != null) {
            grades_table.setItems(gradeitems);
            grades_table.refresh();
        }
    }

    @FXML
    public void initialize() {
        if (titleCol != null)
            titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        if (g_cat != null)
            g_cat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        if (g_content != null)
            g_content.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        if (g_score != null)
            g_score.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getScore())));
        if (g_m_score != null)
            g_m_score.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMaxScore()));
        if (grades_table != null)
            grades_table.setItems(gradeitems);

        grades_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                try {
                    openGradeView(newSelection);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR, "Failed to open grades view: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        updateHeader();
    }

    private void openGradeView(GradeItem gradeItem) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/grade-view.fxml"));
        Parent root = loader.load();
        GradeViewController controller = loader.getController();
        controller.setStudentAndCourse(student, course);
        controller.setGradeItem(gradeItem);
        Stage stage = (Stage) grades_table.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Grade Details");
    }

    private void updateHeader() {
        if (s_name != null && student != null)
            s_name.setText(student.getFirstName() + " " + student.getLastName());
        if (c_name != null && course != null)
            c_name.setText(course.getTitle());
        if (c_code != null && course != null)
            c_code.setText(course.getCourseCode());
        if (f_grade != null && student != null && course != null) {
            Enrollment enrollment = enrollment_service.getEnrollment(student, course);
            if (enrollment != null) {
                double grade = gradeitem_service.calculateTotalGrade(enrollment);
                f_grade.setText(String.format("%.2f", grade));
                if (c_gpa != null) {
                    double gpa = (grade / 100.0) * 4.0;
                    c_gpa.setText(String.format("%.2f", gpa));
                }
            } else {
                f_grade.setText("-");
                if (c_gpa != null) c_gpa.setText("-");
            }
        }
    }

    @FXML
    private void handleAddGrade() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-grade.fxml"));
            DialogPane dialogPane = loader.load();
            AddGradeController controller = loader.getController();
            // You may want to pass enrollment or student/course to the dialog
            controller.setStudentAndCourse(student, course);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Grade");
            dialog.initOwner(g_add.getScene().getWindow());
            controller.setDialog(dialog);
            dialog.showAndWait();
            // Refresh grades and header after adding
            loadGrades();
            updateHeader();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Add Grade dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/student-view.fxml"));
            Parent root = loader.load();
            StudentViewController controller = loader.getController();
            controller.setStudent(student);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Details");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to return to student view: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

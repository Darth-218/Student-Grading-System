package edu.nu.sgm.controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.services.StudentService;

public class MainViewController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Course> c_table;
    @FXML
    private TableColumn<Course, String> c_name;
    @FXML
    private TableColumn<Course, String> c_code;
    @FXML
    private TableColumn<Course, Integer> c_credits;
    @FXML
    private TableColumn<Course, Integer> c_id;

    @FXML
    private TableView<Student> s_table;
    @FXML
    private TableColumn<Student, String> s_name;
    @FXML
    private TableColumn<Student, String> s_id;
    @FXML
    private TableColumn<Student, String> s_email;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private CourseService courseService = new CourseService();

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private StudentService studentService = new StudentService();

    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/edu/nu/sgm/views/main-view.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void SwitchTOAddStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-student.fxml"));
            DialogPane dialogPane = loader.load();
            AddStudentController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Student");

            dialog.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            controller.setDialog(dialog);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                studentList.setAll(studentService.getStudents());
                Alert alert = new Alert(AlertType.INFORMATION, "Student added successfully!");
                alert.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Failed to load Add Student dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void SwitchTOAddCourse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/add-course.fxml"));
            DialogPane dialogPane = loader.load();
            AddCourseController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Course");
            dialog.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            controller.setDialog(dialog);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                courseList.setAll(courseService.getAllCourses());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course added successfully!");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load Add Course dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        c_name.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        c_code.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCourseCode()));
        c_credits.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCreditHours()).asObject());
        c_id.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        courseList.setAll(courseService.getAllCourses());
        c_table.setItems(courseList);

        s_name.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        s_id.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getId())));
        s_email.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        studentList.setAll(studentService.getStudents());
        s_table.setItems(studentList);

        s_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                try {
                    openStudentView(newSelection);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR, "Failed to open student view: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        c_table.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                try {
                    openCourseView(newSelection);
                    // Clear selection so it doesn't trigger again unintentionally
                    c_table.getSelectionModel().clearSelection();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR, "Failed to open course view: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    private void openStudentView(Student student) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/student-view.fxml"));
        Parent root = loader.load();
        // Get the controller and pass the student data
        StudentViewController controller = loader.getController();
        controller.setStudent(student);
        // Switch scene in the current window
        Stage stage = (Stage) s_table.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Student Details");
    }

    private void openCourseView(Course course) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/nu/sgm/views/course-view.fxml"));
        Parent root = loader.load();
        // Get the controller and pass the course data
        CourseViewController controller = loader.getController();
        controller.setCourse(course);
        // Switch scene in the current window
        Stage stage = (Stage) c_table.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Course Details");
    }
}

package edu.nu.sgm.utils;

import edu.nu.sgm.models.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String DB_URL = "jdbc:h2:mem";

  private Object getResults(String query) {
    try (Connection connection = DriverManager.getConnection(DB_URL)) {
      return connection.createStatement().executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
      return "";
    }
  }

  public List<Student> fetchStudents(Course course) {
    String query = (course == null) ? "" : "";
    List<Student> students = new ArrayList<>();
    ResultSet results = (ResultSet)getResults(query);
    while (results.next()) {
      students.add(new Student());
    }
    return students;
  }

  public List<Course> fetchCourses(Student student) {
    String query = "";
    List<Course> courses = new ArrayList<>();
    return courses;
  }

  public List<GradeItem> fetchGrades(Student student, Course course) {
    String query = "";
    List<Course> grades = new ArrayList<>();
    return grades;
  }

  public boolean updateStudents(Student student) {
    String query = "";
    return true;
  }

  public boolean updateCourses(Course course) {
    String query = "";
    return true;
  }

  public boolean updateEnrollments(Enrollment enrollment) {
    String query = "";
    return true;
  }

  public boolean updateGrades(GradeItem grade) {
    String query = "";
    return true;
  }
}

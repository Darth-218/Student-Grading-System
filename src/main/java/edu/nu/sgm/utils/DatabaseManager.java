package edu.nu.sgm.utils;

import edu.nu.sgm.models.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String DB_URL = "jdbc:h2:mem";

  private interface ResultSetMapper<T> {
    T map(ResultSet results) throws SQLException;
  }

  private PreparedStatement parameterizedStatement(PreparedStatement statement,
                                                   Object[] parameters)
      throws SQLException {
    for (int i = 1; i <= parameters.length; i++) {
      statement.setObject(i, parameters[i]);
    }
    return statement;
  }

  private <T> List<T> executeReturn(String query, ResultSetMapper<T> mapper)
      throws SQLException {
    List<T> output = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(DB_URL);
         ResultSet results = connection.createStatement().executeQuery(query)) {
      while (results.next()) {
        output.add(mapper.map(results));
      }
    } catch (SQLException e) {
      throw new SQLException();
    }
    return output;
  }

  private <T> List<T> executeReturn(String query, ResultSetMapper<T> mapper,
                                    Object... parameters) throws SQLException {
    List<T> output = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(DB_URL);
         ResultSet results =
             parameterizedStatement(connection.prepareStatement(
                                        query, Statement.RETURN_GENERATED_KEYS),
                                    parameters)
                 .executeQuery()) {
      while (results.next()) {
        output.add(mapper.map(results));
      }
      return output;
    }
  }

  private int executeUpdate(String query, Object... parameters)
      throws SQLException {
    try (Connection connection = DriverManager.getConnection(DB_URL);
         PreparedStatement statement = parameterizedStatement(
             connection.prepareStatement(query), parameters)) {
      return statement.executeUpdate();
    }
  }

  private int executeInsert(String query, Object... parameters)
      throws SQLException {
    try (Connection connection = DriverManager.getConnection(DB_URL);
         PreparedStatement statement = parameterizedStatement(
             connection.prepareStatement(query), parameters)) {
      if (statement.executeUpdate() == 0)
        throw new SQLException("No rows updated");
      ResultSet generated_keys = statement.getGeneratedKeys();
      return generated_keys.getInt(1);
    }
  }

  public boolean createCourse(Course course) throws SQLException {
    String query = "INSERT INTO courses (course_code, title, credit_hours, "
                   + "instructor, total_students) "
                   + "VALUES (?, ?, ?, ?, ?)";
    int course_id =
        executeInsert(query, course.getCourseCode(), course.getTitle(),
                      course.getCreditHours(), course.getInstructor(),
                      course.getTotalStudents());
    course.setId(course_id);
    return true;
  }

  public boolean createStudent(Student student) throws SQLException {
    String query =
        "INSERT INTO students (first_name, last_name, email) VALUES (?, ?, ?)";
    int student_id = executeInsert(query, student.getFirstName(),
                                   student.getLastName(), student.getEmail());
    student.setId(student_id);
    return true;
  }

  public boolean createEnrollment(Enrollment enrollment) throws SQLException {
    String query = "INSERT INTO enrollments (student_id, course_id, "
                   + "semester) VALUES (?, ?, ?)";
    int enrollment_id =
        executeInsert(query, enrollment.getStudent(), enrollment.getCourse());
    enrollment.setID(enrollment_id);
    return true;
  }

  public boolean createGrade(Enrollment enrollment, GradeItem grade)
      throws SQLException {
    String query = "INSERT INTO grade_items (enrollment_id, title, category, "
                   + "score, max_score, feedback) VALUES (?, ?, ?, ?, ?, ?)";
    int grade_id = executeInsert(query, enrollment.getId(), grade.getTitle(),
                                 grade.getCategory(), grade.getScore(),
                                 grade.getMaxScore(), grade.getFeedback());
    grade.setId(grade_id);
    return true;
  }

  public List<Course> fetchCourses(Student student) throws SQLException {
    String enrollment_query = "SELECT * FROM enrollment WHERE student_id = ?";
    List<Integer> course_ids =
        executeReturn(enrollment_query,
                      results -> results.getInt("course_id"), student.getId());
    String query = "SELECT * FROM courses WHERE id = ?";
    return executeReturn(query,
                         results
                         -> new Course(results.getInt("id"),
                                       results.getString("coures_code"),
                                       results.getString("title"),
                                       results.getString("instructor"),
                                       results.getInt("credit_hours")),
                         course_ids);
  }

  public List<Student> fetchStudents(Course course) throws SQLException {
    String enrollment_query = "SELECT * FROM enrollment WHERE course_id = ?";
    List<Integer> student_ids =
        executeReturn(enrollment_query,
                      results -> results.getInt("student_id"), course.getId());
    String query = "SELECT * FROM students WHERE id = ?";
    return executeReturn(query,
                         results
                         -> new Student(results.getInt("id"),
                                        results.getString("first_name"),
                                        results.getString("last_name"),
                                        results.getString("email")),
                         student_ids);
  }

  public List<Course> fetchCourses() throws SQLException {
    String query = "SELECT * FROM courses";
    return executeReturn(query,
                         results
                         -> new Course(results.getInt("id"),
                                       results.getString("coures_code"),
                                       results.getString("title"),
                                       results.getString("instructor"),
                                       results.getInt("credit_hours")));
  }

  public List<Student> fetchStudents() throws SQLException {
    String query = "SELECT * FROM students";
    return executeReturn(query,
                         results
                         -> new Student(results.getInt("id"),
                                        results.getString("first_name"),
                                        results.getString("last_name"),
                                        results.getString("email")));
  }

  public List<GradeItem> fetchGrades(Enrollment enrollment)
      throws SQLException {
    String query = "SELECT * FROM grade_items WHERE enrollment_id = ?";
    return executeReturn(query, results -> new GradeItem(), enrollment.getId());
  }

  public List<Enrollment> fetchEnrollment(Student student, Course course)
      throws SQLException {
    String query =
        "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
    return executeReturn(query,
                         results
                         -> new Enrollment(results.getInt("id"),
                                           results.getInt("student_id"),
                                           results.getInt("course_id")),
                         student.getId(), course.getId());
  }

  public int updateCourse(Course course) throws SQLException {
    String query =
        "UPDATE courses SET course_code = ?, title = ?, instructor = ?, "
        + "credit_hours = ?, total_students = ? WHERE id = ?";
    return executeUpdate(query, course.getCourseCode(), course.getTitle(),
                         course.getInstructor(), course.getCreditHours(),
                         course.getTotalStudents(), course.getId());
  }

  public int updateStudent(Student student) throws SQLException {
    String query = "UPDATE students SET first_name = ?, last_name = ?, email "
                   + "= ? WHERE id = ?";
    return executeUpdate(query, student.getFirstName(), student.getLastName(),
                         student.getEmail(), student.getId());
  }

  public int updateGrades(GradeItem grade) throws SQLException {
    String query = "UPDATE grade_items SET title = ?, category = ?, score = ?, "
                   + "max_score = ?, feedback = ? WHERE id = ?";
    return executeUpdate(query, grade.getTitle(), grade.getCategory(),
                         grade.getScore(), grade.getMaxScore(),
                         grade.getFeedback(), grade.getId());
  }

  public int deleteCourse(Course course) throws SQLException {
    String query = "DELETE FROM courses WHERE id = ?";
    return executeUpdate(query, course.getId());
  }

  public int deleteStudent(Student student) throws SQLException {
    String query = "DELETE FROM students WHERE id = ?";
    return executeUpdate(query, student.getId());
  }

  public int deleteGrade(GradeItem grade) throws SQLException {
    String query = "DELETE FROM grade_items WHERE id = ?";
    return executeUpdate(query, grade.getId());
  }

  public int deleteEnrollment(Enrollment enrollment) throws SQLException {
    String query = "DELETE FROM enrollments WHERE id = ?";
    return executeUpdate(query, enrollment.getId());
  }
}

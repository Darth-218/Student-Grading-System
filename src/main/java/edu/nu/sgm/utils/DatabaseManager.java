package edu.nu.sgm.utils;

// TODO: Create the delete and insert methods.
// TODO: Finish writing the queries.
// TODO: Finish writing the database itself.

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
                                                   Object[] parameters) {
    for (int i = 1; i <= parameters.length; i++) {
      statement.setObject(i, parameters[i]);
    }
    return statement;
  }

  private <T> List<T> executeQuery(String query, ResultSetMapper<T> mapper)
      throws Exception {
    List<T> output = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(DB_URL);
         ResultSet results = connection.createStatement().executeQuery(query)) {
      while (results.next()) {
        output.add(mapper.map(results));
      }
    } catch (SQLException e) {
      throw new Exception();
    }
    return output;
  }

  private <T> List<T> executeQuery(String query, ResultSetMapper<T> mapper,
                                   Object... parameters) throws Exception {
    List<T> output = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(DB_URL);
         ResultSet results = parameterizedStatement(
                                 connection.prepareStatement(query), parameters)
                                 .executeQuery()) {
      while (results.next()) {
        output.add(mapper.map(results));
      }
    } catch (SQLException e) {
      throw new Exception();
    }
    return output;
  }

  private int executeUpdate(String query, Object[] parameters) {
    try (Connection connection = DriverManager.getConnection(DB_URL);
         PreparedStatement statement = parameterizedStatement(
             connection.prepareStatement(query), parameters)) {
      return statement.executeUpdate();
    }
  }

  public List<Course> fetchCourses() {
    String query = "SELECT * FROM courses";
    return executeQuery(query,
                        results
                        -> new Course(results.getInt("id"),
                                      results.getString("couresultse_code"),
                                      results.getString("title"),
                                      results.getString("instructor"),
                                      results.getInt("credit_houresults")));
  }

  public List<Student> fetchStudents() {
    String query = "SELECT * FROM students";
    return executeQuery(query,
                        results
                        -> new Student(results.getInt("id"),
                                       results.getString("firesultst_name"),
                                       results.getString("last_name"),
                                       results.getString("email")));
  }

  public List<GradeItem> fetchGrades(Student student, Course course) {
    String query = "";
    return executeQuery(query, results -> new GradeItem());
  }

  public int updateCourse(Course course) {
    String query = "";
    return executeUpdate(query, );
  }

  public int updateStudent(Student student) {
    String query = "";
    return executeUpdate(query, );
  }

  public int updateGrades(Student student, Course course, GradeItem grade) {
    String query = "";
    return executeUpdate(query, );
  }
}

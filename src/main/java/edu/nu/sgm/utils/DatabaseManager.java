package edu.nu.sgm.utils;

import edu.nu.sgm.models.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String DB_URL =
      "jdbc:h2:mem:university;DB_CLOSE_DELAY=-1";

  public DatabaseManager() {
    try {
      initializeDatabase();
    } catch (SQLException e) {
      System.err.println("Failed to initialize database.");
    }
  }

  public boolean initializeDatabase() throws SQLException {
    String schemaPath = "";
    try (Connection connection = DriverManager.getConnection(DB_URL);
         Statement statement = connection.createStatement()) {
      URL resource = getClass().getClassLoader().getResource("schema.sql");
      if (resource == null) {
        throw new IllegalStateException("schema.sql not found in classpath");
      }
      Path path = Paths.get(resource.toURI());
      schemaPath = path.toString();
      statement.execute("RUNSCRIPT FROM '" + schemaPath + "'");
      return true;
    } catch (Exception e) {
      System.err.println("Database initialization failed: " + schemaPath +
                         e.getMessage());
      throw new SQLException(e);
    }
  }

  private interface ResultSetMapper<T> {
    T map(ResultSet results) throws SQLException;
  }

  private PreparedStatement parameterizedStatement(PreparedStatement statement,
                                                   Object[] parameters)
      throws SQLException {
    /**
     * @brief A method to "bind" parameters to an SQL statement
     * @param statement  The statement to execute
     * @param parameters The extra parameters bind to the statement
     * @return A parameterized statement
     */
    for (int i = 0; i < parameters.length; i++) {
      statement.setObject(i + 1, parameters[i]);
    }
    return statement;
  }

  private <T> List<T> executeReturn(String query, ResultSetMapper<T> mapper)
      throws SQLException {
    /**
     * @brief A method that executes statements and returns a list of T
     * @param query  The statement to execute
     * @param mapper The T constructor
     * @return A list of T objects
     */
    List<T> output = new ArrayList<>(); ///< The output list
    try (Connection connection = DriverManager.getConnection(DB_URL);
         ResultSet results = connection.createStatement().executeQuery(query)) {
      while (results.next()) {
        output.add(mapper.map(results));
      }
    }
    return output;
  }

  private <T> List<T> executeReturn(String query, ResultSetMapper<T> mapper,
                                    Object... parameters) throws SQLException {
    /**
     * @brief A method that executes parameterized statements and
     *        returns a list of T
     * @param query      The statement to execute
     * @param mapper     The T constructor
     * @param parameters The statement parameters
     * @return A list of T objects
     */
    List<T> output = new ArrayList<>(); ///< The output list
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
    /**
     * @brief A method that updates database entries
     * @param query      The statement to execute
     * @param parameters The statement parameters
     * @return The number of updated rows
     */
    try (Connection connection = DriverManager.getConnection(DB_URL);
         PreparedStatement statement = parameterizedStatement(
             connection.prepareStatement(query), parameters)) {
      return statement.executeUpdate();
    }
  }

  private int executeInsert(String query, Object... parameters)
      throws SQLException {
    /**
     * @brief A method that creates database entries
     * @param query      The statement to execute
     * @param parameters The statement parameters
     * @return The generated ID of the inserted entry
     */
    try (
        Connection connection = DriverManager.getConnection(DB_URL);
        PreparedStatement statement = parameterizedStatement(
            connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS),
            parameters)) {
      if (statement.executeUpdate() == 0)
        throw new SQLException("No rows updated");
      ResultSet keys = statement.getGeneratedKeys();
      while (keys.next()) {
        return keys.getInt(1);
      }
      return 0;
    }
  }

  public boolean createCourse(Course course) throws SQLException {
    /**
     * @brief A method that inserts a course object into the database
     * @param student The course to add
     * @return True when the course is added successfully
     */
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
    /**
     * @brief A method that inserts a Student object into the database
     * @param student The student to add
     * @return True when the student is added successfully
     */
    String query = "INSERT INTO students (first_name, last_name, email) "
                   + "VALUES (?, ?, ?)";
    int student_id = executeInsert(query, student.getFirstName(),
                                   student.getLastName(), student.getEmail());
    student.setId(student_id);
    student.setEmail(student.generateEmail());
    return true;
  }

  public boolean createEnrollment(Enrollment enrollment) throws SQLException {
    /**
     * @brief A method that inserts an Enrollment object into the database
     * @param enrollment The enrollment to add
     * @return True when the enrollment is added successfully
     */
    String query = "INSERT INTO enrollments (student_id, course_id"
                   + ") VALUES (?, ?)";
    int enrollment_id =
        executeInsert(query, enrollment.getStudent(), enrollment.getCourse());
    enrollment.setID(enrollment_id);
    return true;
  }

  public boolean createGrade(Enrollment enrollment, GradeItem grade)
      throws SQLException {
    /**
     * @brief A method that inserts a GradeItem object into the database
     * @param enrollment The course/student pair
     * @param grade      The grade to add
     * @return True when the grade is added successfully
     */
    String query = "INSERT INTO grade_items (enrollment_id, title, category, "
                   + "score, max_score, feedback) VALUES (?, ?, ?, ?, ?, ?)";
    int grade_id = executeInsert(query, enrollment.getId(), grade.getTitle(),
                                 grade.getCategory(), grade.getScore(),
                                 grade.getMaxScore(), grade.getFeedback());
    grade.setId(grade_id);
    return true;
  }

  public List<Course> fetchCourses(Student student) throws SQLException {
    /**
     * @brief A method that gets the courses a student is enrolled in
     * @param student The target student
     * @return The list of courses the target student is enrolled in
     */
    String enrollment_query = "SELECT * FROM enrollment WHERE student_id = ?";
    List<Integer> course_ids =
        executeReturn(enrollment_query,
                      results -> results.getInt("course_id"), student.getId());
    String query = "SELECT * FROM courses WHERE id = ?";
    return executeReturn(query,
                         results
                         -> new Course(results.getInt("id"),
                                       results.getString("course_code"),
                                       results.getString("title"),
                                       results.getString("instructor"),
                                       results.getInt("credit_hours")),
                         course_ids);
  }

  public List<Student> fetchStudents(Course course) throws SQLException {
    /**
     * @brief A method that gets the students enrolled in a course
     * @param course The target course
     * @return The list of students enrolled in the target course
     */
    String enrollment_query = "SELECT * FROM enrollments WHERE course_id = ?";
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
    /**
     * @brief A method that gets all courses in the database
     * @return The list of courses
     */
    String query = "SELECT * FROM courses";
    return executeReturn(query,
                         results
                         -> new Course(results.getInt("id"),
                                       results.getString("course_code"),
                                       results.getString("title"),
                                       results.getString("instructor"),
                                       results.getInt("credit_hours")));
  }

  public Course fetchCourse(int course_id) throws SQLException {
    /**
     * @brief A method that gets all courses in the database
     * @return The list of courses
     */
    String query = "SELECT * FROM courses WHERE id = ?";
    return executeReturn(query,
                         results
                         -> new Course(results.getInt("id"),
                                       results.getString("course_code"),
                                       results.getString("title"),
                                       results.getString("instructor"),
                                       results.getInt("credit_hours")),
                         course_id)
        .getFirst();
  }

  public List<Student> fetchStudents() throws SQLException {
    /**
     * @brief A method that gets all students in the database
     * @return The list of students
     */
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
    /**
     * @brief A method that gets all grades of a student/course pair
     * @param enrollment The student/course pair
     * @return The list of grades
     */
    String query = "SELECT * FROM grade_items WHERE enrollment_id = ?";
    return executeReturn(
        query,
        results
        -> new GradeItem(
            results.getInt("id"), results.getString("title"),
            results.getString("category"), results.getDouble("score"),
            results.getDouble("max_score"), results.getString("feedback"),
            results.getDouble("weight")),
        enrollment.getId());
  }

  public List<Enrollment> fetchEnrollment(Student student, Course course)
      throws SQLException {
    /**
     * @brief A method that gets a specific enrollment
     * @param student The target student
     * @param course  The target course
     * @return The Enrollment object
     */
    String query =
        "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
    return executeReturn(query,
                         results
                         -> new Enrollment(results.getInt("id"),
                                           results.getInt("student_id"),
                                           results.getInt("course_id")),
                         student.getId(), course.getId());
  }

  public List<Enrollment> fetchEnrollment(Course course) throws SQLException {
    /**
     * @brief A method that gets a specific enrollment
     * @param student The target student
     * @param course  The target course
     * @return The Enrollment object
     */
    String query = "SELECT * FROM enrollments WHERE course_id = ?";
    return executeReturn(query,
                         results
                         -> new Enrollment(results.getInt("id"),
                                           results.getInt("student_id"),
                                           results.getInt("course_id")),
                         course.getId());
  }

  public List<Enrollment> fetchEnrollment(Student student) throws SQLException {
    /**
     * @brief A method that gets a specific enrollment
     * @param student The target student
     * @param course  The target course
     * @return The Enrollment object
     */
    String query = "SELECT * FROM enrollments WHERE student_id = ?";
    return executeReturn(query,
                         results
                         -> new Enrollment(results.getInt("id"),
                                           results.getInt("student_id"),
                                           results.getInt("course_id")),
                         student.getId());
  }

  public int updateCourse(Course course) throws SQLException {
    /**
     * @brief A method that updates a Course object in the database
     * @param course The target course with updated attributes
     * @return The number of rows modified
     */
    String query =
        "UPDATE courses SET course_code = ?, title = ?, instructor = ?, "
        + "credit_hours = ?, total_students = ? WHERE id = ?";
    return executeUpdate(query, course.getCourseCode(), course.getTitle(),
                         course.getInstructor(), course.getCreditHours(),
                         course.getTotalStudents(), course.getId());
  }

  public int updateStudent(Student student) throws SQLException {
    /**
     * @brief A method that updates a Student object in the database
     * @param student The target student with updated attributes
     * @return The number of rows modified
     */
    String query = "UPDATE students SET first_name = ?, last_name = ?, email "
                   + "= ? WHERE id = ?";
    return executeUpdate(query, student.getFirstName(), student.getLastName(),
                         student.getEmail(), student.getId());
  }

  public int updateGrades(GradeItem grade) throws SQLException {
    /**
     * @brief A method that updates a GradeItem object in the database
     * @param grade The target grade with updated attributes
     * @return The number of rows modified
     */
    String query = "UPDATE grade_items SET title = ?, category = ?, score = ?, "
                   + "max_score = ?, feedback = ? WHERE id = ?";
    return executeUpdate(query, grade.getTitle(), grade.getCategory(),
                         grade.getScore(), grade.getMaxScore(),
                         grade.getFeedback(), grade.getId());
  }

  public int deleteCourse(Course course) throws SQLException {
    /**
     * @brief A method that deletes a Course object from the database
     * @param course The target course
     * @return The number of rows modified
     */
    String query = "DELETE FROM courses WHERE id = ?";
    return executeUpdate(query, course.getId());
  }

  public int deleteStudent(Student student) throws SQLException {
    /**
     * @brief A method that deletes a Student object from the database
     * @param student The target student
     * @return The number of rows modified
     */
    String query = "DELETE FROM students WHERE id = ?";
    return executeUpdate(query, student.getId());
  }

  public int deleteGrade(GradeItem grade) throws SQLException {
    /**
     * @brief A method that deletes a GradeItem object from the database
     * @param grade The target grade
     * @return The number of rows modified
     */
    String query = "DELETE FROM grade_items WHERE id = ?";
    return executeUpdate(query, grade.getId());
  }

  public int deleteEnrollment(Student student, Course course)
      throws SQLException {
    /**
     * @brief A method that deletes an Enrollment object from the database
     * @param enrollment The target enrollment
     * @return The number of rows modified
     */
    String query =
        "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
    return executeUpdate(query, student.getId(), course.getId());
  }

  public int deleteEnrollment(Course course) throws SQLException {
    /**
     * @brief A method that deletes an Enrollment object from the database
     * @param enrollment The target enrollment
     * @return The number of rows modified
     */
    String query = "DELETE FROM enrollments WHERE course_id = ?";
    return executeUpdate(query, course.getId());
  }

  public int deleteEnrollment(Student student) throws SQLException {
    /**
     * @brief A method that deletes an Enrollment object from the database
     * @param enrollment The target enrollment
     * @return The number of rows modified
     */
    String query = "DELETE FROM enrollments WHERE student_id = ?";
    return executeUpdate(query, student.getId());
  }
}

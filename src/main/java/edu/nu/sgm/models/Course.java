package edu.nu.sgm.models;

/**
 * @brief Represents a course in the student grading system.
 * @details This class contains details about a course such as its
 *          ID,name,credit hours, and the total number of students enrolled.
 */
public class Course {

  private int id;
  ///< The unique identifier for the course.

  private String course_code;
  ///< The name of the course.

  private String title;
  ///< The name of the course.

  private String instructor;
  ///< The name of the instructor teaching the course.

  private int credit_hours;
  ///< The number of credit hours assigned to the course.

  private int total_students; ///< The total number of students enrolled in the course.

  /**
   * @brief Constructs a Course object with the specified details.
   * @param id           The unique identifier for the course.
   * @param name         The name of the course.
   * @param instructor   The name of the instructor teaching the course.
   * @param credit_hours The number of credit hours assigned to the course.
   */

  public Course(int id, String course_code, String title, String instructor,
      int credit_hours) {
    this.id = id;
    this.title = title;
    this.instructor = instructor;
    this.credit_hours = credit_hours;
    this.total_students = 0;
  }

  /**
   * @brief Sets the code of the course.
   * @param course_code The code of the course.
   * @return {@code true} if the course code was successfully set, {@code false}
   *         otherwise.
   */
  public boolean setCourseCode(String course_code) {
    if (course_code == null || course_code.isEmpty()) {
      return false;
    }
    this.course_code = course_code;
    return true;
  }

  /**
   * @brief Retrieves the code of the course.
   * @return The code of the course.
   */
  public String getCourseCode() {
    return course_code;
  }

  /**
   * @brief Sets the unique identifier for the course.
   * @param id The unique identifier for the course.
   * @return {@code true} if the ID was successfully set, {@code false}
   *         otherwise.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @brief Retrieves the unique identifier for the course.
   * @return The unique identifier for the course.
   */
  public int getId() {
    return id;
  }

  /**
   * @brief Sets the name of the course.
   * @param name The name of the course.
   * @return {@code true} if the name was successfully set, {@code false}
   *         otherwise.
   */
  public boolean setTitle(String title) {
    if (title == null || title.isEmpty()) {
      return false;
    }
    this.title = title;
    return true;
  }

  /**
   * @brief Retrieves the name of the course.
   * @return The name of the course.
   */
  public String getTitle() {
    return title;
  }

  /**
   * @brief Sets the name of the instructor teaching the course.
   * @param instructor The name of the instructor.
   * @return {@code true} if the instructor name was successfully set,
   *         {@code false} otherwise.
   */
  public boolean setInstructor(String instructor) {
    if (instructor == null || instructor.isEmpty()) {
      return false;
    }
    this.instructor = instructor;
    return true;
  }

  /**
   * @brief Retrieves the name of the instructor teaching the course.
   * @return The name of the instructor.
   */
  public String getInstructor() {
    return instructor;
  }

  /**
   * @brief Sets the number of credit hours assigned to the course.
   * @param credit_hours The number of credit hours.
   * @return {@code true} if the credit hours were successfully set, {@code
   *     false}
   *         otherwise.
   */
  public boolean setCreditHours(int credit_hours) {
    if (credit_hours <= 0 || credit_hours > 5) {
      return false;
    }
    this.credit_hours = credit_hours;
    return true;
  }

  /**
   * @brief Retrieves the number of credit hours assigned to the course.
   * @return The number of credit hours.
   */
  public int getCreditHours() {
    return credit_hours;
  }

  /**
   * @brief Sets the total number of students enrolled in the course.
   * @param total_students The total number of students.
   * @return {@code true} if the total number of students was successfully set,
   *         {@code false} otherwise.
   */
  public void setTotalStudents(int total_students) {
    this.total_students = total_students;
  }

  /**
   * @brief Retrieves the total number of students enrolled in the course.
   * @return The total number of students enrolled in the course.
   */
  public int getTotalStudents() {
    return total_students;
  }

}

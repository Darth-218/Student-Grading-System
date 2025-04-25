package edu.nu.sgm.models;

/**
 * @brief Represents a course in the student grading system.
 * @details This class contains details about a course such as its
 *          ID,name,credit hours, and the total number of students enrolled.
 */
public class Course {

  private String id;
  ///< The unique identifier for the course.

  private String name;
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

  public Course(String id, String name, String instructor, int credit_hours) {
    this.id = id;
    this.name = name;
    this.instructor = instructor;
    this.credit_hours = credit_hours;
    this.total_students = 0;
  }

  /**
   * @brief Sets the unique identifier for the course.
   * @param id The unique identifier for the course.
   * @return {@code true} if the ID was successfully set, {@code false} otherwise.
   */
  public boolean setId(String id) {
    if (id == null || id.isEmpty()) {
      return false;
    }
    this.id = id;
    return true;
  }

  /**
   * @brief Retrieves the unique identifier for the course.
   * @return The unique identifier for the course.
   */
  public String getId() {
    return id;
  }

  /**
   * @brief Sets the name of the course.
   * @param name The name of the course.
   * @return {@code true} if the name was successfully set, {@code false}
   *         otherwise.
   */
  public boolean setName(String name) {
    if (name == null || name.isEmpty()) {
      return false;
    }
    this.name = name;
    return true;
  }

  /**
   * @brief Retrieves the name of the course.
   * @return The name of the course.
   */
  public String getName() {
    return name;
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
   * @return {@code true} if the credit hours were successfully set, {@code false}
   *         otherwise.
   */
  public boolean setCreditHours(int credit_hours) {
    if (credit_hours <= 0) {
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
   * @brief Retrieves the total number of students enrolled in the course.
   * @return The total number of students enrolled in the course.
   */
  public int getTotalStudents() {
    return total_students;
  }
}

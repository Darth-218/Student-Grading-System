package edu.nu.sgm.models;

public class Enrollment {
  private int id;
  ///< Enrollment ID.
  private int student_id;
  ///< The student enrolled.
  private int course_id; ///< The course enrolled in.

  /**
   * @brief The enrollment object constructor.
   * @param student_id The student to enroll.
   * @param course_id  The course to enroll in.
   */
  public Enrollment(int id, int student_id, int course_id) {
    this.id = id;
    this.student_id = student_id;
    this.course_id = course_id;
  }

  /**
   * @brief Enrollment ID getter.
   * @return Enrollment ID.
   */
  public int getId() { return this.id; }

  /**
   * @brief Enrollment student getter.
   * @return Student enrolled.
   */
  public int getStudent() { return this.student_id; }

  /**
   * @brief Enrollment course getter.
   * @return Course enrolled in.
   */
  public int getCourse() { return this.course_id; }

  /**
   * @brief Enrollment ID setter.
   * @param Enrollment ID.
   */
  public void setID(int id) { this.id = id; }
}

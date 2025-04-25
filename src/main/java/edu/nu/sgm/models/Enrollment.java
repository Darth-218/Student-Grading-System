package edu.nu.sgm.models;

public class Enrollment {
  private int id;
  ///< Enrollment ID.
  private boolean status;
  ///< Enrollment status (course progress).
  private Student student;
  ///< The student enrolled.
  private Course course; ///< The course enrolled in.

  /**
   * @brief The enrollment object constructor.
   * @param student The student to enroll.
   * @param course  The course to enroll in.
   * @param status  The course status (i.e. in progress, done).
   */
  public Enrollment(Student student, Course course, boolean status) {
    this.student = student;
    this.course = course;
    this.status = status;
  }

  /**
   * @brief Enrollment ID getter.
   * @return Enrollment ID.
   */
  public int getId() { return this.id; }

  /**
   * @brief Enrollment status getter.
   * @return Course status.
   */
  public boolean getStatus() { return this.status; }

  /**
   * @brief Enrollment student getter.
   * @return Student enrolled.
   */
  public Student getStudent() { return this.student; }

  /**
   * @brief Enrollment course getter.
   * @return Course enrolled in.
   */
  public Course getCourse() { return this.Course; }
}

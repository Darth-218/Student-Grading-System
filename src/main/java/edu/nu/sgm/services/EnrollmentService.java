package edu.nu.sgm.services;

public class EnrollmentService {
  /**
   * @brief Enrolls a student in a course.
   * @param student The target student.
   * @param course  The target course.
   * @return True when the student is successfully enrolled.
   */
  public boolean enrollStudent(Student Student, Course course) { return true; }

  /**
   * @brief Drops a course from the student's record.
   * @param student The target student.
   * @param course  The target course.
   * @return True when the course is successfully dropped.
   */
  public boolean dropCourse(Student student, Course course) { return true; }

  /**
   * @brief Generates a student's report card.
   * @param student The target student.
   * @return The generated report card.
   */
  public void generateReportCard(Student student) { return; }
}

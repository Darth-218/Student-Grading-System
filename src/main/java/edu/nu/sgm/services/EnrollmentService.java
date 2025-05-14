package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;
import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {
  private DatabaseManager db;

  /**
   * @brief Enrolls a student in a course.
   * @param student The target student.
   * @param course  The target course.
   * @return True when the student is successfully enrolled.
   */
  public boolean enrollStudent(Student student, Course course) {
    Enrollment enrollment = new Enrollment(0, student.getId(), course.getId());
    try {
      if (!db.fetchEnrollment(student, course).isEmpty())
        return false;
      db.createEnrollment(enrollment);
      course.setTotalStudents(course.getTotalStudents() + 1);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * @brief Drops a course from the student's record.
   * @param student The target student.
   * @param course  The target course.
   * @return True when the course is successfully dropped.
   */
  public boolean dropCourse(Student student, Course course) {
    try {
      if (db.fetchEnrollment(student, course).isEmpty())
        return false;
      db.deleteEnrollment(student, course);
      course.setTotalStudents(course.getTotalStudents() - 1);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * @brief Generates a student's report card.
   * @param student The target student.
   * @return The generated report card.
   */
  public void generateReportCard(Student student) { return; }
}

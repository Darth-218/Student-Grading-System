/**
 * @file EnrollmentService.java
 * @brief Service class for managing student enrollments in courses.
 */

package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;
import java.sql.SQLException;

/**
 * @class EnrollmentService
 * @brief Provides methods to enroll students, drop courses, and generate report cards.
 */
public class EnrollmentService {
  private DatabaseManager db = new DatabaseManager();

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
  public String generateReportCard(Student student) {
    String report = "";
    GradeItemService gradeservice = new GradeItemService();
    try {
      for (Enrollment enrollment : db.fetchEnrollment(student)) {
        Double grades = gradeservice.calculateTotalGrade(enrollment);
        Course course = db.fetchCourse(enrollment.getCourse());
        report += String.format("%s: %s\t Final Grade: %f\t GPA: %f\n",
                                course.getTitle(), course.getCourseCode(),
                                grades, 0.0);
      }
    } catch (SQLException e) {
      return "";
    }
    return report;
  }
}

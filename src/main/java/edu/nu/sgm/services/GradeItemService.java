package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.*;
import edu.nu.sgm.services.*;
import java.sql.SQLException;
import java.util.List;

public class GradeItemService {

  private DatabaseManager db;

  public boolean addGradeItem(Enrollment enrollment, GradeItem item) {
    try {
      db.createGrade(enrollment, item);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
public boolean removeGradeItem(GradeItem grade, Enrollment enrollment) {
    if (grade == null || enrollment == null) {
        System.out.println("Either grade item or enrollment is null.");
        return false;
    }

    // Step 1: Validate enrollment
    Student student = studentService.getStudentById(enrollment.getStudentId());
    Course course = courseService.getCourseById(enrollment.getCourseId());

    if (student == null || course == null) {
        System.out.println("Student or Course not found.");
        return false;
    }

    if (db.fetchEnrollment(student, course).isEmpty()) {
        System.out.println("Student is not enrolled in the specified course.");
        return false;
    }

    // Step 2: Show all grades for this enrollment
    List<GradeItem> gradeItems = getGradeItemsByEnrollmentId(enrollment.getId());
    if (gradeItems.isEmpty()) {
        System.out.println("No grades found for this enrollment.");
        return false;
    }

    System.out.println("Available grades:");
    for (int i = 0; i < gradeItems.size(); i++) {
        GradeItem gi = gradeItems.get(i);
        System.out.println((i + 1) + ". " + gi.getName() + " (" + gi.getScore() + "/" + gi.getMaxScore() + ")");
    }

    // Step 3: Check if the selected grade exists and delete it
    boolean found = gradeItems.stream()
        .anyMatch(g -> g.getId() == grade.getId());

    if (!found) {
        System.out.println("Grade item not found in this enrollment.");
        return false;
    }

    String sql = "DELETE FROM grade_items WHERE id = ?";
    boolean success = DatabaseManager.executeUpdate(sql, grade.getId()) > 0;

    if (success) {
        System.out.println("Grade item successfully removed.");
    } else {
        System.out.println("Failed to remove grade item.");
    }

    return success;
}


  public double calculateTotalGrade(Enrollment enrollment){


  }

}

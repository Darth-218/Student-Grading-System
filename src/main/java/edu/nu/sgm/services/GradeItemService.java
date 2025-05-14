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

  public List<GradeItem> getGrades(Enrollment enrollment) {
    if (enrollment == null) {
        System.out.println("Enrollment is null.");
        return List.of();
    }

    try {
        return db.fetchGrades(enrollment);
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error fetching grades for enrollment.");
        return List.of();
    }
}

  
public boolean removeGradeItem(GradeItem grade, Enrollment enrollment) {
    if (grade == null || enrollment == null) {
        System.out.println("Either grade item or enrollment is null.");
        return false;
    }

    List<GradeItem> gradeItems = getGrades(enrollment);
    if (gradeItems.isEmpty()) {
        System.out.println("No grades found for this enrollment.");
        return false;
    }

    try {
      return db.deleteGrade(grade) > 0;
    } catch (SQLException e) {
    e.printStackTrace();
    return false; 
    }
}


}

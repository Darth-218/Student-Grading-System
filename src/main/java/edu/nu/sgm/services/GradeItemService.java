package edu.nu.sgm.services;

import edu.nu.sgm.models.Enrollment;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.utils.DatabaseManager;
import java.sql.SQLException;

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
}

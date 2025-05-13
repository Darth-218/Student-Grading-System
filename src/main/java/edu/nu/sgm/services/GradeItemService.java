package edu.nu.sgm.services;

import java.sql.SQLException;

import edu.nu.sgm.models.Enrollment;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.utils.DatabaseManager;

public class GradeItemService {

    private DatabaseManager db;

    public boolean addGradeItem(Enrollment enrollment, GradeItem item) {

    if (!item.isValid()) {
        System.out.println("Invalid grade item data.");
        return false;
    }

    try {
        db.createGrade(enrollment, item);
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}




}

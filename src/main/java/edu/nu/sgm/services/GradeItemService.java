package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

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

public double calculateTotalGrade(Enrollment enrollment) {
    List<GradeItem> grades = getGrades(enrollment);
    if (grades.isEmpty()) return 0.0;
    double totalScore = 0.0;
    for (GradeItem grade : grades) {
        totalScore += grade.getScore() * grade.getWeight() / grade.getMaxScore();
    }
    return totalScore;
}

public boolean importGrades(File file) {
    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");

            int enrollmentId = Integer.parseInt(parts[0]);
            String title = parts[1];
            double score = Double.parseDouble(parts[2]);
            double maxScore = Double.parseDouble(parts[3]);
            double weight = Double.parseDouble(parts[4]);
            GradeItem item = new GradeItem(0, title, "imported", score, maxScore, "", weight);
            Enrollment enrollment = new Enrollment(enrollmentId, 0, 0);
            addGradeItem(enrollment, item);
        }
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean exportGrades(List<Enrollment> enrollments) {
    try (PrintWriter writer = new PrintWriter("grades_export.csv")) {
        for (Enrollment enrollment : enrollments) {
            List<GradeItem> grades = getGrades(enrollment);

            for (GradeItem grade : grades) {
                writer.println(enrollment.getId() + "," + grade.getTitle() + "," + grade.getScore() + "," +
                               grade.getMaxScore() + "," + grade.getWeight());
            }
        }
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


}

package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Service class for managing grade items related to student enrollments.
 *
 *        This class provides methods to create, retrieve, delete, and process
 *        grade items
 *        including calculating total grades and importing/exporting grade data.
 */
public class GradeItemService {

  private DatabaseManager db = new DatabaseManager();

  /** < Manages database operations */

  /**
   * @brief Adds a new grade item to an enrollment.
   * @param enrollment The enrollment associated with the grade
   * @param item       The grade item to add
   * @return True if the grade item is successfully added; false otherwise
   */
  public boolean addGradeItem(Enrollment enrollment, GradeItem item) {
    try {
      db.createGrade(enrollment, item);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * @brief Retrieves all grade items for a given enrollment.
   * @param enrollment The enrollment whose grades are to be retrieved
   * @return A list of GradeItem objects, or an empty list on error or null
   *         input
   */
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

  /**
   * @brief Removes a specific grade item from an enrollment.
   * @param grade      The grade item to remove
   * @param enrollment The associated enrollment
   * @return True if the item is successfully removed; false otherwise
   */
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

  /**
   * @brief Calculates the total grade for an enrollment based on weighted
   *        scores.
   * @param enrollment The enrollment whose total grade is to be calculated
   * @return The total grade as a percentage
   */
  public double calculateTotalGrade(Enrollment enrollment) {
    List<GradeItem> grades = getGrades(enrollment);
    if (grades.isEmpty())
      return 0.0;
    double totalScore = 0.0;
    for (GradeItem grade : grades) {
      totalScore += grade.getScore() * grade.getWeight() / grade.getMaxScore();
    }
    return totalScore;
  }

  /**
   * @brief Imports grade items from a CSV file.
   * @param file The CSV file to import from
   * @return A list of GradeItem objects, or an empty list if import fails
   */
  public List<GradeItem> impoGradeItems(File file) {
    List<GradeItem> GradeItem =
        Reader.readCSV(file, Reader::parseGradeItemsImport);
    if (GradeItem == null) {
      return new ArrayList<>();
    }
    return GradeItem;
  }

  /**
   * @brief Exports grade items of multiple enrollments to a CSV file.
   * @param enrollments A list of enrollments whose grades will be exported
   * @return True if export is successful
   */
  public boolean exportGradeItems(List<Enrollment> enrollments) {
    List<GradeItem> allGradeItems = new ArrayList<>();
    for (int i = 0; i < enrollments.size(); i++) {
      Enrollment enrollment = enrollments.get(i);
      List<GradeItem> items = getGrades(enrollment);
      if (items != null) {
        allGradeItems.addAll(items);
      }
    }
    Reader.writeCSV(new File("grade_item_export.csv"), allGradeItems,
                    Reader::parseGradeItemsExport);
    return true;
  }
}

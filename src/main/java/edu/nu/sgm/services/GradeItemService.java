package edu.nu.sgm.services;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
        if (grades.isEmpty())
            return 0.0;
        double totalScore = 0.0;
        for (GradeItem grade : grades) {
            totalScore += grade.getScore() * grade.getWeight() / grade.getMaxScore();
        }
        return totalScore;
    }

    public List<GradeItem> impoGradeItems(File file) {
        List<GradeItem> GradeItem = Reader.readCSV(file, Reader::parseGradeItemsImport);
        if (GradeItem == null) {
            return new ArrayList<>();
        }
        return GradeItem;
    }

    public boolean exportGradeItems(List<Enrollment> enrollments) {
        List<GradeItem> allGradeItems = new ArrayList<>();
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            List<GradeItem> items = getGrades(enrollment);
            if (items != null) {
                allGradeItems.addAll(items);
            }
        }
        Reader.writeCSV(new File("grade_item_export.csv"), allGradeItems, Reader::parseGradeItemsExport);
        return true;
    }

}

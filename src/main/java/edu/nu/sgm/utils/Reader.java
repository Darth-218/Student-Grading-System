/**
 * @file Reader.java
 * @brief Utility class for reading and writing CSV files for courses, students, and grade items.
 */

package edu.nu.sgm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.GradeItem;
import edu.nu.sgm.models.Student;

/**
 * @class Reader
 * @brief Provides static methods for reading and writing CSV files and parsing/exporting model objects.
 */
public class Reader {

    /**
     * @brief Reads a CSV file and parses each line into an object using the provided parser.
     * @tparam T The type of object to parse.
     * @param file The CSV file to read.
     * @param parser A function that parses a line into an object of type T.
     * @return A list of parsed objects.
     */
    public static <T> List<T> readCSV(File file, Function<String, T> parser) {
        List<T> list = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                T obj = parser.apply(line);
                list.add(obj);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    /**
     * @brief Writes a list of objects to a CSV file using the provided parser.
     * @tparam T The type of object to write.
     * @param file The CSV file to write to.
     * @param list The list of objects to write.
     * @param parser A function that converts an object of type T to a CSV line.
     */
    public static <T> void writeCSV(File file, List<T> list, Function<T, String> parser) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            lines.add(parser.apply(list.get(i)));
        }
        try {
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * @brief Parses a CSV line into a Course object.
     * @param line The CSV line.
     * @return The parsed Course object.
     */
    public static Course parseCourseImport(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String courseCode = parts[0];
        String title = parts[1];
        String instructor = parts[2];
        int creditHours = Integer.parseInt(parts[3]);
        return new Course(id, courseCode, title, instructor, creditHours);
    }

    /**
     * @brief Parses a CSV line into a Student object.
     * @param line The CSV line.
     * @return The parsed Student object.
     */
    public static Student parseStudentImport(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String firstName = parts[0];
        String lastName = parts[1];
        String email = parts[2];
        return new Student(id, firstName, lastName, email);
    }

    /**
     * @brief Parses a CSV line into a GradeItem object.
     * @param line The CSV line.
     * @return The parsed GradeItem object.
     */
    public static GradeItem parseGradeItemsImport(String line) {
        String[] parts = line.split(",");
        String title = parts[0];
        String category = parts[1];
        double score = Double.parseDouble(parts[2]);
        double max_score = Double.parseDouble(parts[3]);
        String feedback = parts[4];
        double weight = Double.parseDouble(parts[5]);
        int id = 0;
        return new GradeItem(id, title, category, score, max_score, feedback, weight);
    }

    /**
     * @brief Converts a Course object to a CSV line for export.
     * @param course The Course object.
     * @return The CSV line.
     */
    public static String parseCourseExport(Course course) {
        return String.format("%s,%s,%s,%d", course.getCourseCode(), course.getTitle(),
                course.getInstructor(), course.getCreditHours());
    }

    /**
     * @brief Converts a Student object to a CSV line for export.
     * @param student The Student object.
     * @return The CSV line.
     */
    public static String parseStudentExport(Student student) {
        return String.format("%s,%s,%s", student.getFirstName(), student.getLastName(),
                student.getEmail());
    }

    /**
     * @brief Converts a GradeItem object to a CSV line for export.
     * @param grade The GradeItem object.
     * @return The CSV line.
     */
    public static String parseGradeItemsExport(GradeItem grade) {
        return String.format("%s,%s,%f,%f,%s,%f", grade.getTitle(), grade.getCategory(),
                grade.getScore(), grade.getMaxScore(), grade.getFeedback(), grade.getWeight());
    }

}

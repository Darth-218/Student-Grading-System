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

public class Reader {

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

    public static Course parseCourseImport(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String courseCode = parts[0];
        String title = parts[1];
        String instructor = parts[2];
        int creditHours = Integer.parseInt(parts[3]);
        return new Course(id, courseCode, title, instructor, creditHours);
    }

    public static Student parseStudentImport(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String name = parts[0];
        String last_name = parts[1];
        String email = parts[2];
        return new Student(id, name, last_name, email);
    }

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

    public static String parseCourseExport(Course course) {
        return String.format("%s,%s,%s,%d", course.getCourseCode(), course.getTitle(),
                course.getInstructor(), course.getCreditHours());
    }

    public static String parseStudentExport(Student student) {
        return String.format("%s,%s,%s", student.getFirstName(), student.getLastName(),
                student.getEmail());
    }

    public static String parseGradeItemsExport(GradeItem grade) {
        return String.format("%s,%s,%f,%f,%s,%f", grade.getTitle(), grade.getCategory(),
                grade.getScore(), grade.getMaxScore(), grade.getFeedback(), grade.getWeight());
    }

}

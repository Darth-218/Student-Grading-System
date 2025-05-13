package edu.nu.sgm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.nu.sgm.models.Course;
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

    public static Course parseCourse(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String courseCode = parts[0];
        String title = parts[1];
        String instructor = parts[2];
        int creditHours = Integer.parseInt(parts[3]);
        return new Course(id, courseCode, title, instructor, creditHours);
    }

    public static Student parseStudent(String line) {
        String[] parts = line.split(",");
        int id = 0;
        String name = parts[0];
        String last_name = parts[1];
        String email = parts[2];
        return new Student(id, name, last_name, email);
    }

}

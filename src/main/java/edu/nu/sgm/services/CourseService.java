package edu.nu.sgm.services;

import java.util.List;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;

public class CourseService {

    public static boolean addCourse(String courseCode, String title, String instructor, int creditHours) {
        if (!courseCode.matches("^[A-Za-z]{4}-[0-9]{3}$")) {
            throw new IllegalArgumentException("Invalid course code format.");

        }
        return true; // Assume the course was added successfully
    }

    public static boolean removeCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        // Logic to remove a course from the database or in-memory list
        // This is a placeholder implementation
        return true; // Assume the course was removed successfully
    }

    public static List<Student> getStudents(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        // Logic to retrieve the list of students enrolled in a course
        // This is a placeholder implementation
        return null; // Assume no students were found
    }

    public static String displayDetails() {
        // Logic to display the details of a course
        // This is a placeholder implementation
        return "Course Details"; // Assume this is the course details string
    }

    public static double calculateTotalGrade(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Invalid student object.");
        }
        // Logic to calculate the total grade for a student in a course
        // This is a placeholder implementation
        return 0.0; // Assume no grade was calculated
    }

}
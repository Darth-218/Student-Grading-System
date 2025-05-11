package edu.nu.sgm.services;

import java.sql.SQLException;
import java.util.List;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.utils.*;

public class CourseService {

    private static DatabaseManager db;

    public CourseService() {
        db = new DatabaseManager();
    }

    /*
     * @brief Adds a course to the database.
     * 
     * @param course The course to be added.
     * 
     * @return true if the course was added successfully, false otherwise.
     */
    public static boolean addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        try {
            return db.createCourse(course);
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            return false;
        }
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
package edu.nu.sgm.services;

import java.sql.SQLException;
import java.util.List;
import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.utils.*;
import edu.nu.sgm.utils.DatabaseManager;
import java.io.File;

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

    /*
     * @brief removes a course from the database.
     * 
     * @param course The course to be removed.
     * 
     * @return true if the course was removed successfully, false otherwise.
     */
    public static boolean removeCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        try {
            return db.deleteCourse(course) > 0;
        } catch (SQLException e) {
            System.err.println("Error removing course: " + e.getMessage());
            return false;
        }
    }

    /*
     * @brief Retrieves a list of students enrolled in a course.
     * 
     * @param course The course for which to retrieve the students.
     * 
     * @return A list of students enrolled in the course, or null if an error
     * occurred.
     */
    public static List<Student> getStudents(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        try {
            return db.fetchStudents(course);
        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
            return null;
        }
    }

    /*
     * @brief retrieves total number of students enrolled in a course.
     * 
     * @return The total number of students enrolled in the course, or 0 if an error
     * occurred.
     */
    public static int getTotalStudents() {
        try {
            return db.fetchStudents().size();
        } catch (SQLException e) {
            System.err.println("Error retrieving total students: " + e.getMessage());
            return 0;
        }
    }

    /*
     * @brief displays the details of a course.
     * 
     * @param course The course for which to display the details.
     * 
     * @return A string containing the course details.
     */
    public static String displayDetails(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Invalid course object.");
        }
        return String.format("Course Name: %s\nCourse Code: %s\nInstructor: %s\nCredit Hours: %d\n",
                course.getTitle(), course.getCourseCode(), course.getInstructor(), course.getCreditHours());
    }

    /*
     * @brief imports courses from a file.
     * 
     * @param file The file containing the course data.
     * 
     * @return true if the courses were imported successfully, false otherwise.
     */
    public static boolean importCourses(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Invalid file object.");
        }
        // under construction
        return false;
    }

    /*
     * @brief exports courses to a file.
     * 
     * @param file The file to which the course data will be exported.
     * 
     * @return true if the courses were exported successfully, false otherwise.
     */
    public static boolean exportCourses(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Invalid file object.");
        }
        // also under construction
        return false;
    }

}

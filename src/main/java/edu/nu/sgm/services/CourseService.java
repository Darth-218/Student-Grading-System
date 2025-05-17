/**
 * @file CourseService.java
 * @brief Service class for managing courses in the grading system.
 */

package edu.nu.sgm.services;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.models.Student;
import edu.nu.sgm.utils.DatabaseManager;
import java.io.File;
import edu.nu.sgm.utils.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @class CourseService
 * @brief Provides methods to add, remove, retrieve, and manage courses.
 */
public class CourseService {

  private DatabaseManager db = new DatabaseManager();

  /**
   * @brief Checks if a course exists in the database.
   * @param course The course to check for existence.
   * @return true if the course exists, false otherwise.
   */
  public boolean courseExists(Course course) {
    try {
      List<Course> courses = db.fetchCourses();
      for (int i = 0; i < courses.size(); i++) {
        if (courses.get(i).getCourseCode().equals(course.getCourseCode())) {
          return true;
        }
      }
      return false;
    } catch (SQLException e) {
      System.err.println("Error checking course existence: " + e.getMessage());
      return false;
    }
  }

  /**
   * @brief Adds a course to the database.
   * @param course The course to be added.
   * @return true if the course was added successfully, false otherwise.
   */
  public boolean addCourse(Course course) {
    if (courseExists(course)) {
      System.err.println("Course already exists.");
      return false;
    }
    if (course == null) {
      throw new IllegalArgumentException("Invalid course object.");
    }

    if (!course.getCourseCode().matches("^[A-Za-z]{4}-\\d{3}$")) {
      System.err.println("Invalid course code format.");
      return false;
    }

    if (course.getTitle().length() > 50) {
      System.err.println("Invalid course title.");
      return false;
    }

    if (course.getCreditHours() < 0) {
      System.err.println("Invalid credit hours.");
      return false;
    }

    if (course.getInstructor().length() > 20) {
      System.err.println("Invalid instructor name length.");
      return false;
    }

    try {
      return db.createCourse(course);
    } catch (SQLException e) {
      System.err.println("Error adding course: " + e.getMessage());
      return false;
    }
  }

  public boolean updateCourse(Course course) {
        try {
            return db.updateCourse(course) > 0;
        } catch (SQLException e) {
            return false;
        }
  }

  /**
   * @brief Removes a course from the database.
   * @param course The course to be removed.
   * @return true if the course was removed successfully, false otherwise.
   */
  public boolean removeCourse(Course course) {
    if (!courseExists(course)) {
      System.err.println("Course does not exist.");
      return false;
    }
    try {
      return db.deleteCourse(course) > 0;
    } catch (SQLException e) {
      System.err.println("Error removing course: " + e.getMessage());
      return false;
    }
  }

  /**
   * @brief Retrieves a list of students enrolled in a course.
   * @param course The course for which to retrieve the students.
   * @return A list of students enrolled in the course, or null if an error occurred.
   */
  public List<Student> getStudents(Course course) {
    if (!courseExists(course)) {
      System.err.println("Course does not exist.");
      return null;
    }
    try {
      return db.fetchStudents(course);
    } catch (SQLException e) {
      System.err.println("Error retrieving students: " + e.getMessage());
      return null;
    }
  }

  /**
   * @brief Retrieves total number of students enrolled in a course.
   * @param course The course for which to count students.
   * @return The total number of students enrolled in the course, or 0 if an error occurred.
   */
  public int getTotalStudents(Course course) {
    try {
      return db.fetchStudents(course).size();
    } catch (SQLException e) {
      System.err.println("Error retrieving total students: " + e.getMessage());
      return 0;
    }
  }

  /**
   * @brief Retrieves all courses from the database.
   * @return A list of all courses.
   */
  public List<Course> getAllCourses() {
    try {
      List<Course> courses = db.fetchCourses();
      if (courses != null) {
        return courses;
      }
    } catch (SQLException e) {
      System.err.println("Error retrieving courses: " + e.getMessage());
    }

    return new ArrayList<Course>();
  }

  /**
   * @brief Displays the details of a course.
   * @param course The course for which to display the details.
   * @return A string containing the course details.
   */
  public String displayDetails(Course course) {
    if (!courseExists(course)) {
      System.err.println("Course does not exist.");
      return null;
    }
    return String.format("%d, %s, %s, %s, %d", course.getId(),
        course.getCourseCode(), course.getTitle(),
        course.getInstructor(), course.getCreditHours());
  }

  /**
   * @brief Retrieves a course by its ID.
   * @param id The ID of the course to retrieve.
   * @return The course with the specified ID, or null if not found.
   */
  public Course getCourseById(int id) {
    try {
      return db.fetchCourse(id);
    } catch (SQLException e) {
      System.err.println("Error retrieving course: " + e.getMessage());
      return null;
    }
  }

  /**
   * @brief Imports courses from a file.
   * @param file The file containing the course data.
   * @return List of imported courses.
   */
  public List<Course> importCourses(File file) {
    List<Course> courses = Reader.readCSV(file, Reader::parseCourseImport);
    return courses;
  }

  /**
   * @brief Exports courses to a file.
   * @param courses The courses to export.
   * @return true if the courses were exported successfully, false otherwise.
   */
  public boolean exportCourses(List<Course> courses) {
    Reader.writeCSV(new File("courses_export.csv"), courses,
        Reader::parseCourseExport);
    return true;
  }
}

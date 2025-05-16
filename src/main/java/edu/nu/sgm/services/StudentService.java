/**
 * @file StudentService.java
 * @brief Service class for managing students in the grading system.
 */

package edu.nu.sgm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;

/**
 * @class StudentService
 * @brief Provides methods to add, remove, update, and retrieve students and their courses.
 */
public class StudentService {
    private DatabaseManager db = new DatabaseManager();
    private List<Student> st = getStudents(); // list all the students in it

    /**
     * @brief Checks if a name contains special characters or digits.
     * @param name The name to check.
     * @return true if valid, false otherwise.
     */
    private boolean checkspecial(String name) {
        Set<Character> specialChars = new HashSet<>(Arrays.asList('`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '=', '+'));
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (specialChars.contains(c)) {
                return false; // check that any name does not have any of those special characters
            }
            if (!Character.isLetter(c)) {
                return false; // check that any name does not have digits
            }
        }
        return true;
    }

    /**
     * @brief Validates a name based on length and character rules.
     * @param name The name to validate.
     * @return true if valid, false otherwise.
     */
    private boolean checkName(String name) {
        name = name == null ? "" : name.trim();
        if (name.isEmpty()) {
            return false;
        }
        if (name.length() < 2 || name.length() > 50) { // logic length
            return false;
        }
        if (!checkspecial(name)) { // call function of the characters
            return false;
        }
        return true;
    }

    /**
     * @brief Adds a student to the database after validation.
     * @param s The student to add.
     * @return true if added successfully, false otherwise.
     */
    public boolean addstudent(Student s) {
        if (!(checkName(s.getFirstName()) && checkName(s.getLastName()))) {
            return false; // make sure that each student has the first name and the last name
        }
        // make sure about the exceptions
        try {
            return db.createStudent(s);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * @brief Retrieves all students from the database.
     * @return List of students.
     */
    public List<Student> getStudents() {
        try {
            return db.fetchStudents();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @brief Removes a student from the list and the database.
     * @param s The student to remove.
     * @return true if removed successfully, false otherwise.
     */
    public boolean removeStudent(Student s) {
        if (s == null || !st.contains(s)) {
            return false;
        }
        st.remove(s);
        try {
            return db.deleteStudent(s) == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * @brief Retrieves the courses a student is enrolled in.
     * @param s The student.
     * @return List of courses.
     */
    public List<Course> getCourses(Student s) {
        if (s == null || !st.contains(s)) {
            return new ArrayList<Course>();
        }
        try {
            return db.fetchCourses(s);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @brief Counts the total courses a student is enrolled in.
     * @param s The student.
     * @return The number of courses.
     */
    public int getTotalCourses(Student s) {
        int count = 0;
        for (Course _ : getCourses(s)) {
            count++;
        }
        return count;
    }

    /**
     * @brief Displays the details of a student.
     * @param s The student.
     * @return String with student details.
     */
    public String displayDetails(Student s) {
        if (s == null || !st.contains(s)) {
            return " ";
        }
        return String.format(
            "%d, %s, %s, %s",
            s.getId(),
            s.getFirstName(),
            s.getLastName(),
            s.getEmail()
        );
    }

    /**
     * @brief Retrieves a student by their ID.
     * @param id The student ID.
     * @return The student object or null if not found.
     */
    public Student getStudentById(int id) {
        try {
            List<Student> students = db.fetchStudents();
            for (Student student : students) {
                if (student.getId() == id) {
                    return student;
                }
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @brief Updates a student's information in the database.
     * @param student The student to update.
     * @return true if updated successfully, false otherwise.
     */
    public boolean updateStudent(Student student) {
        try {
            return db.updateStudent(student) > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
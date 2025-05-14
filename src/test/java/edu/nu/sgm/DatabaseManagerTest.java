package edu.nu.sgm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

  DatabaseManager db = new DatabaseManager();

  // @Test
  public void test_AddStudent() {
    Student student = new Student(0, "first", "last", "f@f");
    try {
      assertTrue(db.createStudent(student));
      assertEquals(1, student.getId());
      assertEquals(1, db.fetchStudents().size());
    } catch (SQLException e) {
      System.out.println("ERROR");
    }
  }

  @Test
  public void test_AddCourse() {
    Course course = new Course(0, "CSCI-217", "Programming", "Dr. Sameh", 3);
    System.out.println(course.getTotalStudents());
    try {
      assertTrue(db.createCourse(course));
      assertEquals(1, course.getId());
      assertEquals(1, db.fetchCourses().size());
    } catch (SQLException e) {
      System.out.println("ERROR:" + e.getMessage());
    }
  }

  @Test
  public void test_AddEnrollment() {
    Student student = new Student(0, "first", "last", "f@f");
    Course course = new Course(0, "CSCI-217", "Programming", "Dr. Sameh", 3);
    try {
      db.createStudent(student);
      db.createCourse(course);
    } catch (SQLException e) {
      System.out.println("ERROR: " + e);
    }
    Enrollment enrollment = new Enrollment(0, student.getId(), course.getId());
    try {
      assertTrue(db.createEnrollment(enrollment));
      assertEquals(1, enrollment.getId());
      assertEquals(1, db.fetchEnrollment(student, course).size());
    } catch (SQLException e) {
      System.out.println("ERROR: " + e);
    }
  }

  @Test
  public void test_RemoveCourse() {
    Course course = new Course(0, "CSCI-217", "Programming", "Dr. Sameh", 3);
    try {
      db.createCourse(course);
      assertTrue(db.deleteCourse(course) > 0);
      assertEquals(0, db.fetchCourses());
    } catch (SQLException e) {
      System.out.println("ERROR: " + e);
    }
  }
}

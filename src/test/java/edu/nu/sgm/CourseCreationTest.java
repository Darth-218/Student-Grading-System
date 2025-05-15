package edu.nu.sgm;

import static org.junit.jupiter.api.Assertions.*;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import org.junit.jupiter.api.Test;
import java.io.File;

public class CourseCreationTest {

  CourseService cs = new CourseService();

  @Test
  public void testAddCourse_Success() {
    Course course = new Course(
        1, "CSCI-101", "Introduction to Computer Science", "Dr. Smith", 3);
    boolean result = cs.addCourse(course);
    assertTrue(result, "Course should be added successfully");

  }

  @Test
  public void testRemoveCourse_Success() {
    Course course = new Course(10, "CSCI-201", "Software Engineering", "Dr. Lee", 3);
    cs.addCourse(course);
    boolean removed = cs.removeCourse(course);
    assertTrue(removed, "Course should be removed successfully");
  }

  @Test
  public void testDisplayDetails_Success() {
    Course course = new Course(5, "BIOl-101", "Biology Basics", "Dr. Green", 3);
    cs.addCourse(course);

    String details = cs.displayDetails(course);
    assertNotNull(details, "Course details should not be null");
    assertTrue(details.contains("BIOl-101"),
        "Course details should contain course code");
    assertTrue(details.contains("Biology Basics"),
        "Course details should contain course name");
    assertTrue(details.contains("Dr. Green"),
        "Course details should contain instructor name");
    assertTrue(details.contains("3"), "Course details should contain credits");
  }

  @Test
  public void testImportCourses_Success() {
    File file = new File("courses.csv");
    java.util.List<Course> importedCourses = cs.importCourses(file);
    for (int i = 0; i < importedCourses.size(); i++) {
      assertTrue(cs.addCourse(importedCourses.get(i)));
    }
    assertNotNull(importedCourses, "Imported courses list should not be null");
    assertTrue(importedCourses.isEmpty(), "Courses should be imported successfully");
  }

  @Test
  public void testExportCourses_Success() {

    Course course = new Course(2, "MATH-101", "Calculus I", "Dr. Brown", 3);
    cs.addCourse(course);
    java.util.List<Course> courses = new java.util.ArrayList<>();
    courses.add(course);
    cs.exportCourses(courses);
  }

}

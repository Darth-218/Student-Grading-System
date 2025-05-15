package edu.nu.sgm;

import static org.junit.jupiter.api.Assertions.*;

import edu.nu.sgm.models.Course;
import edu.nu.sgm.services.CourseService;
import edu.nu.sgm.utils.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

}


    
# Student-Grading-System

## Project UML Diagram

```mermaid
classDiagram
    class Student {
        -int id
        -String first_name
        -String last_name
        -String email
        +getId() int
        +setId() void
        +getFirstName() String
        +setFirstName(String name) bool
        +getLastName() String
        +setLastName(String name) bool
        +generateEmail() String
        +getEmail() String
        +setEmail() void
    }

    class StudentService {
        -Databasemanager db
        +List~Student~ students
        +addStudent(Student student) bool
        +removeStudent(Student student) bool
        +getCourses(Student student) List~Course~
        +getTotalCourses(Student student) int
        +displayDetails(Student student) String
        +importStudents(File csv) boolean
        +exportStudents() boolean
    }

    class Course {
        -int id
        -String course_code
        -String title
        -String instructor
        -int credit_hours
        -int total_students
        +getId() int
        +setId() void
        +getCourseCode() String
        +setCourseCode(String id) bool
        +getTitle() String
        +setTitle(String name) bool
        +getInstructor() String
        +setInstructor(String instructor) void
        +getCreditHours() int
        +setCreditHours(int credit_hours) void
        +getTotalStudents() int
        +setTotalStudents() void
    }

    class CourseService {
        -Databasemanager db
        +List~Course~ courses
        +addCourse(Course course) bool
        +removeCourse(Course course) bool
        +getStudents(Course course) List~Student~
        +getTotalStudents(Course course) int
        +displayDetails(Course course) String
        +importCourses(File file) boolean
        +exportCourses() boolean
    }

    class GradeItem {
        -int id
        -String title
        -String category
        -double score
        -double max_score
        -double weight
        -String feedback
        +getTitle() String
        +setTitle(String title) void
        +getCategory() String
        +setCategory(String category) void
        +getWeight() double
        +setWeight() void
        +getScore() double
        +getMaxScore() double
        +setScore(double score, double max_score) void
        +getFeedback() String
        +setFeedback(String feedback) void
    }

    class GradeItemService {
        -Databasemanager db
        +addGradeItem(Enrollment enrollment, GradeItem grade) bool
        +removeGradeItem(GradeItem grade) bool
        +calculateTotalGrade(Enrollment enrollment) double
        +importGrades(File file) boolean
        +exportGrades() boolean
    }

    class Enrollment {
        -int id
        -int student_id
        -int course_id
        +getId() int
        +setId() int
        +getStudent() int
        +getcourse() int
    }

    class EnrollmentService {
        -Databasemanager db
        +enrollStudent(Student student, Course course) bool
        +dropCourse(Student student, Course course) bool
        +getGrades(Student student, Course course) List~GradeItem~
        +calculateGPA(Student student) double
        +generateReportCard(Student student) String
    }
```

*Controller classes are not yet defined*

## Project Sequence Diagram

### Adding a Student

```mermaid
sequenceDiagram
    actor User

    User->>StudentsView: User adds a student
    StudentsView->>Controller: newStudent(Student)
    Controller->>Service: addStudent(Student)
    Service->>DatabaseManager: createStudent(Student)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>Service: Add the student to ObservableList<Student>
    Service->>Controller: Returns ObservableList<Student>
    Controller->>StudentsView: Sets ObservableList<Student>
    StudentsView->>StudentsView: Refresh UI
    StudentsView-->>User: Show students
```

### Viewing a Student's Courses

```mermaid
sequenceDiagram
    actor User

    User->>StudentsView: User Selects a student
    StudentsView->>Controller: viewCourses(Student)
    Controller->>Service: getCourses(Student)
    Service->>DatabaseManager: fetchCourse(Student)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Returns courses ResultSet
    DatabaseManager->>Service: Returns List<Course>
    Service->>Controller: Returns ObservableList<Course>
    Controller->>StudentsView: Sets ObservableList<Course>
    StudentsView->>StudentsView: Refresh UI
    StudentsView-->>User: Show courses
```

### Removing a Student

```mermaid
sequenceDiagram
    actor User

    User->>StudentsView: User Selects a student and clicks "delete"
    StudentsView->>Controller: deleteStudent(Student)
    Controller->>Service: removeStudent(Student)
    Service->>DatabaseManager: deleteStudent(Student)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>Service: Remove the student from the ObservableList<Student>
    Service->>Controller: Sets ObservableList<Student>
    Controller->>StudentsView: Refresh UI
    StudentsView-->>User: Show students
```

### Adding a Course

```mermaid
sequenceDiagram
    actor User

    User->>CoursesView: User adds a course
    CoursesView->>Controller: newCourse(Course)
    Controller->>Service: addCourse(Course)
    Service->>DatabaseManager: createCourse(Course)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>Service: Add the course to ObservableList<Course>
    Service->>Controller: Returns ObservableList<Course>
    Controller->>CoursesView: Sets ObservableList<Course>
    CoursesView->>CoursesView: Refresh UI
    CoursesView-->>User: Show courses
```

### Viewing a Course's Students

```mermaid
sequenceDiagram
    actor User

    User->>CoursesView: User selects a course
    CoursesView->>Controller: viewStudents(Course)
    Controller->>Service: getStudents(Course)
    Service->>DatabaseManager: fetchStudents(Course)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Returns students ResultSet
    DatabaseManager->>Service: Add students to ObservableList<Student>
    Service->>Controller: Returns ObservableList<Student>
    Controller->>CoursesView: Sets ObservableList<Student>
    CoursesView->>CoursesView: Refresh UI
    CoursesView-->>User: Show students
```

### Removing a Course

```mermaid
sequenceDiagram
    actor User

    User->>CoursesView: User Selects a course and clicks "delete"
    CoursesView->>Controller: deleteCourse(Course)
    Controller->>Service: removeCourse(Course)
    Service->>DatabaseManager: deleteCourse(Course)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>Service: Remove the course from the ObservableList<Course>
    Service->>Controller: Sets ObservableList<Course>
    Controller->>CoursesView: Refresh UI
    CoursesView-->>User: Show courses
```

### Adding a Student to a Course

```mermaid
sequenceDiagram
    actor User

    User->>CoursesView: User selects a course and clicks "add student"
    CoursesView->>Controller: addEnrollment(Student, Course)
    Controller->>EnrollmentService: enrollStudent(Enrollment)
    EnrollmentService->>DatabaseManager: createEnrollment(Enrollment)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>EnrollmentService: Add the student to the ObservableList<Student>
    EnrollmentService->>Controller: Sets ObservableList<Student>
    Controller->>CoursesView: Refresh UI
    CoursesView-->>User: Show students
```

### Adding a Course to a Student

```mermaid
sequenceDiagram
    actor User

    User->>StudentsView: User Selects a students and clicks "add course"
    StudentsView->>Controller: addEnrollment(Student, Course)
    Controller->>EnrollmentService: enrollStudent(Enrollment)
    EnrollmentService->>DatabaseManager: createEnrollment(Enrollment)
    DatabaseManager->>Database: Executes query 
    Database->>DatabaseManager: Success
    DatabaseManager->>EnrollmentService: Add the course to the ObservableList<Course>
    EnrollmentService->>Controller: Sets ObservableList<Course>
    Controller->>StudentsView: Refresh UI
    StudentsView-->>User: Show courses
```

### Viewing a Student's Grades

```mermaid
sequenceDiagram
    actor User

    User->>StudentsView: User Selects a students and then a course
    StudentsView->>Controller: showGrades(Student, Course)
    Controller->>EnrollmentService: getGrades(Enrollment)
    EnrollmentService->>DatabaseManager: fetchGrades(Enrollment)
    DatabaseManager->>Database: Executes query
    Database->>DatabaseManager: Return grade ResultSet
    DatabaseManager->>EnrollmentService: Return ObservableList<GradeItem>
    EnrollmentService->>Controller: Sets ObservableList<GradeItem>
    Controller->>StudentsView: Switch to GradeItemView
    StudentsView-->>User: Show grades
```

### Adding a Grade
### Removing a Grade

---

# Project Code Standards

## Class Headers

Each class should have a header that contains:
1. The name of the class.
2. What the class represents.
3. Class methods overview.
5. The author of the module.

## Naming Conventions

7. Local Variables: Snake case or Flat case (i.e. `variable_name`, `variablename`)
8. Global Variables: Pascal snake case (i.e. `Global_Variable`)
9. Constants: Capital letters only (i.e. `CONSTANT`)
10. Functions: Camel case (i.e. `functionName`)
11. Classes: Pascal case (i.e. `ClassName`)

*Avoid including digits as much as possible.*

## Code Format

1. Indentation size: 2 space.
2. Braces: Open brace on the same line as the control statement.

## Documentation

### Class Documentation

```java
/**
 * @brief Represents <object>
 * @details Stores ...
 *          Links ...
 */
public class Class {
    // ...
}
```

### Method Documentation

```java
/**
 * @brief Adds two numbers.
 * @param a The first number.
 * @param b The second number.
 * @return The sum of the two numbers.
 * @throws IllegalArgumentException If one of the numbers is negative.
 */
public int addTwoNumbers(int a, int b) {
    // ...
}
```

### Field (Variable) Documentation

```java
private int integer; ///< A random integer.
private String test; ///< A test string.
```

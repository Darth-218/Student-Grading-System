package edu.nu.sgm.models;

public class Student {
  // attributes all details of the student
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  // Constructor all the attributes 
  public Student(int id, String firstName, String lastName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
  // Getter and Setter
  public int getId() { return id; }

  public void setId(int id) { this.id = id; }

  public String getFirstName() { return firstName; }

  public void setName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }

  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  // generating email of the student to each student template email
  public String generateStudentEmail() {
    String first = firstName.split(" ")[0].toLowerCase();
    String last = lastName.toLowerCase().replaceAll("[^a-z]", "");
    int studentId = id;
    return String.format("%s.%s%d@nu.edu.eg", first, last, studentId);
  }
}

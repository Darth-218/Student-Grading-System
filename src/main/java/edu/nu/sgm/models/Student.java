package edu.nu.sgm.models;

public class Student {
    private int id;
    private String name;
    private String last_name;
    private String email;

  Student(int id, String name, String last_name, String email) {
    this.id = id;
    this.name = name;
    this.last_name = last_name;
    this.email = generateStudentEmail();
  }

  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id=id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  
  public String getLastName() {
    return last_name;
  }
  
  public void setLastName(String last_name) {
    this.last_name = last_name;
  }
  
  public String getEmail() {
    return email;
  }

  public String generateStudentEmail() {
      String firstName = name.split(" ")[0].toLowerCase();
      String lastName = last_name.toLowerCase().replaceAll("[^a-z]", "");
      int studentId = (int)id;
      return String.format("%s.%s%d@nu.edu.eg",firstName, lastName, studentId);
  }
}
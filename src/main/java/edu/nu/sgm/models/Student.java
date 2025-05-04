package edu.nu.sgm.models;

public class Student {
  private String name;
  private double id;
  private String enrolled_courses;
  private String email;

  Student(String name, double id, String enrolled_courses, String email) {
    this.name = name;
    this.id = id;
    this.enrolled_courses = enrolled_courses;
    this.email = email;
  }

  public void setname(String name) { this.name = name; }

  public void setid(double id) { this.id = id; }

  public void setenrolled_courses(String enrolled_courses) {
    this.enrolled_courses = enrolled_courses;
  }

  public void setemail(String email) { this.email = email; }

  public String getname() { return name; }

  public double getid() { return id; }

  public String getenrolled_courses() { return enrolled_courses; }

  public String getemail() { return email; }
}

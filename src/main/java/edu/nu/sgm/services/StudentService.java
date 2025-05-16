package edu.nu.sgm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;

public class StudentService {
    private DatabaseManager db = new DatabaseManager();
    private List<Student> st= getStudents(); // list all the students in it
   // check that that any name have correct way
    private boolean checkspecial(String name){
      int a[]= new int[]{'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '=' ,'+'};
      for (int i=0;i<name.length();i++){
        if(Arrays.asList(a).contains(name.charAt(i))) {
            return false; //check that any name do not have any of those special characters
        }
        if(!Character.isLetter(name.charAt(i))) {
            return false;  //check that any name do not have digits
        }
      }
   
      return true;
    }
    private boolean checkName(String name){
    name = name.trim();   
    if (name == null|| name.isEmpty()){  
        return false;
       }
    if(name.length()<2 && name.length()>50){ // logic length
        return false;
    }
    if(!checkspecial(name)){  // call funcation of the characters
        return false;
    }
    return true;
   }
    public boolean addstudent(Student s){
        if (!(checkName(s.getFirstName()) && checkName(s.getLastName()))) {
            return false;   // make sure that each student have the first name and the last name
        }
        // make sure about the exceptions
        try {
            return db.createStudent(s);
        } catch (SQLException e) {
            return false;
        }
     }

  public List<Student> getStudents(){
    try {
            return db.fetchStudents();
        }
    catch (SQLException e) {
           return null;
        }
     
  }
    // removing student from the list and the database
     public boolean removeStudent(Student s){
        if(s == null || !st.contains(s)){
            return false;
        }
        st.remove(s);
        try {
            return db.deleteStudent(s) == 1;
        }
        catch (SQLException e) {
           return false;
        }
         
     }
     // list each student with the courses that pick with each student
     public List<Course> getCourses(Student s) {
        if(s== null || !st.contains(s)){
         return new ArrayList<Course>();
        }
        try {
            return db.fetchCourses(s);
        }
        catch (SQLException e) {
           return null;
        }
        
    }
    // count the total course that enrollment for each student
     public int getTotalCourses(Student s) {
         int count = 0;
        for(Course _ : getCourses(s)){
            count++;
        }
        return count;
    }
// finally display the details of the student 
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

    // Add this method to StudentService class
    // (Removed duplicate updateStudent method)

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

    public boolean updateStudent(Student student) {
        try {
            return db.updateStudent(student) > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
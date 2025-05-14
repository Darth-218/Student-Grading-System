package edu.nu.sgm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.nu.sgm.models.*;
import edu.nu.sgm.utils.DatabaseManager;

public class StudentService {
    private DatabaseManager db = new DatabaseManager();
    private List<Student> st= getStudents(); 
   private boolean checkspecial(String name){
      int a[]= new int[]{'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '=' ,'+'};
      for (int i=0;i<name.length();i++){
        if(Arrays.asList(a).contains(name.charAt(i))) {
            return false;
        }
        if(!Character.isLetter(name.charAt(i))) {
            return false;  //check digit
        }
      }
   
      return true;
    }
    private boolean checkName(String name){
    name = name.trim();   
    if (name == null|| name.isEmpty()){
        return false;
       }
    if(name.length()<2 && name.length()>50){
        return false;
    }
    if(!checkspecial(name)){
        return false;
    }
    return true;
   }
    public boolean addstudent(Student s){
        if (!(checkName(s.getFirstName()) && checkName(s.getLastName()))) {
            return false;
        }
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
    
     public int getTotalCourses(Student s) {
         int count = 0;
        for(Course _ : getCourses(s)){
            count++;
        }
        return count;
    }

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

}
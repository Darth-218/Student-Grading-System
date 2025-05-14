import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

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
   }
    public boolean addstudent(Student s){
        if (!(checkName(s.getFirstName()) && checkName(s.getLastName()))) {
            return false;
        }
        db.createStudent(s);
     }

  public List<Student> getStudents(){
     return db.fetchStudents();
  }

     public boolean removeStudent(Student s){
        if(s == null || !st.contains(s)){
            return false;
        }
        st.remove(s);
        return db.deleteStudent(s) == 1;
     }
     public List<Course> getCourses(Student s) {
        if(s== null || !st.contains(s)){
         return new ArrayList<Course>();
        }
        return db.fetchCourses(s);
    }
    
     public int getTotalCourses(Student s) {
         int count = 0;
        for(Course c : getCourses(s)){
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
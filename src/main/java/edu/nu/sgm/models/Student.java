/**
 * @file Student.java
 * @brief Represents a student in the grading system.
 */

package edu.nu.sgm.models;

/**
 * @class Student
 * @brief Represents a student with ID, name, and email.
 */
public class Student {
  private int id;
  private String firstName;
  private String lastName;
  private String email;

  /**
   * @brief Constructs a Student object with the specified details.
   * @param id        The student's ID.
   * @param firstName The student's first name.
   * @param lastName  The student's last name.
   * @param email     The student's email address.
   */
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

  /**
   * @brief Generates an email for the student based on their name and ID.
   * @return The generated email address.
   */
  public String generateEmail() {
    /**
     * @brief A method generate email of each student 
     * @return email that contain the first letter of the student, the last name and 4 numbers from the id
     */    
        String fi = firstName.substring(0, 1).toLowerCase();
        String cln = lastName.toLowerCase().replaceAll("[^a-z]", "");
        
        String idStr = String.valueOf(id);
        String id_Part = "";
        if (idStr.length() >= 4) {
            String firstTwo = idStr.substring(0, 2);
            String lastTwo = idStr.substring(idStr.length() - 2);
            id_Part = firstTwo + lastTwo;
        } 
        else {
            id_Part = idStr;
        }
        
        return String.format("%s.%s%s@nu.edu.eg", fi, cln, id_Part);
    }
}

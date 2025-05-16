/**
 * @file GradeItem.java
 * @brief Represents a single grade item for a student.
 *
 * A GradeItem includes information such as title, category,
 * score, maximum possible score, feedback, an identifier, and
 * the item's weight in the total grade calculation.
 */

package edu.nu.sgm.models;

/**
 * @class GradeItem
 * @brief Represents a grade item (e.g., assignment, exam) for a student.
 */
public class GradeItem {

  private String title;           /**< Title of the grade item */
  private String category;        /**< Category (e.g., exam, homework) */
  private double score;           /**< Score obtained */
  private double max_score;       /**< Maximum possible score */
  private String feedback;        /**< Feedback from the instructor */
  private int id;                 /**< Unique identifier */
  private double weight;          /**< Weight as percentage of total grade */

  /**
   * @brief Constructs a GradeItem with specified parameters.
   * @param id Unique identifier
   * @param title Title of the item
   * @param category Category of the item
   * @param score Score obtained
   * @param max_score Maximum possible score
   * @param feedback Feedback text
   * @param weight Weight as a percentage of the total grade
   */
  public GradeItem(int id, String title, String category, double score,
                   double max_score, String feedback, double weight) {
    this.id = id;
    this.title = title;
    this.category = category;
    this.score = score;
    this.max_score = max_score;
    this.feedback = feedback;
    this.weight = weight;
  }

  /**
   * @brief Sets the title of the grade item.
   * @param name The new title
   * @return True if the title is valid and set; false otherwise
   */
  public boolean setTitle(String name) {
    if (name == null || name.isEmpty())
      return false;
    this.title = name;
    return true;
  }

  /**
   * @brief Gets the title of the grade item.
   * @return The title
   */
  public String getTitle() { return this.title; }

  /**
   * @brief Sets the category of the grade item.
   * @param category The new category
   * @return True if the category is valid and set; false otherwise
   */
  public boolean setCategory(String category) {
    if (category == null || category.isEmpty())
      return false;
    this.category = category;
    return true;
  }

  /**
   * @brief Gets the category of the grade item.
   * @return The category
   */
  public String getCategory() { return this.category; }

  /**
   * @brief Sets the score and maximum score of the grade item.
   * @param score The score obtained
   * @param maxScore The maximum possible score
   * @return True if values are valid and set; false otherwise
   */
  public boolean setScore(double score, double maxScore) {
    if (score < 0 || maxScore <= 0 || score > maxScore)
      return false;
    this.score = score;
    this.max_score = maxScore;
    return true;
  }

  /**
   * @brief Gets the score of the grade item.
   * @return The score
   */
  public double getScore() { return this.score; }

  /**
   * @brief Sets the feedback for the grade item.
   * @param feedback The feedback text
   * @return Always true
   */
  public boolean setFeedback(String feedback) {
    this.feedback = feedback;
    return true;
  }

  /**
   * @brief Gets the feedback for the grade item.
   * @return The feedback
   */
  public String getFeedback() { return this.feedback; }

  /**
   * @brief Gets the unique identifier of the grade item.
   * @return The ID
   */
  public int getId() { return this.id; }

  /**
   * @brief Sets the unique identifier of the grade item.
   * @param id The ID to set
   * @return True if the ID is valid and set; false otherwise
   */
  public boolean setId(int id) {
    if (id < 0)
      return false;
    this.id = id;
    return true;
  }

  /**
   * @brief Gets the maximum score for the grade item.
   * @return The maximum score
   */
  public double getMaxScore() { return this.max_score; }

  /**
   * @brief Sets the maximum score for the grade item.
   * @param maxScore The maximum score to set
   * @return True if valid; false otherwise
   */
  public boolean setMaxScore(double maxScore) {
    if (maxScore <= 0 || this.score > maxScore)
      return false;
    this.max_score = maxScore;
    return true;
  }

  /**
   * @brief Sets the weight of the grade item.
   * @param weight The weight as a percentage (0–100)
   * @return True if valid; false otherwise
   */
  public boolean setWeight(double weight) {
    if (weight < 0 || weight > 100)
      return false;
    this.weight = weight;
    return true;
  }

  /**
   * @brief Gets the weight of the grade item.
   * @return The weight as a percentage
   */
  public double getWeight() { return this.weight; }
}

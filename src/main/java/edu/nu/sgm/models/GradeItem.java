package edu.nu.sgm.models;

public class GradeItem {

  private String title;
  private String category;
  private double score;
  private double max_score;
  private String feedback;
  private int id;

  // constructor 
  public GradeItem(String title, String category, double score, double max_score, String feedback, int id) {
  this.title = title;
  this.category = category;
  this.score = score;
  this.max_score = max_score;
  this.feedback = feedback;
  this.id = id;
}

  // Title getter/setter
  public boolean setTitle(String name) {
    if (name == null || name.isEmpty())
      return false;
    this.title = name;
    return true;
  }

  public String getTitle() { return this.title; }

  // category getter/setter
  public boolean setCategory(String category) {
    if (category == null || category.isEmpty())
      return false;
    this.category = category;
    return true;
  }

  public String getCategory() { return this.category; }

  // score setter/getter
  public boolean setScore(double score, double maxScore) {
    if (score < 0 || maxScore <= 0 || score > maxScore)
      return false;
    this.score = score;
    this.max_score = maxScore;
    return true;
  }

  public double getScore() { return this.score; }

  // feedback getter/setter
  public boolean setFeedback(String feedback) {
    if (feedback == null)
      return false;
    this.feedback = feedback;
    return true;
  }

  public String getFeedback() { return this.feedback; }

  // id getter/setter
  public int getId() {
    return this.id;
  }

  public double getMaxScore() {
    return this.max_score;
  }

}

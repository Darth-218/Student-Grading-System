package edu.nu.sgm.models;

public class GradeItem {

  private String title;
  private String category;
  private double score;
  private double max_score;
  private String feedback;

  public boolean setName(String name) {
    if (name == null || name.isEmpty())
      return false;
    this.title = name;
    return true;
  }

  public String getName() { return this.title; }

  public boolean setCategory(String category) {
    if (category == null || category.isEmpty())
      return false;
    this.category = category;
    return true;
  }

  public String getCategory() { return this.category; }

  public boolean setScore(double score, double maxScore) {
    if (score < 0 || maxScore <= 0 || score > maxScore)
      return false;
    this.score = score;
    this.max_score = maxScore;
    return true;
  }

  public double getScore() { return this.score; }

  public boolean setFeedback(String feedback) {
    if (feedback == null)
      return false;
    this.feedback = feedback;
    return true;
  }

  public String getFeedback() { return this.feedback; }
}

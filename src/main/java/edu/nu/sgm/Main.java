package edu.nu.sgm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/nu/sgm/views/main-view.fxml"));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
      stage.setTitle("Window");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
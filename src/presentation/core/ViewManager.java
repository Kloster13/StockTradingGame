package presentation.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewManager
{
  private static BorderPane mainLayout;
  private final static String fxmlDirectoryPath = "/fxml/";
  private static ControllerFactory controllerFactory;

  private ViewManager()
  {
  }

  public static void init(Stage primaryStage, String initialView) throws IOException
  {
    controllerFactory = new ControllerFactory();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(ViewManager.class.getResource(fxmlDirectoryPath + initialView + ".fxml"));
    loader.setControllerFactory(controllerFactory);
    BorderPane root = loader.load();
    mainLayout = root;
    Scene scene = new Scene(root, 1000, 700);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void showView(String viewName)
  {
    ViewManager.showView(viewName, null);
  }

  public static void showView(String viewName, String argument)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(ViewManager.class.getResource(fxmlDirectoryPath + viewName + ".fxml"));
      loader.setControllerFactory(controllerFactory);
      Parent root = loader.load();
      Object controller = loader.getController();
      if (controller instanceof AcceptsStringArgument)
      {
        ((AcceptsStringArgument) controller).setArgument(argument);
      }
      mainLayout.setCenter(root);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      Alert error = new Alert(Alert.AlertType.ERROR, "cannot find view: " + viewName);
      error.show();
      throw new RuntimeException(e);
    }
  }
}

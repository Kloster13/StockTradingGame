import javafx.application.Application;
import javafx.stage.Stage;
import presentation.core.ViewManager;

public class RunApp extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    System.out.printf("FASfd");
    ViewManager.init(primaryStage, "MainView");
    ViewManager.showView("Home");
  }
}

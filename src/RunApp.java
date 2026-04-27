import javafx.application.Application;
import javafx.stage.Stage;
import presentation.core.ViewManager;
import shared.logging.FileLogAdapter;
import shared.logging.Logger;

public class RunApp extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    Logger.getInstance().setOutput(new FileLogAdapter());
    ViewManager.init(primaryStage, "MainView");
    ViewManager.showView("Home");
  }
}

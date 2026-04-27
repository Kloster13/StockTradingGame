import business.services.GameService;
import javafx.application.Application;
import javafx.stage.Stage;
import persistence.interfaces.UnitOfWork;
import presentation.core.ViewManager;
import shared.configuration.AppConfiguration;
import shared.logging.ConsoleLogOutput;
import shared.logging.Logger;

public class RunAppInTest extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    AppConfiguration.getAppConfiguration().setDirectoryPath("src/data/testdata/");
    Logger.getInstance().setOutput(new ConsoleLogOutput());
    ViewManager.init(primaryStage, "MainView");
    ViewManager.showView("Home");
  }
}


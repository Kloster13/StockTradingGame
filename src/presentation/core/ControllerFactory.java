package presentation.core;

import javafx.util.Callback;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.interfaces.UnitOfWork;
import presentation.controllers.MainViewController;
import presentation.controllers.PortfolioViewController;

public class ControllerFactory implements Callback<Class<?>, Object>
{
  @Override public Object call(Class<?> controllerType)
  {
    if (controllerType == MainViewController.class)
      return new MainViewController();
    if(controllerType == PortfolioViewController.class) return new PortfolioViewController();

    throw new RuntimeException("Cant find controller of type " + controllerType.getSimpleName());
  }

  private UnitOfWork getUOW()
  {
    return new FileUnitOfWork("src/data/testdata/");
  }
}

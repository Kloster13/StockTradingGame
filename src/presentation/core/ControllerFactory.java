package presentation.core;

import javafx.util.Callback;
import presentation.controllers.HomeViewController;
import presentation.controllers.MainViewController;
import presentation.controllers.PortfolioViewController;
import presentation.controllers.StockMarketController;

public class ControllerFactory implements Callback<Class<?>, Object>
{
  private final AppContext context = AppContext.getAppContext();
  @Override public Object call(Class<?> controllerType)
  {
    if (controllerType == MainViewController.class)
      return new MainViewController();
    if (controllerType == PortfolioViewController.class)
      return new PortfolioViewController(context.createPortfolioViewModel());
    if (controllerType == StockMarketController.class)
      return new StockMarketController(context.createStockMarketViewModel());
    if(controllerType== HomeViewController.class)
      return new HomeViewController(context.createHomeViewModel());

    throw new RuntimeException("Cant find controller of type " + controllerType.getSimpleName());
  }
}

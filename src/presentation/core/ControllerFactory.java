package presentation.core;

import business.services.GameService;
import business.services.PortfolioService;
import javafx.util.Callback;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import presentation.controllers.MainViewController;
import presentation.controllers.PortfolioViewController;
import presentation.controllers.StockMarketController;
import presentation.viewmodels.PortfolioViewModel;
import presentation.viewmodels.StockMarketViewModel;

public class ControllerFactory implements Callback<Class<?>, Object>
{
  private final FileUnitOfWork uow = new FileUnitOfWork("src/data/testdata/");
  private final StockDao stockDao = new StockDaoFileImplementation(uow);
  private final OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(uow);
  private final PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(uow);
  private final StockPriceHistoryDao historyDao = new StockPriceHistoryDaoFileImplementation(uow);

  @Override public Object call(Class<?> controllerType)
  {
    if (controllerType == MainViewController.class)
      return new MainViewController();
    if (controllerType == PortfolioViewController.class)
      return createPortfolioController();
    if (controllerType == StockMarketController.class)
      return createStockMarketController();

    throw new RuntimeException("Cant find controller of type " + controllerType.getSimpleName());
  }

  private PortfolioViewController createPortfolioController()
  {
    PortfolioViewModel vm = new PortfolioViewModel(createPortfolioService());
    return new PortfolioViewController(vm);
  }

  private StockMarketController createStockMarketController()
  {
    StockMarketViewModel vm = new StockMarketViewModel(
        new GameService(uow, ownedStockDao, stockDao, historyDao,portfolioDao));
    return new StockMarketController(vm);
  }

  /// ///////////////////////////////
  private PortfolioService createPortfolioService()
  {
    return new PortfolioService(ownedStockDao, portfolioDao, stockDao);
  }
}

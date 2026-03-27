package presentation.core;

import business.services.PortfolioService;
import javafx.util.Callback;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import presentation.controllers.MainViewController;
import presentation.controllers.PortfolioViewController;
import presentation.viewmodels.PortfolioViewModel;

public class ControllerFactory implements Callback<Class<?>, Object>
{
  @Override public Object call(Class<?> controllerType)
  {
    if (controllerType == MainViewController.class)
      return new MainViewController();
    if (controllerType == PortfolioViewController.class)
      return createPortfolioController();

    throw new RuntimeException("Cant find controller of type " + controllerType.getSimpleName());
  }

  private PortfolioViewController createPortfolioController()
  {
    PortfolioViewModel vm = new PortfolioViewModel(createPortfolioService());
    return new PortfolioViewController(vm);
  }


//////////////////////////////////
  private FileUnitOfWork createUOW()
  {
    return new FileUnitOfWork("src/data/testdata/");
  }

  private StockDao createStockDao()
  {
    return new StockDaoFileImplementation(createUOW());
  }

  private OwnedStockDao createOwnedStockDao()
  {
    return new OwnedStockDaoFileImplementation(createUOW());
  }

  private TransactionDao createTransactionDao()
  {
    return new TransactionDaoFileImplementation(createUOW());
  }

  private PortfolioDao createPortfolioDao()
  {
    return new PortfolioDaoFileImplementation(createUOW());
  }

  private PortfolioService createPortfolioService(){
    return new PortfolioService(createOwnedStockDao(),createPortfolioDao(),createStockDao());
  }
}

package presentation.core;

import business.services.GameService;
import business.services.PortfolioService;
import business.services.StockTransactionService;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import presentation.viewmodels.HomeViewModel;
import presentation.viewmodels.PortfolioViewModel;
import presentation.viewmodels.StockMarketViewModel;
import shared.configuration.AppConfiguration;

public class AppContext
{
  private static AppContext instance;
  private final String filePath = AppConfiguration.getAppConfiguration().getDirectoryPath();
  private GameService gameService;
  private UnitOfWork unitOfWork;
  private ActivePortfolioCache cache;

  public AppContext()
  {
    cache = new ActivePortfolioCache();
  }

  public static AppContext getAppContext()
  {
    if (instance == null)
    {
      instance = new AppContext();
    }
    return instance;
  }

  // Exposed viewmodels
  public PortfolioViewModel createPortfolioViewModel()
  {
    return new PortfolioViewModel(createPortfolioService(),cache);
  }

  public StockMarketViewModel createStockMarketViewModel()
  {
    return new StockMarketViewModel(createGameService(), createStockTransactionService(),
        createPortfolioService(),cache);
  }

  public HomeViewModel createHomeViewModel()
  {
    return new HomeViewModel(createGameService(), createPortfolioService(),cache);
  }

  // Services
  private GameService createGameService()
  {
    if (gameService == null)
    {
      UnitOfWork uow = creatUnitOfWork();
      OwnedStockDao ownedStockDao = createOwnedStockDao(uow);
      StockDao stockDao = createStockDao(uow);
      StockPriceHistoryDao historyDao = createStockPriceHistory(uow);
      PortfolioDao portfolioDao = createPortfolioDao(uow);
      gameService = new GameService(uow, ownedStockDao, stockDao, historyDao, portfolioDao);
    }
    return gameService;
  }

  private PortfolioService createPortfolioService()
  {
    UnitOfWork uow = creatUnitOfWork();
    OwnedStockDao ownedStockDao = createOwnedStockDao(uow);
    StockDao stockDao = createStockDao(uow);
    PortfolioDao portfolioDao = createPortfolioDao(uow);
    return new PortfolioService(ownedStockDao, portfolioDao, stockDao);
  }

  private StockTransactionService createStockTransactionService()
  {
    UnitOfWork uow = creatUnitOfWork();
    OwnedStockDao ownedStockDao = createOwnedStockDao(uow);
    StockDao stockDao = createStockDao(uow);
    PortfolioDao portfolioDao = createPortfolioDao(uow);
    TransactionDao transactionDao = createTransactionDao(uow);
    return new StockTransactionService(uow, ownedStockDao, portfolioDao, stockDao, transactionDao);
  }

  ///  DAOs
  private TransactionDao createTransactionDao(UnitOfWork unitOfWork)
  {
    return new TransactionDaoFileImplementation((FileUnitOfWork) unitOfWork);
  }

  private StockDao createStockDao(UnitOfWork unitOfWork)
  {
    return new StockDaoFileImplementation((FileUnitOfWork) unitOfWork);
  }

  private OwnedStockDao createOwnedStockDao(UnitOfWork unitOfWork)
  {
    return new OwnedStockDaoFileImplementation((FileUnitOfWork) unitOfWork);
  }

  private PortfolioDao createPortfolioDao(UnitOfWork unitOfWork)
  {
    return new PortfolioDaoFileImplementation((FileUnitOfWork) unitOfWork);
  }

  private StockPriceHistoryDao createStockPriceHistory(UnitOfWork unitOfWork)
  {
    return new StockPriceHistoryDaoFileImplementation((FileUnitOfWork) unitOfWork);
  }

  private UnitOfWork creatUnitOfWork()
  {
    if(unitOfWork==null)
      unitOfWork = new FileUnitOfWork(filePath);

    return unitOfWork;
  }
}

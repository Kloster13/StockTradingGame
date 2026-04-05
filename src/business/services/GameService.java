package business.services;

import business.services.eventhandlers.StockAlertService;
import business.services.eventhandlers.StockBankruptService;
import business.services.eventhandlers.StockListenerService;
import business.stockmarket.MarketTicker;
import business.stockmarket.TheStockMarket;
import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.*;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.util.List;

public class GameService
{
  private final UnitOfWork uow;
  private final Logger logger = Logger.getInstance();
  private final StockDao stockDao;
  private final TheStockMarket market;
  private final PortfolioDao portfolioDao;

  public GameService(UnitOfWork uow, OwnedStockDao ownedStockDao, StockDao stockDao,
      StockPriceHistoryDao historyDao, PortfolioDao portfolioDao)
  {
    this.uow = uow;
    this.stockDao = stockDao;
    this.portfolioDao = portfolioDao;
    market = TheStockMarket.getInstance();
    market.addListener(new StockListenerService(uow, stockDao, historyDao));
    market.addListener(new StockBankruptService(uow, ownedStockDao));
    market.addListener(new StockAlertService());
  }

  public void startGame()
  {
    logger.log("INFO", "Starting game");
    for (Stock stock : stockDao.getAllStocks())
    {
      logger.log("INFO", "Starting stock: " + stock.getSymbol() + " price: " + stock.getCurrentPrice());
      market.addLiveStock(stock);
    }
    TheStockMarket.getInstance().startMarket();
  }

  public void resetGame()
  {
    logger.log("INFO", "Resetting game");
    uow.reset();
    stopGame();
    market.resetMarket();
    setUpStartingStocksAndPortfolio();
  }

  public void stopGame()
  {
    logger.log("INFO", "Stopping game");
    market.stopMarket();
  }

  public List<Stock> getAllStocks()
  {
    return stockDao.getAllStocks();
  }

  private void setUpStartingStocksAndPortfolio()
  {
    uow.begin();
    double startingPrice = AppConfiguration.getAppConfiguration().getStockResetValue();
    stockDao.createStock(new Stock("GOOG", "Google", startingPrice));
    stockDao.createStock(new Stock("MET", "Meta", startingPrice));
    stockDao.createStock(new Stock("NVDA", "Nvidia", startingPrice));
    stockDao.createStock(new Stock("TSLA", "Tesla", startingPrice));
    portfolioDao.createPortfolio(new Portfolio(AppConfiguration.getAppConfiguration().getStartingBalance()));
    uow.commit();
  }
}

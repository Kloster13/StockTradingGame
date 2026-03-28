package business.services;

import business.services.eventhandlers.StockAlertService;
import business.services.eventhandlers.StockBankruptService;
import business.services.eventhandlers.StockListenerService;
import business.stockmarket.MarketTicker;
import business.stockmarket.TheStockMarket;
import domain.OwnedStock;
import domain.Stock;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.*;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

public class GameService
{
  private final UnitOfWork uow;
  private final Logger logger = Logger.getInstance();
  ;
  private final StockDao stockDao;
  private final TheStockMarket market;
  private final MarketTicker marketTicker = new MarketTicker();
  private final Thread marketThread = new Thread(marketTicker);

  public GameService(UnitOfWork uow, OwnedStockDao ownedStockDao, StockDao stockDao,
      StockPriceHistoryDao historyDao)
  {
    this.uow = uow;
    this.stockDao = stockDao;
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
      logger.log("INFO" , "Starting stock: "+stock.getSymbol() +" price: "+stock.getCurrentPrice());
      market.addLiveStock(stock);
    }
    marketThread.start();
  }

  public void resetGame()
  {
    logger.log("INFO", "Resetting game");
    uow.reset();
    for (Stock stock : stockDao.getAllStocks())
    {
      stock.setCurrentPrice(AppConfiguration.getAppConfiguration().getStockResetValue());
    }
  }

  public void stopGame()
  {
    logger.log("INFO", "Stopping game");
    marketTicker.stopMarket();
  }
}

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
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.StockDao;
import persistence.interfaces.UnitOfWork;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

public class GameService
{
  private final FileUnitOfWork uow;
  private final Logger logger = Logger.getInstance();
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;
  private final StockDao stockDao;
  private final TheStockMarket market;
  private final MarketTicker marketTicker = new MarketTicker();
  private final Thread marketThread = new Thread(marketTicker);

  public GameService(FileUnitOfWork uow)
  {
    this.uow = uow;
    this.ownedStockDao = new OwnedStockDaoFileImplementation(uow);
    this.portfolioDao = new PortfolioDaoFileImplementation(uow);
    this.stockDao = new StockDaoFileImplementation(uow);
    market = TheStockMarket.getInstance();
    market.addListener(new StockListenerService(uow));
    market.addListener(new StockBankruptService(uow));
    market.addListener(new StockAlertService(uow));
  }

  public void startGame()
  {
    for (Stock stock : stockDao.getAllStocks())
    {
      market.addNewLiveStock(stock.getSymbol());
    }
    marketThread.start();
  }

  public void resetGame()
  {
    uow.reset();
    for (Stock stock : stockDao.getAllStocks())
    {
      stock.setCurrentPrice(
          AppConfiguration.getAppConfiguration().getStockResetValue());
    }
  }

  public void stopGame()
  {
    marketTicker.stopMarket();
  }

}

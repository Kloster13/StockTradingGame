package Testing;

import business.services.StockListenerService;
import business.stockmarket.MarketTicker;
import business.stockmarket.TheStockMarket;
import domain.Stock;
import persistence.FileAccessException;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.StockDao;
import shared.logging.Logger;

import java.util.Optional;

public class MarketUpdateTest
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    Stock stockFromList = null;
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      stockFromList = stockDao.getStockById(1).orElse(stockFromList = null);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    TheStockMarket market = TheStockMarket.getInstance();

    market.addLiveStock(stockFromList);
    MarketTicker marketTicker = new MarketTicker();
    StockListenerService listenerService = new StockListenerService(tester);
    market.addListener(listenerService);
    try
    {
      marketTicker.runMarket();
    }
    catch (InterruptedException e)
    {
      Logger.getInstance().log("ERROR", "Interrupted");
    }
  }
}

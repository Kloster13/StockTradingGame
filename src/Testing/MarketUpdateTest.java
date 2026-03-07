package Testing;

import business.services.StockAlertService;
import business.services.StockBankruptService;
import business.services.StockListenerService;
import business.stockmarket.MarketTicker;
import business.stockmarket.TheStockMarket;
import domain.Stock;
import persistence.FileAccessException;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.StockDao;
import shared.logging.Logger;

public class MarketUpdateTest
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    TheStockMarket market = TheStockMarket.getInstance();
    try // Stocks
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockFromList = stockDao.getStockById(1).orElse(stockFromList = null);
      Stock stockFromList2 = stockDao.getStockById(2).orElse(stockFromList2 = null);
      market.addLiveStock(stockFromList);
      market.addLiveStock(stockFromList2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    MarketTicker marketTicker = new MarketTicker();
    StockListenerService stockListenerService = new StockListenerService(tester);
    StockBankruptService bankruptListenerService = new StockBankruptService(tester);
    StockAlertService stockAlertService = new StockAlertService(tester);

    market.addListener(stockListenerService);
    market.addListener(bankruptListenerService);
    market.addListener(stockAlertService);
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

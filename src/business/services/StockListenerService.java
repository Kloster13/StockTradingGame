package business.services;

import business.stockmarket.LiveStockDTU;
import domain.Stock;
import domain.StockPriceHistory;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.fileimplementation.StockPriceHistoryDaoFileImplementation;
import persistence.interfaces.StockDao;
import persistence.interfaces.StockPriceHistoryDao;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockListenerService implements PropertyChangeListener
{
  private FileUnitOfWork uow;
  private StockDao stockDao;
  private StockPriceHistoryDao historyDao;

  private Logger logger = Logger.getInstance();

  public StockListenerService(FileUnitOfWork uow)
  {
    this.uow = uow;
    stockDao = new StockDaoFileImplementation(uow);
    historyDao = new StockPriceHistoryDaoFileImplementation(uow);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String evtName = evt.getPropertyName();
    switch (evtName)
    {
      case "PriceUpdate" -> stockPersistenceUpdate((LiveStockDTU) evt.getNewValue());
      case "Bankrupt" -> System.out.println("Bankrupt");
      default -> System.out.println(evtName);
    }
  }

  private void stockPersistenceUpdate(LiveStockDTU liveStockDTU)
  {
    uow.begin();
    Stock stock = stockDao.getStockBySymbol(liveStockDTU.symbol())
        .orElseThrow(() -> new IllegalArgumentException("Stock not in list"));
    stock.setCurrentPrice(liveStockDTU.price());
    stock.setCurrentState(liveStockDTU.state());

    historyDao.createStockPriceHistory(
        new StockPriceHistory(liveStockDTU.symbol(), liveStockDTU.price()));
    stockDao.updateStock(stock);

    uow.commit();
    logger.log("INFO", stock.getSymbol() + " was updated to: " + stock.getCurrentPrice());
  }
}

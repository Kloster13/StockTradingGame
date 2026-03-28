package business.services.eventhandlers;

import business.services.DateUpdateException;
import business.services.dtos.LiveStockDTO;
import domain.Stock;
import domain.StockPriceHistory;
import persistence.FileAccessException;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.fileimplementation.StockPriceHistoryDaoFileImplementation;
import persistence.interfaces.StockDao;
import persistence.interfaces.StockPriceHistoryDao;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockListenerService implements PropertyChangeListener
{
  private UnitOfWork uow;
  private StockDao stockDao;
  private StockPriceHistoryDao historyDao;

  private Logger logger = Logger.getInstance();

  public StockListenerService(UnitOfWork uow, StockDao stockDao,
      StockPriceHistoryDao historyDao)
  {
    this.uow = uow;
    this.stockDao = stockDao;
    this.historyDao = historyDao;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String evtName = evt.getPropertyName();
    if (evtName.equals("PriceUpdate"))
    {
      stockPersistenceUpdate((LiveStockDTO) evt.getNewValue());
    }
  }

  private void stockPersistenceUpdate(LiveStockDTO liveStockDTO)
  {
    try
    {
      uow.begin();
      Stock stock = stockDao.getStockBySymbol(liveStockDTO.symbol())
          .orElseThrow(() -> new IllegalArgumentException("Stock not in list"));
      stock.setCurrentPrice(liveStockDTO.price());
      stock.setCurrentState(liveStockDTO.state());

      historyDao.createStockPriceHistory(
          new StockPriceHistory(liveStockDTO.symbol(), liveStockDTO.price()));
      stockDao.updateStock(stock);

      uow.commit();
      logger.log("INFO",
          stock.getSymbol() + " was updated to: " + stock.getCurrentPrice());
    }
    catch (IllegalArgumentException | FileAccessException e)
    {
      uow.rollback();
      logger.log("ERROR", e.getMessage());
      throw new DateUpdateException("Error in updating data");
    }
  }
}

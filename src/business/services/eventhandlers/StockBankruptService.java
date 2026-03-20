package business.services.eventhandlers;

import business.services.DateUpdateException;
import domain.OwnedStock;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class StockBankruptService implements PropertyChangeListener
{
  private final UnitOfWork uow;
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;

  private final Logger logger = Logger.getInstance();

  public StockBankruptService(FileUnitOfWork uow)
  {
    this.uow = uow;
    ownedStockDao = new OwnedStockDaoFileImplementation(uow);
    portfolioDao = new PortfolioDaoFileImplementation(uow);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("Bankrupt"))
      bankruptPersistenceUpdate((String) evt.getNewValue());
  }

  private void bankruptPersistenceUpdate(String symbol)
  {
    try
    {
      List<OwnedStock> ownedStocks = ownedStockDao.getAllOwnedStocks();
      for (OwnedStock ownedStock : ownedStocks)
      {
        if (ownedStock.getStockSymbol().equals(symbol))
        {
          ownedStockDao.deleteOwnedStock(ownedStock.getId());
        }
      }
      uow.commit();
      logger.log("INFO", "Owned stock from " + symbol + " was deleted");
    }
    catch (IllegalArgumentException e)
    {
      uow.rollback();
      logger.log("ERROR", e.getMessage());
      throw new DateUpdateException("Error in deleting owned stock");
    }
  }
}

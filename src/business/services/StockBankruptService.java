package business.services;

import domain.OwnedStock;
import domain.Portfolio;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class StockBankruptService implements PropertyChangeListener
{
  private final FileUnitOfWork uow;
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
      ArrayList<Integer> listOfBankruptOwnedStock = new ArrayList<>();
      for (OwnedStock ownedStock : ownedStocks)
      {
        if (ownedStock.getStockSymbol().equals(symbol))
        {
          listOfBankruptOwnedStock.add(ownedStock.getId());
          ownedStockDao.deleteOwnedStock(ownedStock.getId());
        }
      }
      deleteOwnedStockFromPortfolio(listOfBankruptOwnedStock);
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

  private void deleteOwnedStockFromPortfolio(ArrayList<Integer> listOfBankruptOwnedStock)
  {
    ArrayList<Portfolio> portfolios = new ArrayList<>(portfolioDao.getAllPortfolios());
    for (Portfolio portfolio : portfolios)
    {
      for (int idToRemove : listOfBankruptOwnedStock)
      {
        portfolio.removeOwnedStock(idToRemove);
      }
    }
  }
}

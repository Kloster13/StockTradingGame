package business.services;

import domain.OwnedStock;
import domain.Portfolio;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
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
      ArrayList<String> listOfBankruptOwnedStock = new ArrayList<>();
      for (OwnedStock ownedStock : ownedStocks)
      {
        if (ownedStock.getStockSymbol().equals(symbol))
        {
          listOfBankruptOwnedStock.add(symbol);
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

  private void deleteOwnedStockFromPortfolio(ArrayList<String> listOfBankruptOwnedStock)
  {
    ArrayList<Portfolio> portfolios = new ArrayList<>(portfolioDao.getAllPortfolios());
    for (Portfolio portfolio : portfolios)
    {
      for (int Remove : listOfBankruptOwnedStock)
      {
        portfolio.removeOwnedStock(symbolToRemove);
      }
    }
  }
}

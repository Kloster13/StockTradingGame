package business.services;

import business.services.dtos.PortfolioData;
import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import shared.logging.Logger;

import java.util.*;

public class PortfolioService
{
  private final Logger logger = Logger.getInstance();
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;
  private final StockDao stockDao;

  public PortfolioService(FileUnitOfWork uow)
  {
    this.ownedStockDao = new OwnedStockDaoFileImplementation(uow);
    this.portfolioDao = new PortfolioDaoFileImplementation(uow);
    this.stockDao = new StockDaoFileImplementation(uow);
  }

  public PortfolioData getPortfolioData(int portfolioId)
  {
    try
    {
      Portfolio portfolio = portfolioDao.getPortfolioById(portfolioId).orElseThrow();
      List<OwnedStock> ownedStocks = ownedStockDao.getAllOwnedStocks();

      Map<String, Integer> ownedStockInfo = new HashMap<>();
      double portfolioValue = 0;
      for (OwnedStock ownedStock : ownedStocks)
      {
        if (ownedStock.getPortfolioId() == portfolioId)
        {
          ownedStockInfo.put(ownedStock.getStockSymbol(), ownedStock.getNumberOfShares());
          portfolioValue += (stockDao.getStockBySymbol(ownedStock.getStockSymbol()).orElseThrow()
              .getCurrentPrice())*ownedStock.getNumberOfShares();
        }
      }
      Map<String,Double> stocksToBuy= new HashMap<>();
      for(Stock stock : stockDao.getAllStocks()){
        stocksToBuy.put(stock.getSymbol(),stock.getCurrentPrice());
      }

      return new PortfolioData(portfolio.getCurrentBalance(), portfolioValue, ownedStockInfo,stocksToBuy);
    }
    catch (NoSuchElementException e)
    {
      logger.log("ERROR", e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}

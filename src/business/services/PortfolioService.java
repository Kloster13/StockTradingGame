package business.services;

import business.services.dtos.OwnedStockDTO;
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

  public PortfolioService(OwnedStockDao ownedStockDao, PortfolioDao portfolioDao, StockDao stockDao)
  {
    this.ownedStockDao = ownedStockDao;
    this.portfolioDao = portfolioDao;
    this.stockDao = stockDao;
  }

  public PortfolioData getPortfolioData(int portfolioId)
  {
    try
    {
      logger.log("INFO", "Fetching portfolio data");
      Portfolio portfolio = portfolioDao.getPortfolioById(portfolioId).orElseThrow();
      List<OwnedStock> ownedStocks = ownedStockDao.getAllOwnedStocks();

      ArrayList<OwnedStockDTO> ownedStockInfo = new ArrayList<>();
      double portfolioValue = 0;
      for (OwnedStock ownedStock : ownedStocks)
      {
        if (ownedStock.getPortfolioId() == portfolioId)
        {
          Stock stock = stockDao.getStockBySymbol(ownedStock.getStockSymbol()).orElseThrow();
          ownedStockInfo.add(
              new OwnedStockDTO(ownedStock.getStockSymbol(), ownedStock.getNumberOfShares(),
                  stock.getCurrentPrice()));
          portfolioValue += (stock.getCurrentPrice()) * ownedStock.getNumberOfShares();
        }
      }

      return new PortfolioData(portfolio.getCurrentBalance(), portfolioValue, ownedStockInfo);
    }
    catch (NoSuchElementException e)
    {
      logger.log("ERROR", e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}

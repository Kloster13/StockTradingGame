package business.services;

import business.services.dtos.BuySellStockRequest;
import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import domain.Transaction;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import shared.logging.Logger;

import java.util.List;

public class StockTransactionService
{
  private final Logger logger = Logger.getInstance();
  private final UnitOfWork uow;
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;
  private final StockDao stockDao;
  private final TransactionDao transactionDao;

  public StockTransactionService(UnitOfWork uow, OwnedStockDao ownedStockDao,
      PortfolioDao portfolioDao, StockDao stockDao,
      TransactionDao transactionDao)
  {
    this.uow = uow;
    this.ownedStockDao = ownedStockDao;
    this.portfolioDao = portfolioDao;
    this.stockDao = stockDao;
    this.transactionDao=transactionDao;
  }

  public void buyStock(BuySellStockRequest request)
  {
    try
    {
      uow.begin();
      // Validation and transaction creation
      Stock stock = stockDao.getStockById(request.stockId()).orElseThrow();
      Portfolio portfolio = portfolioDao.getPortfolioById(request.portfolioID()).orElseThrow();
      if (stock.getCurrentState().equals("Bankrupt"))
        throw new IllegalArgumentException(stock.getSymbol() + " is bankrupt");
      if (request.quantity() < 1)
        throw new IllegalArgumentException("Quantity must be above 0");
      Transaction transaction = new Transaction(stock.getSymbol(), "buy", request.quantity(),
          stock.getCurrentPrice());
      if (portfolio.getCurrentBalance() < transaction.getTotalAmount())
        throw new IllegalArgumentException("Not enough money");

      // Owned stock update
      OwnedStock ownedStock = getOwnedStockFromData(stock, portfolio);
      if (ownedStock == null)
      {
        ownedStock = new OwnedStock(stock.getSymbol(), portfolio.getId(), request.quantity());
        ownedStockDao.createOwnedStock(ownedStock);
      }
      else
      {
        ownedStock.setNumberOfShares(ownedStock.getNumberOfShares() + request.quantity());
        ownedStockDao.updateOwnedStock(ownedStock);
      }
      // Transaction update
      transactionDao.createTransaction(transaction);
      portfolio.addTransactions(transaction.getId());

      // Balance update
      portfolio.updateCurrentBalance(transaction.getTotalAmount() * -1);
      portfolioDao.updatePortfolio(portfolio);

      uow.commit();
      logger.log("INFO", request.quantity() + " of " + stock.getName() + " was bought");
    }

    catch (Exception e) // TODO FIX
    {
      uow.rollback();
      logger.log("ERROR", "Could not buy stock" + e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public void sellStock(BuySellStockRequest request)
  {
    try
    {
      uow.begin();

      Stock stock = stockDao.getStockById(request.stockId()).orElseThrow();
      Portfolio portfolio = portfolioDao.getPortfolioById(request.portfolioID()).orElseThrow();
      OwnedStock ownedStock = getOwnedStockFromData(stock, portfolio);
      if (ownedStock == null)
      {
        throw new IllegalArgumentException(stock.getSymbol() + " not found for in owned stock");
      }
      if (request.quantity() < 1)
        throw new IllegalArgumentException("Quantity must be above 0");
      if (request.quantity() > ownedStock.getNumberOfShares())
        throw new IllegalArgumentException("Request exceed number of owned stock. Owned stocks: "
            + ownedStock.getNumberOfShares());

      if (ownedStock.getNumberOfShares() == request.quantity())
        ownedStockDao.deleteOwnedStock(ownedStock.getId());
      else
        ownedStock.setNumberOfShares(ownedStock.getNumberOfShares() - request.quantity());

      Transaction transaction = new Transaction(stock.getSymbol(), "sell", request.quantity(),
          stock.getCurrentPrice());
      transactionDao.createTransaction(transaction);
      portfolio.addTransactions(transaction.getId());

      portfolio.updateCurrentBalance(transaction.getTotalAmount());
      portfolioDao.updatePortfolio(portfolio);

      uow.commit();
      logger.log("INFO", request.quantity() + " of " + stock.getName() + " was sold");
    }
    catch (Exception e)
    {
      uow.rollback();
      logger.log("ERROR", "Could not sell stock" + e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private OwnedStock getOwnedStockFromData(Stock stock, Portfolio portfolio)
  {
    List<OwnedStock> ownedStocks = ownedStockDao.getAllOwnedStocks();
    OwnedStock ownedStock = null;
    for (OwnedStock loadedOwnedStock : ownedStocks)
    {
      if (loadedOwnedStock.getStockSymbol().equals(stock.getSymbol())
          && loadedOwnedStock.getPortfolioId() == portfolio.getId())
      {
        ownedStock = loadedOwnedStock;
        break;
      }
    }
    return ownedStock;
  }
}

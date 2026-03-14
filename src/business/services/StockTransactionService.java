package business.services;

import business.services.dtos.BuyStockRequest;
import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import domain.Transaction;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import shared.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class StockTransactionService
{
  private final Logger logger = Logger.getInstance();
  private final UnitOfWork uow;
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;
  private final StockDao stockDao;
  private final TransactionDao transactionDao;

  public StockTransactionService(UnitOfWork unitOfWork, FileUnitOfWork uow)
  {
    this.uow = unitOfWork;
    this.ownedStockDao = new OwnedStockDaoFileImplementation(uow);
    this.portfolioDao = new PortfolioDaoFileImplementation(uow);
    this.stockDao = new StockDaoFileImplementation(uow);
    this.transactionDao = new TransactionDaoFileImplementation(uow);
  }

  public void buyStock(BuyStockRequest request)
  {
    try
    {
      uow.begin();
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

      List<OwnedStock> ownedStock =  ownedStockDao.getAllOwnedStocks();




      uow.commit();
    }

    catch (Exception e) // TODO FIX
    {
      uow.rollback();
      logger.log("ERROR", "Shit that did not go well" + e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }

  }

}

package Testing;

import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import persistence.fileimplementation.*;
import persistence.interfaces.*;

public class StockTransactionTest
{
  public static void main(String[] args)
  {
    StockTransactionService transaction = getStockTransactionService();
    BuySellStockRequest buyStockRequest = new BuySellStockRequest(1, 2,
        20);// NVIDIA
    BuySellStockRequest buyStockRequest1 = new BuySellStockRequest(1, 1,
        20); // GOOGLE
    transaction.buyStock(buyStockRequest);
    transaction.buyStock(buyStockRequest1);

    BuySellStockRequest sellStockRequest = new BuySellStockRequest(1, 2,
        10);// NVIDIA
    transaction.sellStock(sellStockRequest);

  }

  public static StockTransactionService getStockTransactionService()
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(tester);
    PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
    StockDao stockDao = new StockDaoFileImplementation(tester);
    StockPriceHistoryDao historyDao = new StockPriceHistoryDaoFileImplementation(
        tester);
    TransactionDao transactionDao = new TransactionDaoFileImplementation(
        tester);
    StockTransactionService transaction = new StockTransactionService(tester,
        ownedStockDao, portfolioDao, stockDao, transactionDao);
    return transaction;
  }
}

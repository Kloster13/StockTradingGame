import domain.*;
import persistence.FileAccessException;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import shared.logging.Logger;

public class Testing
{
  public static void main(String[] args)
  {
    // Slet filer før test køres////
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");

    // stock testing
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest = new Stock("MET", "Meta", 1000);
      stockDao.createStock(stockTest);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest2 = new Stock("MET2", "Meta", 1000);
      stockDao.createStock(stockTest2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock updatedTest = new Stock("MET1", "Meta", 2000);
      stockDao.updateStock(updatedTest, 1);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    // portfolio testing
    try
    {
      PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
      Portfolio portfolio = new Portfolio(1000);
      portfolioDao.createPortfolio(portfolio);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
      Portfolio portfolio2 = new Portfolio(2000);
      Portfolio portfolio3 = new Portfolio(10000);
      portfolioDao.createPortfolio(portfolio3);
      portfolioDao.deletePortfolio(2);
      portfolioDao.createPortfolio(portfolio2); // Denne får samme id som den der bliver sletter inden
      portfolio2.setCurrentBalance(50);
      portfolioDao.updatePortfolio(portfolio2,2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    // owned stock testing
    try
    {
      OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(tester);
      OwnedStock ownedStock = new OwnedStock("MET",200);
      ownedStockDao.createOwnedStock(ownedStock);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      OwnedStockDao OwnedStockDao = new OwnedStockDaoFileImplementation(tester);
      OwnedStock OwnedStock2 = new OwnedStock("GOG",10);
      OwnedStock OwnedStock3 = new OwnedStock("TES",1);
      OwnedStockDao.createOwnedStock(OwnedStock3);
      OwnedStockDao.deleteOwnedStock(2);
      OwnedStockDao.createOwnedStock(OwnedStock2); // Denne får samme id som den der bliver sletter inden
      OwnedStockDao.updateOwnedStock(OwnedStock3,2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    // transaction testing
    try
    {
      TransactionDao TransactionDao = new TransactionDaoFileImplementation(tester);
      Transaction Transaction = new Transaction("MET","buy",10,100,5);
      TransactionDao.createTransaction(Transaction);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      TransactionDao TransactionDao = new TransactionDaoFileImplementation(tester);
      Transaction Transaction2 = new Transaction("GOG","sell",100,20,10);
      Transaction Transaction3 = new Transaction("GOG","sell",1000,20,10);
      TransactionDao.createTransaction(Transaction3);
      TransactionDao.deleteTransaction(2);
      TransactionDao.createTransaction(Transaction2); // Denne får samme id som den der bliver sletter inden
      TransactionDao.updateTransaction(Transaction3,2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    try
    {
      StockPriceHistoryDao StockPriceHistoryDao = new StockPriceHistoryDaoFileImplementation(tester);
      StockPriceHistory StockPriceHistory = new StockPriceHistory("MET",10);
      StockPriceHistoryDao.createStockPriceHistory(StockPriceHistory);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      StockPriceHistoryDao StockPriceHistoryDao = new StockPriceHistoryDaoFileImplementation(tester);
      StockPriceHistory StockPriceHistory2 = new StockPriceHistory("GOG",10);
      StockPriceHistory StockPriceHistory3 = new StockPriceHistory("GOG",20);
      StockPriceHistoryDao.createStockPriceHistory(StockPriceHistory3);
      StockPriceHistoryDao.deleteStockPriceHistory(2);
      StockPriceHistoryDao.createStockPriceHistory(StockPriceHistory2); // Denne får samme id som den der bliver sletter inden
      StockPriceHistoryDao.updateStockPriceHistory(StockPriceHistory3,2);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    tester.commit();
  }
}

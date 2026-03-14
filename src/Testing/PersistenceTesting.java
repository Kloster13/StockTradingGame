package Testing;

import domain.*;
import persistence.FileAccessException;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import shared.logging.Logger;

public class PersistenceTesting
{
  public static void main(String[] args)
  {
    // Slet filer før test køres////
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");

    // stock testing
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest = new Stock("GOOG", "Google", 100);
      stockDao.createStock(stockTest);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest2 = new Stock("META", "Meta", 100);
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
      stockDao.updateStock(updatedTest);
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
    // owned stock testing
    try
    {
      OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(tester);
      OwnedStock ownedStock = new OwnedStock("META",1, 20);
      OwnedStock ownedStock2 = new OwnedStock("GOOG",1, 10);
      ownedStockDao.createOwnedStock(ownedStock);
      ownedStock2.setId(2);
      ownedStockDao.createOwnedStock(ownedStock2);

    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
//    try
//    {
//      OwnedStockDao OwnedStockDao = new OwnedStockDaoFileImplementation(tester);
//      OwnedStock OwnedStock2 = new OwnedStock("GOOG",1, 10);
//      OwnedStock OwnedStock3 = new OwnedStock("META",1, 1);
//      OwnedStockDao.createOwnedStock(OwnedStock3);
//      OwnedStockDao.deleteOwnedStock(2);
//      OwnedStockDao.createOwnedStock(
//          OwnedStock2); // Denne får samme id som den der bliver sletter inden
//      OwnedStockDao.updateOwnedStock(OwnedStock3);
//    }
//    catch (FileAccessException | IllegalArgumentException e)
//    {
//      Logger.getInstance().log("ERROR", e.getMessage());
//    }

    // transaction testing
    try
    {
      TransactionDao TransactionDao = new TransactionDaoFileImplementation(tester);
      Transaction Transaction = new Transaction("META", "buy", 10, 100);
      TransactionDao.createTransaction(Transaction);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      TransactionDao TransactionDao = new TransactionDaoFileImplementation(tester);
      Transaction Transaction2 = new Transaction("GOOG", "sell", 100, 20);
      Transaction Transaction3 = new Transaction("GOOG", "sell", 1000, 20);
      TransactionDao.createTransaction(Transaction3);
      TransactionDao.deleteTransaction(2);
      TransactionDao.createTransaction(
          Transaction2); // Denne får samme id som den der bliver sletter inden
      Transaction3.setId(Transaction2.getId());
      TransactionDao.updateTransaction(Transaction3);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    try
    {
      StockPriceHistoryDao StockPriceHistoryDao = new StockPriceHistoryDaoFileImplementation(
          tester);
      StockPriceHistory StockPriceHistory = new StockPriceHistory("MET", 10);
      StockPriceHistoryDao.createStockPriceHistory(StockPriceHistory);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
    try
    {
      StockPriceHistoryDao StockPriceHistoryDao = new StockPriceHistoryDaoFileImplementation(
          tester);
      StockPriceHistory StockPriceHistory2 = new StockPriceHistory("GOOG", 10);
      StockPriceHistory StockPriceHistory3 = new StockPriceHistory("GOOG", 20);
      StockPriceHistoryDao.createStockPriceHistory(StockPriceHistory3);
      StockPriceHistoryDao.deleteStockPriceHistory(2);
      StockPriceHistoryDao.createStockPriceHistory(
          StockPriceHistory2); // Denne får samme id som den der bliver sletter inden
      StockPriceHistoryDao.updateStockPriceHistory(StockPriceHistory3);
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    tester.commit();
  }
}

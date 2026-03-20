package buystocktests;

import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import domain.Portfolio;
import domain.Stock;
import domain.Transaction;
import mocks.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import persistence.interfaces.*;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class BuyStockTest
{
  MockUnitOfWork uow;
  StockDao stockDao;
  OwnedStockDao ownedStockDao;
  PortfolioDao portfolioDao;
  TransactionDao transactionDao;
  StockPriceHistoryDao stockPriceHistoryDao;
  StockTransactionService transactionService;
  Logger logger;

  @BeforeAll void setupLogger()
  {
    logger = Logger.getInstance();
    logger.setOutput(new MockLogOutput());
  }

  @BeforeEach void setupMocks()
  {
    uow = new MockUnitOfWork();
    stockDao = new StockDaoMock(uow);
    ownedStockDao = new OwnedStockDAOMock(uow);
    portfolioDao = new PortfolioDaoMock(uow);
    transactionDao = new TransactionDaoMock(uow);
    stockPriceHistoryDao = new StockPriceHistoryDaoMock(uow);
    transactionService = new StockTransactionService(uow, ownedStockDao,
        portfolioDao, stockDao, transactionDao);
    Portfolio portfolio = new Portfolio(1, 10000, new ArrayList<>());
    portfolioDao.createPortfolio(portfolio);
  }

  @Test void buyStock_whenSingleStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 1);
    transactionService.buyStock(request);
    assertEquals(1,
        ownedStockDao.getOwnedStockById(1).get().getNumberOfShares());
  }

  @Test void buyStock_whenQuantityIs0_throwInvalidInput()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 0);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void transactionHistoryCreation_whenBuyingStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 1);
    transactionService.buyStock(request);
    Transaction transaction = transactionDao.getTransactionById(1).get();

    assertEquals(105, transaction.getTotalAmount());
  }

  @Test void updateOwnedStock_whenOwningStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 1);
    transactionService.buyStock(request);
    transactionService.buyStock(request);
    assertEquals(1, ownedStockDao.getAllOwnedStocks().size());
  }

  @Test void buyStock_whenLargeAmountOfStocks_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 99);
    transactionService.buyStock(request);
    assertEquals(99,
        ownedStockDao.getOwnedStockById(1).get().getNumberOfShares());
  }

  @Test void buyStock_whenPriceEqualsTotal_returnTrue()
  {
    createStock();
    portfolioDao.getPortfolioById(1).get().setCurrentBalance(
        10000 + AppConfiguration.getAppConfiguration().getTransactionFee());
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 100);
    transactionService.buyStock(request);
    assertEquals(0, portfolioDao.getPortfolioById(1).get().getCurrentBalance());
  }

  @Test void buyStock_whenPriceIsJustAbove_throwInvalidArgument()
  {
    createStock();
    portfolioDao.getPortfolioById(1).get().setCurrentBalance(
        10000 -0.1);
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 100);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenQuantityIsNegative_throwInvalidArgument()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, 1, -1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenStockIsBankrupt_tthrowInvalidArgument()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stock.setCurrentState("Bankrupt");
    stockDao.createStock(stock);
    BuySellStockRequest request = new BuySellStockRequest(1, 1, 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenNotEnoughMoney_throwInvalidArgument(){
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1,1,100);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenSymbolIsNull_throwNoSuchElement(){
    createStock();
    assertThrows(NoSuchElementException.class, () -> {
      Stock stock =  stockDao.getStockBySymbol("").get();
    });
  }


  private void createStock()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stockDao.createStock(stock);
  }

}

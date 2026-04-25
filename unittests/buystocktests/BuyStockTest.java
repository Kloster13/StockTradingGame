package buystocktests;

import business.fee.FeeStrategy;
import business.fee.FlatFee;
import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import domain.Portfolio;
import domain.Stock;
import mocks.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import persistence.interfaces.*;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.time.LocalDate;
import java.util.ArrayList;

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
  FeeStrategy feeStrategy = new FlatFee();

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
        portfolioDao, stockDao, transactionDao, feeStrategy);
    Portfolio portfolio = new Portfolio(1,"TestPortfolio", 10000, new ArrayList<>());
    portfolioDao.createPortfolio(portfolio);
  }

  @Test void buyStock_whenSingleStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    assertEquals(1,
        ownedStockDao.getOwnedStockById(1).get().getNumberOfShares());
  }

  @Test void buyStock_whenQuantityIs0_throwInvalidInput()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 0);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void updateOwnedStock_whenOwningStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    transactionService.buyStock(request);
    assertEquals(1, ownedStockDao.getAllOwnedStocks().size());
  }

  @Test void buyStock_whenLargeAmountOfStocks_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 99);
    transactionService.buyStock(request);
    assertEquals(99,
        ownedStockDao.getOwnedStockById(1).get().getNumberOfShares());
  }

  @Test void buyStock_whenPriceEqualsTotal_returnTrue()
  {
    createStock();
    portfolioDao.getPortfolioById(1).get().setCurrentBalance(
        10000 + AppConfiguration.getAppConfiguration().getTransactionFee());
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 100);
    transactionService.buyStock(request);
    assertEquals(0, portfolioDao.getPortfolioById(1).get().getCurrentBalance());
  }

  @Test void buyStock_whenPriceIsJustAbove_throwInvalidArgument()
  {
    createStock();
    portfolioDao.getPortfolioById(1).get().setCurrentBalance(10000 - 0.1);
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenQuantityIsNegative_throwInvalidArgument()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", -1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenStockIsBankrupt_throwInvalidArgument()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stock.setCurrentState("Bankrupt");
    stockDao.createStock(stock);
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenNotEnoughMoney_throwInvalidArgument()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenSymbolIsEmpty_throwIllegalArgument()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "", 1);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void buyStock_whenSymbolIsNull_throwIllegalArgument()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, null, 1);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.buyStock(request);
    });
  }

  @Test void portfolioUpdated_whenBuyStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    double balance = portfolioDao.getPortfolioById(1).get().getCurrentBalance();
    assertEquals(10000 - 100 - AppConfiguration.getAppConfiguration()
        .getTransactionFee(), balance);
  }

  @Test void transactionType_whenBuyingStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    String type = transactionDao.getTransactionById(1).get().getType();
    assertEquals("buy", type);
  }

  @Test void transactionAmount_whenBuyingStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    double totalAmount = transactionDao.getTransactionById(1).get()
        .getTotalAmount();
    assertEquals(105, totalAmount);
  }

  @Test void correctDateFormatInTransaction_whenBuyingStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    LocalDate date = transactionDao.getTransactionById(1).get().getTimestamp();
    assertEquals(LocalDate.now(), date);
  }

  @Test void useAppConfigForFee_whenBuyingStock_returnTrue()
  {
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    AppConfiguration.getAppConfiguration().setTransactionFee(20);
    transactionService.buyStock(request);
    assertTrue(transactionDao.getTransactionById(1).get().getTotalAmount()
        != transactionDao.getTransactionById(2).get().getTotalAmount());
    AppConfiguration.getAppConfiguration().setTransactionFee(5);
  }

  @Test void negativeFee_WhenBuyingStock_returnTrue(){
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1, "GOOG", 1);
    transactionService.buyStock(request);
    AppConfiguration.getAppConfiguration().setTransactionFee(100);
    transactionService.buyStock(request);
    assertTrue(transactionDao.getTransactionById(1).get().getTotalAmount()
        < transactionDao.getTransactionById(2).get().getTotalAmount());
    AppConfiguration.getAppConfiguration().setTransactionFee(5);
  }

  @Test void commitOnlyOnce_WhenBuyingStock_returnTrue(){
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1,"GOOG",1);
    transactionService.buyStock(request);
    assertEquals(1,uow.commitCounter);
  }

  @Test void beginOnlyOnce_WhenBuyingStock_returnTrue(){
    createStock();
    BuySellStockRequest request = new BuySellStockRequest(1,"GOOG",1);
    transactionService.buyStock(request);
    assertEquals(1,uow.beginCounter);
  }

  private void createStock()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stockDao.createStock(stock);
  }
}

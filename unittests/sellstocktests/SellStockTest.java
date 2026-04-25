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
import shared.logging.Logger;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class SellStockTest
{

  MockUnitOfWork uow;
  StockDao stockDao;
  OwnedStockDao ownedStockDao;
  PortfolioDao portfolioDao;
  TransactionDao transactionDao;
  StockPriceHistoryDao stockPriceHistoryDao;
  StockTransactionService transactionService;
  Logger logger;

  FeeStrategy feeStrategy  =new FlatFee();

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

    Portfolio portfolio = new Portfolio(1,"TestPorfolio", 10000, new ArrayList<>());
    portfolioDao.createPortfolio(portfolio);
  }

  private void createStock()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stockDao.createStock(stock);
  }

  private void seedOwned(String symbol, int qty)
  {
    transactionService.buyStock(new BuySellStockRequest(1, symbol, qty));
  }

  @Test void sellStock_whenSellingSingleShare_decreasesOwnedShares()
  {
    createStock();
    seedOwned("GOOG", 2);
    transactionService.sellStock(
        new BuySellStockRequest(1, "GOOG", 1));
    assertEquals(1,
        ownedStockDao.getOwnedStockById(1).get().getNumberOfShares());
  }

  @Test void sellStock_whenSellingAllShares_removesOwnedStock()
  {
    createStock();
    seedOwned("GOOG", 2);
    transactionService.sellStock(
        new BuySellStockRequest(1, "GOOG", 2));

    assertEquals(0, ownedStockDao.getAllOwnedStocks().size());
  }

  @Test void sellStock_whenQuantityIs0_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 0));
    });
  }

  @Test void sellStock_whenQuantityIsNegative_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, "GOOG", -1));
    });
  }

  @Test void sellStock_whenSellingMoreThanOwned_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 2);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 3));
    });
  }

  @Test void sellStock_whenSymbolIsNotOwned_throwInvalidArgument()
  {
    createStock();
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    });
  }

  @Test void sellStock_whenSymbolIsEmpty_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, "", 1));
    });
  }

  @Test void sellStock_whenSymbolIsNull_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 1);

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(1, null, 1));
    });
  }

  @Test void sellStock_whenPortfolioNotFound_throwInvalidArgument()
  {
    createStock();
    seedOwned("GOOG", 1);
    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.sellStock(new BuySellStockRequest(999, "GOOG", 1));
    });
  }

  @Test void portfolioUpdated_whenSellStock_increasesBalanceByTransactionTotal()
  {
    createStock();
    seedOwned("GOOG", 1);

    double before = portfolioDao.getPortfolioById(1).get().getCurrentBalance();
    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    double after = portfolioDao.getPortfolioById(1).get().getCurrentBalance();

    double totalAmount = transactionDao.getTransactionById(2).get()
        .getTotalAmount();
    assertEquals(before + totalAmount, after);
  }

  @Test void transactionType_whenSellingStock_isSell()
  {
    createStock();
    seedOwned("GOOG", 1);

    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    String type = transactionDao.getTransactionById(2).get().getType();
    assertEquals("sell", type);
  }

  @Test void transactionAmount_whenSellingSingleShare_returnTrue()
  {
    createStock();
    seedOwned("GOOG", 1);

    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    double totalAmount = transactionDao.getTransactionById(2).get()
        .getTotalAmount();
    assertEquals(95, totalAmount);
  }

  @Test void correctDateFormatInTransaction_whenSellingStock_returnTrue()
  {
    createStock();
    seedOwned("GOOG", 1);

    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    LocalDate date = transactionDao.getTransactionById(2).get().getTimestamp();
    assertEquals(LocalDate.now(), date);
  }

  @Test void commitOnlyOnce_WhenSellingStock_returnTrue()
  {
    createStock();
    seedOwned("GOOG", 1);

    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    assertEquals(2, uow.commitCounter); // commits on creation
  }

  @Test void beginOnlyOnce_WhenSellingStock_returnTrue()
  {
    createStock();
    seedOwned("GOOG", 1);

    transactionService.sellStock(new BuySellStockRequest(1, "GOOG", 1));
    assertEquals(2, uow.beginCounter);
  }
}
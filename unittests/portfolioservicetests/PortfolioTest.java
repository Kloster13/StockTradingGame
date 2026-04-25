package portfolioservicetests;

import business.fee.FeeStrategy;
import business.fee.FlatFee;
import business.services.PortfolioService;
import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import business.services.dtos.OwnedStockDTO;
import business.services.dtos.PortfolioData;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class PortfolioTest
{
  MockUnitOfWork uow;
  StockDao stockDao;
  OwnedStockDao ownedStockDao;
  PortfolioDao portfolioDao;
  TransactionDao transactionDao;
  StockPriceHistoryDao stockPriceHistoryDao;
  StockTransactionService transactionService;
  PortfolioService portfolioService;
  Logger logger;
  double transactionFee;
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
    transactionService = new StockTransactionService(uow, ownedStockDao, portfolioDao, stockDao,
        transactionDao, feeStrategy);
    portfolioService = new PortfolioService(ownedStockDao, portfolioDao, stockDao);
    transactionFee = AppConfiguration.getAppConfiguration().getTransactionFee();
  }

  private void createAndBuyStock()
  {
    Stock stock = new Stock("GOOG", "Google", 100);
    stockDao.createStock(stock);
    Portfolio portfolio = new Portfolio(1,"TestPortfolio", 10000, new ArrayList<>());
    portfolioDao.createPortfolio(portfolio);
    transactionService.buyStock(new BuySellStockRequest(1, "GOOG", 1));
  }

  @Test void getBalance_whenPortfolioService_returnTrue()
  {
    createAndBuyStock();
    double balance = portfolioService.getPortfolioData(1).currentBalance();
    assertEquals(10000 - 100 - transactionFee, balance);
  }

  @Test void getValue_whenPortfolioService_returnTrue()
  {
    createAndBuyStock();
    double value = portfolioService.getPortfolioData(1).portfolioValue();
    assertEquals(100, value);
  }

  @Test void getPortfolioData_whenPortfolioService_returnTrue()
  {
    createAndBuyStock();
    OwnedStockDTO toCompare= new OwnedStockDTO("GOOG",1,100);
    OwnedStockDTO data = portfolioService.getPortfolioData(1).ownedStockInfo().getFirst();
    assertEquals(toCompare, data);
  }
}

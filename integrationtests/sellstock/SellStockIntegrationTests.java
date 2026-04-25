package sellstock;

import business.fee.FeeStrategy;
import business.fee.FlatFee;
import business.services.GameService;
import business.services.PortfolioService;
import business.services.StockTransactionService;
import domain.Stock;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mocks.MockLogOutput;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import presentation.core.ActivePortfolioCache;
import presentation.viewmodels.StockMarketViewModel;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class SellStockIntegrationTests
{
  Logger logger;
  private String testDirPath;
  AppConfiguration config = AppConfiguration.getAppConfiguration();
  Stock testStock;

  StockMarketViewModel viewModel;
  ActivePortfolioCache cache = new ActivePortfolioCache();

  StringProperty balance;
  StringProperty sellStockSymbol;
  IntegerProperty sellStockAmount;
  StringProperty sellStatusLabel;

  FeeStrategy feeStrategy = new FlatFee();

  @BeforeAll static void initToolkit()
  {
    try {
      Platform.startup(() -> {});
    } catch (IllegalStateException e) {
    }
  }

  @BeforeAll void setupLogger()
  {
    logger = Logger.getInstance();
    logger.setOutput(new MockLogOutput());
  }

  @BeforeEach public void setup()
  {
    testDirPath = "testdata/test-" + UUID.randomUUID() + "/";
    new File(testDirPath).mkdirs();

    UnitOfWork unitOfWork = new FileUnitOfWork(testDirPath);
    StockDao stockDao = new StockDaoFileImplementation((FileUnitOfWork) unitOfWork);
    OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation((FileUnitOfWork) unitOfWork);
    PortfolioDao portfolioDao = new PortfolioDaoFileImplementation((FileUnitOfWork) unitOfWork);
    StockPriceHistoryDao historyDao = new StockPriceHistoryDaoFileImplementation((FileUnitOfWork) unitOfWork);

    GameService gameService = new GameService(unitOfWork, ownedStockDao, stockDao, historyDao, portfolioDao);
    gameService.resetGame();
    testStock = stockDao.getStockBySymbol("TSLA").get();
    cache.setPortfolioId(portfolioDao.getAllPortfolios().getFirst().getId());

    viewModel = createStockViewModel();

    balance = new SimpleStringProperty("");
    sellStockSymbol = new SimpleStringProperty("");
    sellStockAmount = new SimpleIntegerProperty(0);
    sellStatusLabel = new SimpleStringProperty("");

    // Binding
    balance.bind(viewModel.balanceProperty());
    sellStatusLabel.bind(viewModel.sellStatusProperty());
    sellStockAmount.bindBidirectional(viewModel.sellAmountProperty());
    sellStockSymbol.bindBidirectional(viewModel.sellStockSymbolProperty());
  }

  @AfterEach public void cleanup()
  {
    File dir = new File(testDirPath);
    for (File file : dir.listFiles())
      if (!file.isDirectory())
        file.delete();
    dir.delete();
  }

  @Nested class GivenNoInput
  {
    @BeforeEach public void setupProperties()
    {
      viewModel.sellStock();
    }

    @Test void statusUpdated()
    {
      assertEquals("Can't find stock", sellStatusLabel.get());
    }

    @Test void noUpdateToBalance()
    {
      assertEquals(String.valueOf(config.getStartingBalance()), viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsNotStored()
    {
      assertEquals(0, new FileUnitOfWork(testDirPath).getOwnedStocks().size());
    }
  }

  @Nested class GivenStockNotOwned
  {
    @BeforeEach public void setupProperties()
    {
      sellStockSymbol.setValue("TSLA");
      sellStockAmount.setValue(1);
      viewModel.sellStock();
    }

    @Test void statusUpdated()
    {
      assertEquals("TSLA not found in owned stock", sellStatusLabel.get());
    }

    @Test void noUpdateToBalance()
    {
      assertEquals(String.valueOf(config.getStartingBalance()), viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsNotStored()
    {
      assertEquals(0, new FileUnitOfWork(testDirPath).getOwnedStocks().size());
    }
  }

  @Nested class GivenSellingMoreThanOwned
  {
    @BeforeEach public void setupProperties()
    {
      viewModel.stockSymbolProperty().set("TSLA");
      viewModel.buyAmountProperty().set(1);
      viewModel.buyStock();

      sellStockSymbol.setValue("TSLA");
      sellStockAmount.setValue(2);
      viewModel.sellStock();
    }

    @Test void statusUpdated()
    {
      assertEquals("Request exceed number of owned stock. Owned stocks: 1", sellStatusLabel.get());
    }

    @Test void noUpdateToBalance()
    {
      assertEquals(String.valueOf(config.getStartingBalance() - testStock.getCurrentPrice() - config.getTransactionFee()),
          viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsUnchanged()
    {
      assertEquals(1, new FileUnitOfWork(testDirPath).getOwnedStocks().size());
    }
  }

  @Nested class GivenValidInput
  {
    @BeforeEach public void setupProperties()
    {
      viewModel.stockSymbolProperty().set("TSLA");
      viewModel.buyAmountProperty().set(1);
      viewModel.buyStock();

      sellStockSymbol.setValue("TSLA");
      sellStockAmount.setValue(1);
      viewModel.sellStock();
    }

    @Test void statusIsUpdated()
    {
      assertEquals("Transaction completed", sellStatusLabel.get());
    }

    @Test void balanceIsUpdated()
    {
      String expectedBalance = String.valueOf(config.getStartingBalance() - 2 * config.getTransactionFee());
      assertEquals(expectedBalance, viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsRemovedFromStorage()
    {
      assertEquals(0, new FileUnitOfWork(testDirPath).getOwnedStocks().size());
    }
  }

  private StockMarketViewModel createStockViewModel()
  {
    UnitOfWork unitOfWork = new FileUnitOfWork(testDirPath);
    StockDao stockDao = new StockDaoFileImplementation((FileUnitOfWork) unitOfWork);
    OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation((FileUnitOfWork) unitOfWork);
    PortfolioDao portfolioDao = new PortfolioDaoFileImplementation((FileUnitOfWork) unitOfWork);
    StockPriceHistoryDao historyDao = new StockPriceHistoryDaoFileImplementation((FileUnitOfWork) unitOfWork);
    TransactionDao transactionDao = new TransactionDaoFileImplementation((FileUnitOfWork) unitOfWork);

    GameService gameService = new GameService(unitOfWork, ownedStockDao, stockDao, historyDao, portfolioDao);
    StockTransactionService transactionService = new StockTransactionService(unitOfWork, ownedStockDao,
        portfolioDao, stockDao, transactionDao, feeStrategy);
    PortfolioService portfolioService = new PortfolioService(ownedStockDao, portfolioDao, stockDao);
    return new StockMarketViewModel(gameService, transactionService, portfolioService, cache);
  }

}

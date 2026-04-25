package buystock;

import business.fee.FeeStrategy;
import business.fee.FlatFee;
import business.services.GameService;
import business.services.PortfolioService;
import business.services.StockTransactionService;
import domain.Stock;
import javafx.application.Platform;
import javafx.beans.property.*;
import mocks.MockLogOutput;
import org.junit.jupiter.api.*;
import persistence.fileimplementation.*;
import persistence.interfaces.*;
import presentation.core.ActivePortfolioCache;
import presentation.viewmodels.StockMarketViewModel;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class BuyStockIntegrationTests
{
  Logger logger;
  private String testDirPath;
  AppConfiguration config = AppConfiguration.getAppConfiguration();
  Stock testStock;

  StockMarketViewModel viewModel;
  ActivePortfolioCache cache = new ActivePortfolioCache();

  StringProperty balance;
  StringProperty buyStockSymbol;
  IntegerProperty buyStockAmount;
  StringProperty buyStatusLabel;

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
    buyStockSymbol = new SimpleStringProperty("");
    buyStockAmount = new SimpleIntegerProperty(0);
    buyStatusLabel = new SimpleStringProperty("");

    // Binding
    balance.bind(viewModel.balanceProperty());
    buyStatusLabel.bind(viewModel.buyStatusProperty());
    buyStockAmount.bindBidirectional(viewModel.buyAmountProperty());
    buyStockSymbol.bindBidirectional(viewModel.stockSymbolProperty());
  }

  @AfterEach public void cleanup()
  {
    File dir = new File(testDirPath);
    for (File file : dir.listFiles())
      if (!file.isDirectory())
        file.delete();
    dir.delete();
  }

  @Nested class GivenValidInput
  {
    @BeforeEach public void setupProperties()
    {
      buyStockSymbol.setValue("TSLA");
      buyStockAmount.setValue(1);
      viewModel.buyStock();
    }

    @Test void balanceIsUpdated()
    {
      String expectedBalance = String.valueOf(
          config.getStartingBalance() - testStock.getCurrentPrice() - config.getTransactionFee());

      assertEquals(expectedBalance, viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsCreatedAndStored()
    {
      assertEquals("TSLA", new FileUnitOfWork(testDirPath).getOwnedStocks().getFirst().getStockSymbol());
    }

    @Test void statusUpdateShown()
    {
      assertEquals("Transaction completed", buyStatusLabel.get());
    }

    @Test void transactionHistorySaved()
    {
      assertEquals(1, new FileUnitOfWork(testDirPath).getTransactions().size());
    }
  }

  @Nested class GivenNoInput
  {
    @BeforeEach public void setupProperties()
    {
      viewModel.buyStock();
    }

    @Test void statusUpdated()
    {
      viewModel.buyStock();
      assertEquals("No stock selected", buyStatusLabel.get());
    }

    @Test void noUpdateToBalance()
    {
      String expectedBalance = String.valueOf(config.getStartingBalance());

      assertEquals(expectedBalance, viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsNotStored()
    {
      assertEquals(0, new FileUnitOfWork(testDirPath).getOwnedStocks().size());
    }
  }

  @Nested class GivenNotEnoughMoney
  {
    @BeforeEach public void setupProperties()
    {
      buyStockSymbol.setValue("TSLA");
      buyStockAmount.setValue(10000);
      viewModel.buyStock();
    }

    @Test void statusUpdated()
    {
      assertEquals("Not enough money", viewModel.buyStatusProperty().get());
    }
    @Test void noUpdateToBalance()
    {
      String expectedBalance = String.valueOf(config.getStartingBalance());

      assertEquals(expectedBalance, viewModel.balanceProperty().get());
    }

    @Test void ownedStockIsNotStored()
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
    return new StockMarketViewModel(gameService, transactionService, portfolioService,cache);
  }
}
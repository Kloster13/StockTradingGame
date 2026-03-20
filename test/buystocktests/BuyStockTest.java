package buystocktests;

import mocks.*;
import org.junit.jupiter.api.Test;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.StockDao;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class BuyStockTest
{
  MockUnitOfWork test = new MockUnitOfWork();
  StockDao stockDao = new StockDaoMock(test);
  OwnedStockDao ownedStockDao = new OwnedStockDAOMock(test);
  PortfolioDao portfolioDao = new PortfolioDaoMock(test);
  Logger logger = Logger.getInstance();

  @Test void test()
  {
    logger.setOutput(new MockLogOutput());
    logger.log("asd","sad");
    assertEquals(1, 1);
  }

}

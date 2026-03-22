package Testing;

import business.services.GameService;
import persistence.fileimplementation.*;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.StockDao;
import persistence.interfaces.StockPriceHistoryDao;

public class TestGameService
{
  public static void main(String[] args) throws InterruptedException
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(tester);
    PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
    StockDao stockDao = new StockDaoFileImplementation(tester);
    StockPriceHistoryDao historyDao = new StockPriceHistoryDaoFileImplementation(
        tester);
    GameService gameService = new GameService(tester, ownedStockDao, stockDao, historyDao);

    gameService.startGame();
    Thread.sleep(100);
    System.out.println("jasdf");
    gameService.stopGame();
  }
}

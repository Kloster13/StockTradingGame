package Testing;

import business.services.PortfolioService;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.StockDao;

public class PortfolioDataTest
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    OwnedStockDao ownedStockDao = new OwnedStockDaoFileImplementation(tester);
    PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
    StockDao stockDao = new StockDaoFileImplementation(tester);
    PortfolioService portfolioService = new PortfolioService(tester,ownedStockDao,portfolioDao,stockDao);
    System.out.println(portfolioService.getPortfolioData(1));

  }
}

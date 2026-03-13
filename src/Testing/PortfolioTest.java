package Testing;

import domain.Portfolio;
import persistence.FileAccessException;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.interfaces.PortfolioDao;
import shared.logging.Logger;

import java.util.Optional;

public class PortfolioTest
{
  public static void main(String[] args)
  {
    // portfolio testing
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    try
    {
      tester.begin();
      PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
      Portfolio portfolio = new Portfolio(1000);
      portfolio.addTransactions(1);
      portfolio.addOwnedStock(2);
      portfolio.addOwnedStock(3);
      System.out.println(portfolio);
      portfolioDao.createPortfolio(portfolio);
      tester.commit();
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      tester.rollback();
      Logger.getInstance().log("ERROR", e.getMessage());
    }

    try
    {
      PortfolioDao portfolioDao = new PortfolioDaoFileImplementation(tester);
      Optional<Portfolio> portfolio = portfolioDao.getPortfolioById(1);
      portfolio.get().addTransactions(2);
      portfolio.get().addOwnedStock(4);
      portfolio.get().removeOwnedStock(2);
      System.out.println(portfolio);
      portfolioDao.updatePortfolio(portfolio.orElse(null));
      tester.commit();
    }
    catch (FileAccessException | IllegalArgumentException e)
    {
      tester.rollback();
      Logger.getInstance().log("ERROR", e.getMessage());
    }
  }
}

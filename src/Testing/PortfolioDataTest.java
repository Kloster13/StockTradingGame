package Testing;

import business.services.PortfolioService;
import persistence.fileimplementation.FileUnitOfWork;

public class PortfolioDataTest
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    PortfolioService portfolioService = new PortfolioService(tester);
    System.out.println(portfolioService.getPortfolioData(1));

  }
}

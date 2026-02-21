import domain.Stock;
import persistence.FileAccessException;
import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.StockDao;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

public class Testing
{
  public static void main(String[] args)
  {
    try
    {
      FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest = new Stock("MET", "Meta", 1000);
      stockDao.createStock(stockTest);
      tester.commit();
    }
    catch (FileAccessException |IllegalArgumentException e )
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    };

    try
    {
      FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock stockTest2 = new Stock("MET2", "Meta", 1000);
      stockDao.createStock(stockTest2);
      tester.commit();
    }
    catch (FileAccessException |IllegalArgumentException e )
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    };
    try
    {
      FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
      StockDao stockDao = new StockDaoFileImplementation(tester);
      Stock updatedTest = new Stock("MET1", "Meta", 2000);
      stockDao.updateStock(updatedTest,1);
      tester.commit();
    }
    catch (FileAccessException |IllegalArgumentException e )
    {
      Logger.getInstance().log("ERROR", e.getMessage());
    }
  }
}

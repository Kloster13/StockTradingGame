import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.StockDao;

import java.io.File;
import java.util.UUID;

public class maiun
{
  public static void main(String[] args)
  {
    String testDirPath = "testdata/test-" + UUID.randomUUID()+"/";
    new File(testDirPath).mkdirs();
    FileUnitOfWork uow  =new FileUnitOfWork(testDirPath);
    StockDao stockDao = new StockDaoFileImplementation(uow);

File dir = new File(testDirPath);
    for (File file : dir.listFiles())
      if (!file.isDirectory())
        file.delete();
    dir.delete();


  }
}

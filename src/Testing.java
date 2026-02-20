import domain.OwnedStock;
import persistence.fileimplementation.FileUnitOfWork;

import java.util.List;

public class Testing
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");

   List<OwnedStock> ownedStocks=  tester.getOwnedStock();
   for(OwnedStock ownedStock : ownedStocks){
     System.out.println(ownedStock.getId());
     System.out.println(ownedStock.getStockSymbol());
     System.out.println(ownedStock.getNumberOfShares());
   }
  }
}

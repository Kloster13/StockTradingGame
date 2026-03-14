package Testing;

import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import persistence.fileimplementation.FileUnitOfWork;

public class StockTransactionTest
{
  public static void main(String[] args)
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    StockTransactionService transaction = new StockTransactionService(tester);
    BuySellStockRequest buyStockRequest = new BuySellStockRequest(1, 2, 20);// NVIDIA
    BuySellStockRequest buyStockRequest1 = new BuySellStockRequest(1, 1, 20); // GOOGLE
   // transaction.buyStock(buyStockRequest);
//    transaction.buyStock(buyStockRequest1);


    BuySellStockRequest sellStockRequest = new BuySellStockRequest(1, 2, 10);// NVIDIA
    transaction.sellStock(sellStockRequest);

  }
}

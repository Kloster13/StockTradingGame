package persistence.interfaces;

import domain.*;

import java.util.List;

public interface UnitOfWork
{
  void begin();
  void commit();
  void rollback();

  List<OwnedStock> getOwnedStock();
  List<Portfolio> getPortfolio();
  List<Stock> getStock();
  List<StockPriceHistory> getStockPriceHistory();
  List<Transaction> getTransactions();
}

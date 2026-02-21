package persistence.interfaces;

import domain.*;

import java.util.List;

public interface UnitOfWork
{
  void begin();
  void commit();
  void rollback();

  List<OwnedStock> getOwnedStocks();
  List<Portfolio> getPortfolios();
  List<Stock> getStocks();
  List<StockPriceHistory> getStockPriceHistory();
  List<Transaction> getTransactions();
}

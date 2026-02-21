package persistence.interfaces;

import domain.StockPriceHistory;

import java.util.List;
import java.util.Optional;

public interface StockPriceHistoryDao
{
  void createStockPriceHistory(StockPriceHistory StockPriceHistory);
  void updateStockPriceHistory(StockPriceHistory updatedStockPriceHistory, int oldStockPriceHistory);
  Optional<StockPriceHistory> getStockPriceHistoryById(int id);
  List<StockPriceHistory> getAllStockPriceHistory();
  void deleteStockPriceHistory(int id);
}

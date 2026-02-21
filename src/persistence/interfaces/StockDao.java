package persistence.interfaces;

import domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao
{
 void createStock(Stock stock);
 void updateStock(Stock updatedStock, int oldStock);
 Optional<Stock> getStockById(int id);
 List<Stock> getAllStocks();
 void deleteStock(int id);
}

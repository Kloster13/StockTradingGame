package persistence.interfaces;

import domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao
{
 void createStock(Stock stock);
 void updateStock(Stock updatedStock);
 Optional<Stock> getStockById(int id);
 Optional<Stock> getStockBySymbol(String symbol);
 List<Stock> getAllStocks();
 void deleteStock(int id);
}

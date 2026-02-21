package persistence.interfaces;

import domain.OwnedStock;

import java.util.List;
import java.util.Optional;

public interface OwnedStockDao
{
  void createOwnedStock(OwnedStock OwnedStock);
  void updateOwnedStock(OwnedStock updatedOwnedStock, int oldOwnedStock);
  Optional<OwnedStock> getOwnedStockById(int id);
  List<OwnedStock> getAllOwnedStocks();
  void deleteOwnedStock(int id);
}

package persistence.fileimplementation;

import domain.Stock;
import persistence.interfaces.StockDao;
import shared.logging.Logger;

import java.util.List;
import java.util.Optional;

public class StockDaoFileImplementation implements StockDao
{
  private static int nextId = 0;
  private final FileUnitOfWork uow;

  public StockDaoFileImplementation(FileUnitOfWork uow)
  {
    this.uow = uow;
    calculateNextId();
  }

  @Override public void createStock(Stock stock)
  {
    stock.setId(nextId);
    List<Stock> stocks = uow.getStocks();
    if (!stocks.contains(stock))
      stocks.add(stock);
    else
      Logger.getInstance().log("ERROR", "Stock already in list");
  }

  @Override public void updateStock(Stock updatedStock, int oldStockId)
  {
    Stock oldStock = getStockById(oldStockId).orElseThrow(
        () -> new IllegalArgumentException("Stock not in list"));

    updatedStock.setId(oldStock.getId());
    uow.getStocks().remove(oldStock);
    uow.getStocks().add(updatedStock);
  }

  @Override public Optional<Stock> getStockById(int id)
  {
    for (Stock stock : uow.getStocks())
    {
      if (stock.getId() == id)
        return Optional.of(stock);
    }
    return Optional.empty();
  }

  @Override public List<Stock> getAllStocks()
  {
    return List.copyOf(uow.getStocks());
  }

  @Override public void deleteStock(int stockId)
  {
    Stock oldStock = getStockById(stockId).orElseThrow(
        () -> new IllegalArgumentException("Stock not in list"));
    uow.getStocks().remove(oldStock);
  }

  private void calculateNextId()
  {
    int maxValue = 0;
    for (Stock stock : uow.getStocks())
    {
      if (stock.getId() > maxValue)
        maxValue = stock.getId();
    }
    nextId = maxValue + 1;
  }
}

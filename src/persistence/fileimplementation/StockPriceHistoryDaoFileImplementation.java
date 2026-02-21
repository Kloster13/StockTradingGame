package persistence.fileimplementation;

import domain.StockPriceHistory;
import persistence.interfaces.StockPriceHistoryDao;
import shared.logging.Logger;

import java.util.List;
import java.util.Optional;

public class StockPriceHistoryDaoFileImplementation implements StockPriceHistoryDao
{
  private static int nextId = 0;

  private final FileUnitOfWork uow;

  public StockPriceHistoryDaoFileImplementation(FileUnitOfWork uow)
  {
    this.uow = uow;
    calculateNextId();
  }

  @Override public void createStockPriceHistory(StockPriceHistory stockPriceHistory)
  {
    stockPriceHistory.setId(nextId);
    List<StockPriceHistory> histories = uow.getStockPriceHistory();
    if (!histories.contains(stockPriceHistory))
      histories.add(stockPriceHistory);
    else
      Logger.getInstance().log("ERROR", "StockPriceHistory already in list");
  }

  @Override public void updateStockPriceHistory(StockPriceHistory updatedStockPriceHistory, int oldStockPriceHistoryId)
  {
    StockPriceHistory oldHistory = getStockPriceHistoryById(oldStockPriceHistoryId).orElseThrow(
        () -> new IllegalArgumentException("StockPriceHistory not in list"));

    updatedStockPriceHistory.setId(oldHistory.getId());
    uow.getStockPriceHistory().remove(oldHistory);
    uow.getStockPriceHistory().add(updatedStockPriceHistory);
  }

  @Override public Optional<StockPriceHistory> getStockPriceHistoryById(int id)
  {
    for (StockPriceHistory history : uow.getStockPriceHistory())
    {
      if (history.getId() == id)
        return Optional.of(history);
    }
    return Optional.empty();
  }

  @Override public List<StockPriceHistory> getAllStockPriceHistory()
  {
    return List.copyOf(uow.getStockPriceHistory());
  }

  @Override public void deleteStockPriceHistory(int stockPriceHistoryId)
  {
    StockPriceHistory oldHistory = getStockPriceHistoryById(stockPriceHistoryId).orElseThrow(
        () -> new IllegalArgumentException("StockPriceHistory not in list"));
    uow.getStockPriceHistory().remove(oldHistory);
  }

  private void calculateNextId()
  {
    int maxValue = 0;
    for (StockPriceHistory history : uow.getStockPriceHistory())
    {
      if (history.getId() > maxValue)
        maxValue = history.getId();
    }
    nextId = maxValue + 1;
  }
}
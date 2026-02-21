package persistence.fileimplementation;

import domain.OwnedStock;
import persistence.interfaces.OwnedStockDao;
import shared.logging.Logger;

import java.util.List;
import java.util.Optional;

public class OwnedStockFileImplementation implements OwnedStockDao
{
  private static int nextId = 0;
  private final FileUnitOfWork uow;

  public OwnedStockFileImplementation(FileUnitOfWork uow)
  {
    this.uow = uow;
    calculateNextId();
  }

  @Override public void createOwnedStock(OwnedStock ownedStock)
  {
    ownedStock.setId(nextId);
    List<OwnedStock> ownedStocks = uow.getOwnedStocks();
    if (!ownedStocks.contains(ownedStock))
      ownedStocks.add(ownedStock);
    else
      Logger.getInstance().log("ERROR", "OwnedStock already in list");
  }

  @Override public void updateOwnedStock(OwnedStock updatedOwnedStock, int oldOwnedStockId)
  {
    OwnedStock oldOwnedStock = getOwnedStockById(oldOwnedStockId).orElseThrow(
        () -> new IllegalArgumentException("OwnedStock not in list"));

    updatedOwnedStock.setId(oldOwnedStock.getId());
    uow.getOwnedStocks().remove(oldOwnedStock);
    uow.getOwnedStocks().add(updatedOwnedStock);
  }

  @Override public Optional<OwnedStock> getOwnedStockById(int id)
  {
    for (OwnedStock ownedStock : uow.getOwnedStocks())
    {
      if (ownedStock.getId() == id)
        return Optional.of(ownedStock);
    }
    return Optional.empty();
  }

  @Override public List<OwnedStock> getAllOwnedStocks()
  {
    return List.copyOf(uow.getOwnedStocks());
  }

  @Override public void deleteOwnedStock(int ownedStockId)
  {
    OwnedStock oldOwnedStock = getOwnedStockById(ownedStockId).orElseThrow(
        () -> new IllegalArgumentException("OwnedStock not in list"));
    uow.getOwnedStocks().remove(oldOwnedStock);
  }

  private void calculateNextId()
  {
    int maxValue = 0;
    for (OwnedStock ownedStock : uow.getOwnedStocks())
    {
      if (ownedStock.getId() > maxValue)
        maxValue = ownedStock.getId();
    }
    nextId = maxValue + 1;
  }

}

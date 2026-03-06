package business.stockmarket;

import business.stockmarket.simulation.LiveStock;
import domain.Stock;
import shared.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TheStockMarket
{
  private volatile static TheStockMarket instance;
  private static final Logger logger = Logger.getInstance();

  private final List<LiveStock> liveStocks;

  private TheStockMarket()
  {
    liveStocks = new ArrayList<>();
  }

  public static synchronized TheStockMarket getInstance()
  {
    if (instance == null)
    {
      instance = new TheStockMarket();
    }
    return instance;
  }

  public void addNewLiveStock(String stockSymbol)
  {

    liveStocks.add(new LiveStock(stockSymbol));
  }

  public void addLiveStock(Stock stock)
  {
    liveStocks.add(new LiveStock(stock));
  }

  public void updateStocks()
  {
    for (LiveStock liveStock : liveStocks)
    {
      liveStock.updatePrice();
      logger.log("INFO", liveStock.getSymbol() + " was updated to " + liveStock.getCurrentPrice());
      logger.log("INFO", "New state: " + liveStock.getStateName());
    }
  }
}

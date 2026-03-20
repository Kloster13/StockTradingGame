package business.stockmarket;

import business.services.dtos.LiveStockDTO;
import business.stockmarket.simulation.LiveStock;
import domain.Stock;
import shared.logging.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class TheStockMarket
{
  private volatile static TheStockMarket instance;
  private static final Logger logger = Logger.getInstance();
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  private final List<LiveStock> liveStocks;

  private TheStockMarket()
  {
    liveStocks = new ArrayList<>();
  }

  public void addListener(String evtName, PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(evtName, listener);
  }

  public void addListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void removeListener(String evtName, PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(evtName, listener);
  }

  public void removeListener(PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
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
      firePriceUpdate(liveStock);
      if (liveStock.getStateName().equals("Bankrupt") && liveStock.getBankruptTic() == 0)
        fireBankruptUpdate(liveStock.getSymbol());
      if (liveStock.getStateName().equals("Reset"))
        fireResetUpdate(liveStock.getSymbol());
    }
  }

  private void firePriceUpdate(LiveStock liveStock)
  {
    support.firePropertyChange("PriceUpdate", null,
        new LiveStockDTO(liveStock.getSymbol(), liveStock.getCurrentPrice(),
            liveStock.getStateName()));
  }

  private void fireBankruptUpdate(String symbol)
  {
    support.firePropertyChange("Bankrupt", null, symbol);
  }

  private void fireResetUpdate(String symbol)
  {
    support.firePropertyChange("Reset", null, symbol);
  }
}

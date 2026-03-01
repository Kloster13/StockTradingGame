package business.stockmarket.simulation;

import shared.configuration.AppConfiguration;
import shared.logging.Logger;

public class LiveStock
{
  private String symbol;
  private StockState state; // Change
  double currentPrice;

  public LiveStock(String symbol)
  {
    this.symbol = symbol;
    state = new Steady();
    currentPrice = AppConfiguration.getAppConfiguration().getStockResetValue();
  }

  // TODO add second constructor that take an actual stock

  public void updatePrice()
  {
    double priceChange = state.CalculatePriceChange(this);

    currentPrice += priceChange;
    if (currentPrice < 0)
    {
      Logger.getInstance().log("INFO", symbol + " went bankrupt");
      currentPrice=0;
      state = new Bankrupt();
    }
  }

  public String getSymbol()
  {
    return symbol;
  }

  public double getCurrentPrice()
  {
    return currentPrice;
  }

  public String getStateName()
  {
    return state.getName();
  }

  void setState(StockState state)
  {
    this.state = state;
  }
}

package business.stockmarket.simulation;

import shared.configuration.AppConfiguration;

public class Reset implements StockState
{
  public Reset()
  {
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    liveStock.setState(new Steady());
    return AppConfiguration.getAppConfiguration().getStockResetValue();
  }

  @Override public String getName()
  {
    return "Reset";
  }
}

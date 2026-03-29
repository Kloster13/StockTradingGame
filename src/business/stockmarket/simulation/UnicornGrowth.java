package business.stockmarket.simulation;

import java.util.Random;

public class UnicornGrowth implements StockState
{
  public UnicornGrowth()
  {
  }

  @Override public double calculatePriceChange(LiveStock liveStock)
  {
    liveStock.setState(new Growing());
    return liveStock.getCurrentPrice();
  }

  @Override public String getName()
  {
    return "UnicornGrowth";
  }
}

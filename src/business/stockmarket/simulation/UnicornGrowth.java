package business.stockmarket.simulation;

import java.util.Random;

public class UnicornGrowth implements StockState
{
  public UnicornGrowth()
  {
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    if(new Random().nextDouble()>0.01)
      liveStock.setState(new Growing());
    return liveStock.getCurrentPrice();
  }

  @Override public String getName()
  {
    return "Unicorn Growth";
  }
}

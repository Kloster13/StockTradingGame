package business.stockmarket.simulation;

import java.util.Random;

public class Bankrupt implements StockState
{
  private int tics;

  public Bankrupt()
  {
    tics = 0;
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    tics++;
    if (tics <= 10)
    {
      return 0;
    }
    if(new Random().nextDouble()<0.33)
      liveStock.setState(new Reset());
    return 0;
  }

  @Override public String getName()
  {
    return "Bankrupt";
  }
}

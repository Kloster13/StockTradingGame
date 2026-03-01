package business.stockmarket.simulation;

import java.util.Random;

public class Growing implements StockState
{
  private static final Random random = new Random();
  public Growing()
  {
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if (change < 0.02)
      liveStock.setState(new UnicornGrowth());
    else if (change < 0.22)
      liveStock.setState(new Steady());

    return (0.03 + 0.03 * random.nextDouble()) * liveStock.currentPrice;
  }
  @Override public String getName()
  {
    return "Growing";
  }
}

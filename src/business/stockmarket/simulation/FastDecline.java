package business.stockmarket.simulation;

import java.util.Random;

public class FastDecline implements StockState
{
  private static final Random random = new Random();

  public FastDecline()
  {
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if (change < 0.3)
      liveStock.setState(new Declining());

    return (0.3 +0.3 *random.nextDouble()) * liveStock.currentPrice * -1;
  }

  @Override public String getName()
  {
    return "Fast Decline";
  }
}

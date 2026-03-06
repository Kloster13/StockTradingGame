package business.stockmarket.simulation;

import java.util.Random;

public class FastDecline implements StockState
{
  private static final Random random = new Random();

  public FastDecline()
  {
  }

  @Override public double calculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if (change < 0.3)
      liveStock.setState(new Declining());

    return random.nextInt(15)-30;
  }

  @Override public String getName()
  {
    return "FastDecline";
  }
}

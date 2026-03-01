package business.stockmarket.simulation;

import java.util.Random;

public class Steady implements StockState
{
  private static final Random random = new Random();

  public Steady()
  {
  }

  public double CalculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if (change < 0.03)
      liveStock.setState(new FastDecline());
    else if(change<0.1)
      liveStock.setState(new Declining());
    else if(change<0.2)
      liveStock.setState(new Growing());
    return random.nextInt(10) - 5;
  }

  @Override public String getName()
  {
    return "Steady";
  }
}

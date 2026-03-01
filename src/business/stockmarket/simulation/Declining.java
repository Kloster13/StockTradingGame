package business.stockmarket.simulation;

import java.util.Random;

public class Declining implements StockState
{
  private static final Random random = new Random();

  public Declining()
  {
  }

  @Override public double CalculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if(change<0.03)
      liveStock.setState(new FastDecline());
    else if(change<0.23)
      liveStock.setState(new Steady());

    return (0.03 +0.03 *random.nextDouble()) * liveStock.currentPrice * -1; // Between -0.5-1
  }

  @Override public String getName()
  {
    return "Declining";
  }
}

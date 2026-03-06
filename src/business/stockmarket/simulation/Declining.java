package business.stockmarket.simulation;

import java.util.Random;

public class Declining implements StockState
{
  private static final Random random = new Random();

  public Declining()
  {
  }

  @Override public double calculatePriceChange(LiveStock liveStock)
  {
    double change = random.nextDouble();
    if(change<0.03)
      liveStock.setState(new FastDecline());
    else if(change<0.20)
      liveStock.setState(new Steady());

    return random.nextInt(10)-15;
  }

  @Override public String getName()
  {
    return "Declining";
  }
}

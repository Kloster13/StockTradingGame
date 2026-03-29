package business.stockmarket;

import shared.configuration.AppConfiguration;

public class MarketTicker implements Runnable
{
  private volatile boolean running;

  public MarketTicker()
  {
    running = false;
  }

  @Override public void run()
  {
    TheStockMarket market = TheStockMarket.getInstance();
    running = true;
    while (running)
    {
      market.updateStocks();
      try
      {
        Thread.sleep(
            AppConfiguration.getAppConfiguration().getUpdateFrequencyInMs());
      }
      catch (InterruptedException e)
      {
      }
    }
  }

  public void stopMarket()
  {
    running = false;
  }
}

package business.stockmarket;

import shared.configuration.AppConfiguration;

public class MarketTicker
{

  public MarketTicker()
  {
  }

  public void runMarket() throws InterruptedException
  {
    while (true)
    {
     TheStockMarket.getInstance().updateStocks();
     Thread.sleep(AppConfiguration.getAppConfiguration().getUpdateFrequencyInMs());
    }
  }

}

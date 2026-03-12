package business.stockmarket;

import shared.configuration.AppConfiguration;

public class MarketTicker
{

  public MarketTicker()
  {
  }

  public void runMarket() throws InterruptedException
  {
    TheStockMarket market = TheStockMarket.getInstance();
    while (true)
    {
     market.updateStocks();
     Thread.sleep(AppConfiguration.getAppConfiguration().getUpdateFrequencyInMs());
    }
  }

}

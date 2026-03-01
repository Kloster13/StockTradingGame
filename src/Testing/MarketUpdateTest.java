package Testing;

import business.stockmarket.MarketTicker;
import business.stockmarket.TheStockMarket;
import business.stockmarket.simulation.LiveStock;
import shared.logging.Logger;

public class MarketUpdateTest
{
  public static void main(String[] args)
  {
    TheStockMarket.getInstance().addNewLiveStock("META");
    MarketTicker marketTicker = new MarketTicker();
    try
    {
      marketTicker.runMarket();
    }
    catch (InterruptedException e)
    {
      Logger.getInstance().log("ERROR","Interrupted");
    }
  }
}

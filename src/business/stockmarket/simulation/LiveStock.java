package business.stockmarket.simulation;

import domain.Stock;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

public class LiveStock
{
  private final String symbol;
  private StockState state;
  double currentPrice;

  public LiveStock(String symbol)
  {
    this.symbol = symbol;
    state = new Steady();
    currentPrice = AppConfiguration.getAppConfiguration().getStockResetValue();
  }

  public LiveStock(Stock stock)
  {
    symbol = stock.getSymbol();
    state = switch (stock.getCurrentState())
    {
      case "Steady" -> new Steady();
      case "Bankrupt" -> new Bankrupt();
      case "Declining" -> new Declining();
      case "FastDecline" -> new FastDecline();
      case "Growing" -> new Growing();
      case "UnicornGrowth"-> new UnicornGrowth();
      default -> throw new IllegalStateException("Unexpected value: " + stock.getCurrentState());
    };
    currentPrice = stock.getCurrentPrice();
  }

  public void updatePrice()
  {
    double priceChange = state.calculatePriceChange(this);

    currentPrice += priceChange;
    if (currentPrice < 0)
    {
      Logger.getInstance().log("INFO", symbol + " went bankrupt");
      currentPrice = 0;
      state = new Bankrupt();
    }
  }

  public String getSymbol()
  {
    return symbol;
  }

  public double getCurrentPrice()
  {
    return currentPrice;
  }

  public String getStateName()
  {
    return state.getName();
  }

  void setState(StockState state)
  {
    this.state = state;
  }
}

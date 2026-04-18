package business.stockmarket.simulation;

import domain.Stock;
import shared.configuration.AppConfiguration;
import shared.logging.Logger;

import java.util.Objects;

public class LiveStock
{
  private final String symbol;
  private StockState state;
  double currentPrice;
  private int bankruptTic;

  public LiveStock(String symbol)
  {
    this.symbol = symbol;
    state = new Steady();
    currentPrice = AppConfiguration.getAppConfiguration().getStockResetValue();
    bankruptTic=-1;
  }

  public LiveStock(Stock stock)
  {
    symbol = stock.getSymbol();
    state = switch (stock.getCurrentState())
    {
      case "Bankrupt" -> new Bankrupt();

      case "Steady" -> new Steady();
      case "Declining" -> new Declining();
      case "FastDecline" -> new FastDecline();
      case "Growing" -> new Growing();
      case "UnicornGrowth" -> new UnicornGrowth();
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
    else if(state instanceof Bankrupt)
      bankruptTic++;
    if (state instanceof Reset)
    {
      bankruptTic = -1;
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

  public int getBankruptTic()
  {
    return bankruptTic;
  }

  @Override public boolean equals(Object o)
  {
    if (o == null || getClass() != o.getClass())
      return false;
    LiveStock liveStock = (LiveStock) o;
    return Objects.equals(symbol, liveStock.symbol);
  }

  @Override public int hashCode()
  {
    return Objects.hashCode(symbol);
  }
}

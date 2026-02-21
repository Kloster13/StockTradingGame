package domain;

import java.util.Objects;

public class Stock
{
  private int id;
  private final String symbol;
  private final String name;
  private double currentPrice;
  private String currentState;

  public Stock(String symbol, String name, double currentPrice)
  {
    this.symbol = symbol;
    this.name = name;
    this.currentPrice = currentPrice;
    this.currentState = "Steady";
  }

  public Stock(int id, String symbol, String name, double currentPrice, String currentState)
  {
    this.id = id;
    this.symbol = symbol;
    this.name = name;
    this.currentPrice = currentPrice;
    this.currentState = currentState;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getSymbol()
  {
    return symbol;
  }

  public String getName()
  {
    return name;
  }

  public double getCurrentPrice()
  {
    return currentPrice;
  }

  public void setCurrentPrice(double currentPrice)
  {
    this.currentPrice = currentPrice;
  }

  public String getCurrentState()
  {
    return currentState;
  }

  public void setCurrentState(String currentState)
  {
    this.currentState = currentState;
  }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof Stock stock))
      return false;
    return Double.compare(currentPrice, stock.currentPrice) == 0 && Objects.equals(symbol,
        stock.symbol) && Objects.equals(name, stock.name);
  }

  @Override public int hashCode()
  {
    return Objects.hash(symbol, name, currentPrice);
  }
}

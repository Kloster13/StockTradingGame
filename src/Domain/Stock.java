package Domain;

public class Stock
{
  private String symbol;
  private String name;
  private double currentPrice;
  private String currentState;

  public Stock(String symbol, String name, double currentPrice, String currentState)
  {
    this.symbol = symbol;
    this.name = name;
    this.currentPrice = currentPrice;
    this.currentState = currentState;
  }

  public String getSymbol()
  {
    return symbol;
  }

  public void setSymbol(String symbol)
  {
    this.symbol = symbol;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
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
}

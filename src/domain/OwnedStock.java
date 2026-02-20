package domain;

public class OwnedStock
{
  private int id;
  private final String stockSymbol;
  private int numberOfShares;

  public OwnedStock(String stockSymbol, int numberOfShares)
  {
    this.stockSymbol = stockSymbol;
    this.numberOfShares = numberOfShares;
  }

  public OwnedStock(int id,String stockSymbol, int numberOfShares)
  {
    this.id=id;
    this.stockSymbol = stockSymbol;
    this.numberOfShares = numberOfShares;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getStockSymbol()
  {
    return stockSymbol;
  }

  public int getNumberOfShares()
  {
    return numberOfShares;
  }

  public void setNumberOfShares(int numberOfShares)
  {
    this.numberOfShares = numberOfShares;
  }
}

package Domain;

public class OwnedStock
{
  private int id;
  private String stockSymbol;
  private int numberOfShares;

  public OwnedStock(String stockSymbol, int numberOfShares)
  {
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

  public void setStockSymbol(String stockSymbol)
  {
    this.stockSymbol = stockSymbol;
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

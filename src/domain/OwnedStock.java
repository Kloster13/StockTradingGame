package domain;

public class OwnedStock
{
  private int id;
  private int portfolioId;
  private final String stockSymbol;
  private int numberOfShares;

  public OwnedStock(String stockSymbol, int portfolioId, int numberOfShares)
  {
    this.stockSymbol = stockSymbol;
    this.portfolioId=portfolioId;
    this.numberOfShares = numberOfShares;
  }

  public OwnedStock(int id, int portfolioId,String stockSymbol, int numberOfShares)
  {
    this.id=id;
    this.portfolioId=portfolioId;
    this.stockSymbol = stockSymbol;
    this.numberOfShares = numberOfShares;
  }

  public int getId()
  {
    return id;
  }

  public int getPortfolioId()
  {
    return portfolioId;
  }

  public void setPortfolioId(int portfolioId)
  {
    this.portfolioId = portfolioId;
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

package domain;

import java.time.LocalDate;

public class StockPriceHistory
{
  private int id;
  private final String stockSymbol;
  private final double price;
  private final LocalDate timestamp;

  public StockPriceHistory(String stockSymbol, double price)
  {
    this.stockSymbol = stockSymbol;
    this.price = price;
    this.timestamp = LocalDate.now();
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

  public double getPrice()
  {
    return price;
  }

  public LocalDate getTimestamp()
  {
    return timestamp;
  }

}

package Domain;

import java.time.LocalDate;

public class StockPriceHistory
{
  private int id;
  private String stockSymbol;
  private double price;
  private LocalDate timestamp;

  public StockPriceHistory(String stockSymbol, double price)
  {
    this.stockSymbol = stockSymbol;
    this.price = price;
    this.timestamp=LocalDate.now();
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

  public double getPrice()
  {
    return price;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  public LocalDate getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp)
  {
    this.timestamp = timestamp;
  }
}

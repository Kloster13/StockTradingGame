package Domain;

import java.time.LocalDate;

public class Transaction
{
  private int id;
  private int portfolioId;
  private String stockSymbol;
  private String type;
  private int quantity;
  private double pricePrShare;
  private double totalAmount;
  private double fee;
  private LocalDate timestamp;

  public Transaction(String stockSymbol, String type, int quantity, double pricePrShare, double fee)
  {
    this.stockSymbol = stockSymbol;
    this.type = type;
    this.quantity = quantity;
    this.pricePrShare = pricePrShare;
    this.fee = fee;
    this.totalAmount=pricePrShare*quantity;
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

  public int getPortfolioId()
  {
    return portfolioId;
  }

  public void setPortfolioId(int portfolioId)
  {
    this.portfolioId = portfolioId;
  }

  public String getStockSymbol()
  {
    return stockSymbol;
  }

  public void setStockSymbol(String stockSymbol)
  {
    this.stockSymbol = stockSymbol;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity(int quantity)
  {
    this.quantity = quantity;
  }

  public double getPricePrShare()
  {
    return pricePrShare;
  }

  public void setPricePrShare(double pricePrShare)
  {
    this.pricePrShare = pricePrShare;
  }

  public double getTotalAmount()
  {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount)
  {
    this.totalAmount = totalAmount;
  }

  public double getFee()
  {
    return fee;
  }

  public void setFee(double fee)
  {
    this.fee = fee;
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

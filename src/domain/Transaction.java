package domain;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction
{
  private int id;
  private final String stockSymbol;
  private final String type;
  private final int quantity;
  private final double pricePrShare;
  private final double totalAmount;
  private final double fee;
  private final LocalDate timestamp;

  public Transaction(String stockSymbol, String type, int quantity, double pricePrShare, double fee)
  {
    this.stockSymbol = stockSymbol;
    this.type = type;
    this.quantity = quantity;
    this.pricePrShare = pricePrShare;
    this.fee = fee;
    this.totalAmount = (pricePrShare * quantity) +fee;
    this.timestamp = LocalDate.now();
  }

  public Transaction(int id, String stockSymbol, String type, int quantity, double pricePrShare,
      double fee, LocalDate timestamp)
  {
    this.id = id;
    this.stockSymbol = stockSymbol;
    this.type = type;
    this.quantity = quantity;
    this.pricePrShare = pricePrShare;
    this.fee = fee;
    this.totalAmount = (pricePrShare * quantity) +fee;
    this.timestamp = timestamp;
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

  public String getType()
  {
    return type;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public double getPricePrShare()
  {
    return pricePrShare;
  }

  public double getTotalAmount()
  {
    return totalAmount;
  }

  public double getFee()
  {
    return fee;
  }

  public LocalDate getTimestamp()
  {
    return timestamp;
  }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof Transaction that))
      return false;
    return quantity == that.quantity && Double.compare(pricePrShare, that.pricePrShare) == 0
        && Double.compare(totalAmount, that.totalAmount) == 0 && Double.compare(fee, that.fee) == 0
        && Objects.equals(stockSymbol, that.stockSymbol) && Objects.equals(type, that.type)
        && Objects.equals(timestamp, that.timestamp);
  }

  @Override public int hashCode()
  {
    return Objects.hash(stockSymbol, type, quantity, pricePrShare, totalAmount, fee, timestamp);
  }
}

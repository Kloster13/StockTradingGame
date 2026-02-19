package domain;

import java.util.ArrayList;
import java.util.List;

public class Portfolio
{
  private int id;
  private int currentBalance;
  private List<Integer> ownedStock;
  private List<Integer> transactions;

  public Portfolio(int currentBalance)
  {
    this.currentBalance = currentBalance;
    ownedStock=new ArrayList<>();
    transactions=new ArrayList<>();
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public int getCurrentBalance()
  {
    return currentBalance;
  }

  public void setCurrentBalance(int currentBalance)
  {
    this.currentBalance = currentBalance;
  }

  public List<Integer> getOwnedStock()
  {
    return ownedStock;
  }

  public void setOwnedStock(List<Integer> ownedStock)
  {
    this.ownedStock = ownedStock;
  }

  public List<Integer> getTransactions()
  {
    return transactions;
  }

  public void setTransactions(List<Integer> transactions)
  {
    this.transactions = transactions;
  }
}

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
    ownedStock = new ArrayList<>();
    transactions = new ArrayList<>();
  }
  public Portfolio(int id, int currentBalance, List<Integer> ownedStock, List<Integer> transactions)
  {
    this.id=id;
    this.currentBalance = currentBalance;
    this.ownedStock=ownedStock;
    this.transactions = transactions;
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
    return List.copyOf(ownedStock);
  }

  public void addOwnedStock(int stockId)
  {
    ownedStock.add(stockId);
  }

  public List<Integer> getTransactions()
  {
    return List.copyOf(transactions);
  }

  public void setTransactions(int transactionsId)
  {
    transactions.add(transactionsId);
  }
}

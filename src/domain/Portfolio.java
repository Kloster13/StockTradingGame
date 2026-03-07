package domain;

import java.util.*;

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
    this.ownedStock = new ArrayList<>(Objects.requireNonNull(ownedStock, "ownedStock"));
    this.transactions = new ArrayList<>(Objects.requireNonNull(transactions, "transactions"));

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

  public ArrayList<Integer> getOwnedStock()
  {
    return new ArrayList<>(ownedStock);
  }

  public void removeOwnedStock(int id){
    if(ownedStock.contains(id))
      ownedStock.remove(Integer.valueOf(id));
  }

  public void addOwnedStock(int stockId)
  {
    ownedStock.add(stockId);
  }

  public ArrayList<Integer> getTransactions()
  {
    return new ArrayList<>(transactions);
  }

  public void addTransactions(int transactionsId)
  {
    transactions.add(transactionsId);
  }

  @Override public String toString()
  {
    return "Portfolio{" + "currentBalance=" + currentBalance + ", ownedStock=" + ownedStock
        + ", transactions=" + transactions + '}';
  }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof Portfolio portfolio))
      return false;
    return currentBalance == portfolio.currentBalance && Objects.equals(ownedStock,
        portfolio.ownedStock) && Objects.equals(transactions, portfolio.transactions);
  }

  @Override public int hashCode()
  {
    return Objects.hash(currentBalance, ownedStock, transactions);
  }
}

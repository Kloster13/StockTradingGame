package domain;

import java.util.*;

public class Portfolio
{
  private int id;
  private String name;
  private double currentBalance;
  private final List<Integer> transactions;

  public Portfolio(String name, double currentBalance)
  {
    this.name = name;
    this.currentBalance = currentBalance;
    transactions = new ArrayList<>();
  }

  public Portfolio(int id, String name, double currentBalance, List<Integer> transactions)
  {
    this.id = id;
    this.name=name;
    this.currentBalance = currentBalance;
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

  public double getCurrentBalance()
  {
    return currentBalance;
  }

  public void setCurrentBalance(double currentBalance)
  {
    this.currentBalance = currentBalance;
  }

  public void updateCurrentBalance(double change)
  {
    this.currentBalance += change;
  }

  public ArrayList<Integer> getTransactions()
  {
    return new ArrayList<>(transactions);
  }

  public void addTransactions(int transactionsId)
  {
    transactions.add(transactionsId);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override public String toString()
  {
    return name +" ("+currentBalance+")";
  }

  @Override public boolean equals(Object o)
  {
    if (o == null || getClass() != o.getClass())
      return false;
    Portfolio portfolio = (Portfolio) o;
    return Double.compare(currentBalance, portfolio.currentBalance) == 0 && Objects.equals(name,
        portfolio.name) && Objects.equals(transactions, portfolio.transactions);
  }

  @Override public int hashCode()
  {
    return Objects.hash(name, currentBalance, transactions);
  }
}

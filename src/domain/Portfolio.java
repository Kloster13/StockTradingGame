package domain;

import java.util.*;

public class Portfolio
{
  private int id;
  private double currentBalance;
  private List<Integer> transactions;

  public Portfolio(double currentBalance)
  {
    this.currentBalance = currentBalance;
    transactions = new ArrayList<>();
  }

  public Portfolio(int id, double currentBalance, List<Integer> transactions)
  {
    this.id = id;
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

  @Override public String toString()
  {
    return "Portfolio{" + "currentBalance=" + currentBalance + ", transactions=" + transactions
        + '}';
  }

  @Override public boolean equals(Object o)
  {
    if (!(o instanceof Portfolio portfolio))
      return false;
    return currentBalance == portfolio.currentBalance && Objects.equals(transactions,
        portfolio.transactions);
  }

  @Override public int hashCode()
  {
    return Objects.hash(currentBalance, transactions);
  }
}

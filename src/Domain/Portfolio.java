package Domain;

public class Portfolio
{
  private int id;
  private int currentBalance;

  public Portfolio(int currentBalance)
  {
    this.currentBalance = currentBalance;
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
}

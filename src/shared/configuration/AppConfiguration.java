package shared.configuration;

public class AppConfiguration
{
  private static AppConfiguration instance;
  private final int startingBalance = 10000;
  private double transactionFee = 5;
  private final int updateFrequencyInMs = 1000;
  private final double stockResetValue = 100;

  private AppConfiguration()
  {
  }

  public static AppConfiguration getAppConfiguration()
  {
    if (instance == null)
    {
      instance = new AppConfiguration();
    }
    return instance;
  }

  public int getStartingBalance()
  {
    return startingBalance;
  }

  public void setTransactionFee(double transactionFee)
  {
    this.transactionFee = transactionFee;
  }

  public double getTransactionFee()
  {
    return transactionFee;
  }


  public int getUpdateFrequencyInMs()
  {
    return updateFrequencyInMs;
  }

  public double getStockResetValue()
  {
    return stockResetValue;
  }
}

package shared.configuration;

import domain.Stock;

import java.util.List;

public class AppConfiguration
{
  private static AppConfiguration instance;
  private final double startingBalance = 10000.0;
  private double transactionFee = 5;
  private final int updateFrequencyInMs = 1000;
  private final double stockResetValue = 100;

  private String directoryPath = "src/data/devdata/";

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

  public double getStartingBalance()
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

  public void setDirectoryPath(String directoryPath)
  {
    this.directoryPath = directoryPath;
  }

  public int getUpdateFrequencyInMs()
  {
    return updateFrequencyInMs;
  }

  public double getStockResetValue()
  {
    return stockResetValue;
  }

  public String getDirectoryPath()
  {
    return directoryPath;
  }
}

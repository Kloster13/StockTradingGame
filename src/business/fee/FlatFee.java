package business.fee;

import shared.configuration.AppConfiguration;

public class FlatFee implements FeeStrategy
{
  public FlatFee()
  {
  }

  @Override public double calculateFee(int quantity, double price)
  {
    return AppConfiguration.getAppConfiguration().getTransactionFee();
  }
}

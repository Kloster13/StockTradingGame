package business.fee;

public class PercentageFee implements FeeStrategy
{
  public PercentageFee()
  {
  }
  @Override public double calculateFee(int quantity, double price)
  {
    return (quantity * price) * 0.1;
  }
}

package business.fee;

public class QuantityFee implements FeeStrategy
{
  @Override public double calculateFee(int quantity, double price)
  {
    return quantity*2;
  }
}

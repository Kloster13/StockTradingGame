package business.fee;

public interface FeeStrategy
{
  double calculateFee(int quantity, double price);
}

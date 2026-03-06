package business.stockmarket.simulation;

public interface StockState
{
  double calculatePriceChange(LiveStock liveStock);
  String getName();
}

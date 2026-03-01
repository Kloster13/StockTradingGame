package business.stockmarket.simulation;

public interface StockState
{
  double CalculatePriceChange(LiveStock liveStock);
  String getName();
}

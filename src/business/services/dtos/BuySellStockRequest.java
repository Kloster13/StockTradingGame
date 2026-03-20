package business.services.dtos;

public record BuySellStockRequest(int portfolioID, String symbol, int quantity)
{
}

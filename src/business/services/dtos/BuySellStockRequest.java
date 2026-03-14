package business.services.dtos;

public record BuySellStockRequest(int portfolioID, int stockId, int quantity)
{
}

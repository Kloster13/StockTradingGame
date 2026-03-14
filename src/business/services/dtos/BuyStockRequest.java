package business.services.dtos;

public record BuyStockRequest(int portfolioID, int stockId, int quantity)
{
}

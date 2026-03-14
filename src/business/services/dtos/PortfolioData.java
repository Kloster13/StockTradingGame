package business.services.dtos;

import java.util.List;
import java.util.Map;

public record PortfolioData(double currentBalance, double portfolioValue, Map<String, Integer> ownedStockInfo)
{
}

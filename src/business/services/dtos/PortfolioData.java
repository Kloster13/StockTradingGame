package business.services.dtos;

import java.util.ArrayList;
import java.util.Map;

public record PortfolioData(double currentBalance, double portfolioValue, ArrayList<OwnedStockDTO> ownedStockInfo)
{
}

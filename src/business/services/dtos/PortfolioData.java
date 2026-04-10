package business.services.dtos;

import java.util.ArrayList;
import java.util.Map;

public record PortfolioData(String name,double currentBalance, double portfolioValue, ArrayList<OwnedStockDTO> ownedStockInfo)
{
}

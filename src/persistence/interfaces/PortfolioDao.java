package persistence.interfaces;

import domain.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioDao
{
  void createPortfolio(Portfolio Portfolio);
  void updatePortfolio(Portfolio updatedPortfolio, int oldPortfolio);
  Optional<Portfolio> getPortfolioById(int id);
  List<Portfolio> getAllPortfolios();
  void deletePortfolio(int id);
}

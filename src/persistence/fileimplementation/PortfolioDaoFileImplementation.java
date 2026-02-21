package persistence.fileimplementation;

import domain.Portfolio;
import persistence.interfaces.PortfolioDao;
import shared.logging.Logger;

import java.util.List;
import java.util.Optional;

public class PortfolioDaoFileImplementation implements PortfolioDao
{
  private static int nextId = 0;

  private final FileUnitOfWork uow;

  public PortfolioDaoFileImplementation(FileUnitOfWork uow)
  {
    this.uow = uow;
    calculateNextId();
  }

  @Override public void createPortfolio(Portfolio portfolio)
  {
    portfolio.setId(nextId);
    List<Portfolio> portfolios = uow.getPortfolios();
    if (!portfolios.contains(portfolio))
      portfolios.add(portfolio);
    else
      Logger.getInstance().log("ERROR", "Portfolio already in list");
  }

  @Override public void updatePortfolio(Portfolio updatedPortfolio, int oldPortfolioId)
  {
    Portfolio oldPortfolio = getPortfolioById(oldPortfolioId).orElseThrow(
        () -> new IllegalArgumentException("Portfolio not in list"));

    updatedPortfolio.setId(oldPortfolio.getId());
    uow.getPortfolios().remove(oldPortfolio);
    uow.getPortfolios().add(updatedPortfolio);
  }

  @Override public Optional<Portfolio> getPortfolioById(int id)
  {
    for (Portfolio portfolio : uow.getPortfolios())
    {
      if (portfolio.getId() == id)
        return Optional.of(portfolio);
    }
    return Optional.empty();
  }

  @Override public List<Portfolio> getAllPortfolios()
  {
    return List.copyOf(uow.getPortfolios());
  }

  @Override public void deletePortfolio(int portfolioId)
  {
    Portfolio oldPortfolio = getPortfolioById(portfolioId).orElseThrow(
        () -> new IllegalArgumentException("Portfolio not in list"));
    uow.getPortfolios().remove(oldPortfolio);
  }

  private void calculateNextId()
  {
    int maxValue = 0;
    for (Portfolio portfolio : uow.getPortfolios())
    {
      if (portfolio.getId() > maxValue)
        maxValue = portfolio.getId();
    }
    nextId = maxValue + 1;
  }
}
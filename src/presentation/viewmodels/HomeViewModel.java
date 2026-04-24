package presentation.viewmodels;

import business.services.GameService;
import business.services.PortfolioService;
import domain.Portfolio;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.core.ActivePortfolioCache;

public class HomeViewModel
{
  private final GameService gameService;
  ActivePortfolioCache activePortfolioCache;
  private final ObservableList<Portfolio> portfolios = FXCollections.observableArrayList();
  private final ObjectProperty<Portfolio> selectedPortfolio = new SimpleObjectProperty<>();

  public HomeViewModel(GameService gameService, PortfolioService portfolioService, ActivePortfolioCache cache)
  {
    this.gameService = gameService;
    activePortfolioCache = cache;
    portfolios.setAll(portfolioService.getAllPortfolios());
    if (portfolios.isEmpty())
    {
      gameService.resetGame();
      portfolios.setAll(portfolioService.getAllPortfolios());
      portfolios.getFirst();
    }
    selectedPortfolio.addListener((obs, oldValue, newValue) -> setActivePortfolio(newValue));

    if (activePortfolioCache.getPortfolioId() == 0)
    {
      setActivePortfolio(portfolios.getFirst());
    }
  selectedPortfolio.setValue(portfolioService.getPortfolioById(cache.getPortfolioId()));
  }

  public ObservableList<Portfolio> getPortfolios()
  {
    return portfolios;
  }

  public void startGame()
  {
    gameService.startGame();
  }

  public void stopGame()
  {
    gameService.stopGame();
  }

  public void resetGame()
  {
    gameService.resetGame();
  }

  public ObjectProperty<Portfolio> selectedPortfolioProperty()
  {
    return selectedPortfolio;
  }

  private void setActivePortfolio(Portfolio portfolio)
  {
    activePortfolioCache.setPortfolioId(portfolio.getId());
  }
}

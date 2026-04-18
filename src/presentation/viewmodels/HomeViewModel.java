package presentation.viewmodels;

import business.services.GameService;
import business.services.PortfolioService;
import domain.Portfolio;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.core.AppContext;

public class HomeViewModel
{
  private final GameService gameService;
  private final ObservableList<Portfolio> portfolios = FXCollections.observableArrayList();
  private final ObjectProperty<Portfolio> selectedPortfolio = new SimpleObjectProperty<>();

  public HomeViewModel(GameService gameService, PortfolioService portfolioService)
  {
    this.gameService = gameService;
    portfolios.setAll(portfolioService.getAllPortfolios());
    selectedPortfolio.addListener((obs, oldValue, newValue) -> setActivePortfolio(newValue));
    selectedPortfolio.setValue(portfolios.getFirst());
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
    AppContext.getAppContext().setActivePortfolio(portfolio.getId());
  }
}

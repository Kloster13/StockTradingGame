package presentation.viewmodels;

import business.fee.FeeStrategy;
import business.fee.FlatFee;
import business.fee.PercentageFee;
import business.fee.QuantityFee;
import business.services.GameService;
import business.services.PortfolioService;
import domain.Portfolio;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.core.ActivePortfolioCache;
import presentation.core.AppContext;

import java.util.List;

public class HomeViewModel
{
  private final GameService gameService;
  ActivePortfolioCache activePortfolioCache;
  private final ObservableList<Portfolio> portfolios = FXCollections.observableArrayList();
  private final ObjectProperty<Portfolio> selectedPortfolio = new SimpleObjectProperty<>();
  private final ObservableList<String> feeStrategies = FXCollections.observableArrayList();
  private final StringProperty selectedFee = new SimpleStringProperty("Flat");

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

    feeStrategies.setAll(List.of("Flat", "Percentage","Quantity"));
    selectedFee.addListener((obs, oldValue, newValue) -> setFeeStrategy(newValue));
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

  private void setFeeStrategy(String strategy)
  {
    FeeStrategy feeStrategy = switch (strategy)
    {
      case "Percentage" -> new PercentageFee();
      case "Quantity" ->new QuantityFee();
      default -> new FlatFee();
    };
    System.out.println(strategy);
    AppContext.getAppContext().setFeeStrategy(feeStrategy);
  }

  private void setActivePortfolio(Portfolio portfolio)
  {
    activePortfolioCache.setPortfolioId(portfolio.getId());
  }

  public ObservableList<String> getFeeStrategies()
  {
    return feeStrategies;
  }

  public StringProperty selectedFeeProperty()
  {
    return selectedFee;
  }
}

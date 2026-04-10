package presentation.controllers;

import domain.Portfolio;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import presentation.core.AppContext;
import presentation.viewmodels.HomeViewModel;

public class HomeViewController
{
  private HomeViewModel viewModel;
  public ComboBox<Portfolio> portfolioCombo;
  public Button startGameButton;
  public Button resetGameButton;


  public HomeViewController(HomeViewModel viewModel)
  {
    this.viewModel=viewModel;
    AppContext.getAppContext().setActivePortfolio(1);

  }

  public void handleStartGame()
  {
  }

  public void handleReset()
  {
  }
}

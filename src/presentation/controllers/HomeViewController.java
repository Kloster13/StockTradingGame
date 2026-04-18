package presentation.controllers;

import domain.Portfolio;
import javafx.fxml.FXML;
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


  @FXML private void initialize()
  {
    portfolioCombo.setItems(viewModel.getPortfolios());
    portfolioCombo.valueProperty().bindBidirectional(viewModel.selectedPortfolioProperty());
  }

  public HomeViewController(HomeViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  public void handleStartGame()
  {
    viewModel.startGame();
  }
  public void handleStop(){
    viewModel.stopGame();
  }

  public void handleReset()
  {
    viewModel.resetGame();
  }
}

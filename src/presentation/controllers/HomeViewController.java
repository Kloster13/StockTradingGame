package presentation.controllers;

import domain.Portfolio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import presentation.core.AppContext;
import presentation.viewmodels.HomeViewModel;

public class HomeViewController
{
  private final HomeViewModel viewModel;
  public ComboBox<Portfolio> portfolioCombo;
  public Button startGameButton;
  public Button resetGameButton;
  public ComboBox<String> feeStrategyCombo;

  @FXML private void initialize()
  {
    portfolioCombo.setItems(viewModel.getPortfolios());
    portfolioCombo.valueProperty().bindBidirectional(viewModel.selectedPortfolioProperty());
    feeStrategyCombo.setItems(viewModel.getFeeStrategies());
    feeStrategyCombo.valueProperty().bindBidirectional(viewModel.selectedFeeProperty());
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

  public void handleFeeStrategy()
  {
  }
}

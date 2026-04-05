package presentation.controllers;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import presentation.viewmodels.StockMarketViewModel;

public class StockMarketController
{
  public LineChart<Number, Number> stockChart;
  public Button startGameButton;
  public Button resetButton;
  public ComboBox<String> buyStockDropdown;
  public Spinner<Integer> buyStockAmount;
  public Button buyStockButton;
  public Label buyStatusLabel;
  public ComboBox<String> sellStockDropdown;
  public Spinner<Integer> sellStockAmount;
  public Label sellStatusLabel;
  public Button sellStockButton;
  private NumberAxis xAxis;
  private NumberAxis yAxis;

  private StockMarketViewModel viewModel;

  public StockMarketController(StockMarketViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  @FXML private void initialize()
  {
    // binding
    // TODO note til Troels - det tog mig meget lang tid at få min AI til at finde frem til den her løsning.
    //  Det var den eneste måde jeg kunne få bindingen til at fungere. Er det virkelig sådan her det skal gøres?
    Platform.runLater(() -> {
      SpinnerValueFactory<Integer> buyStockValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
          0, 200, 0);
      buyStockAmount.setValueFactory(buyStockValueFactory);
      buyStockAmount.setEditable(true);
      buyStockValueFactory.valueProperty().bindBidirectional(viewModel.buyAmountProperty().asObject());

      SpinnerValueFactory<Integer> sellStockValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
          0, 200, 0);
      sellStockAmount.setValueFactory(sellStockValueFactory);
      sellStockAmount.setEditable(true);
      sellStockValueFactory.valueProperty().bindBidirectional(viewModel.sellAmountProperty().asObject());

    });
    //Byy
    buyStatusLabel.textProperty().bind(viewModel.buyStatusProperty());
    buyStockDropdown.valueProperty().bindBidirectional(viewModel.stockSymbolProperty());
    buyStockDropdown.setItems(viewModel.getStockSymbols());
    BooleanBinding cannotBuy = viewModel.canBuyProperty().not();
    buyStockButton.disableProperty().bind(cannotBuy);

    // Sell
    sellStatusLabel.textProperty().bind(viewModel.sellStatusProperty());
    sellStockDropdown.valueProperty().bindBidirectional(viewModel.sellStockSymbolProperty());
    sellStockDropdown.setItems(viewModel.getStockSymbols()); // TODO træk owned stock og brug listeners

    // Chart Stuff
    stockChart.setAnimated(false);
    stockChart.setCreateSymbols(false);
    xAxis = (NumberAxis) stockChart.getXAxis();
    yAxis = (NumberAxis) stockChart.getYAxis();

    xAxis.setAutoRanging(false);

    xAxis.setLowerBound(0);
    xAxis.setUpperBound(40);

    for (XYChart.Series<Number, Number> s : viewModel.getPiceMap().values())
    {
      attachSeries(s);
    }
    viewModel.getPiceMap()
        .addListener((MapChangeListener<? super String, ? super XYChart.Series<Number, Number>>) (change) -> {
          if (change.wasAdded())
          {
            attachSeries(change.getValueAdded());
          }
        });
  }

  private void attachSeries(XYChart.Series<Number, Number> series)
  {
    stockChart.getData().add(series);
    series.getData().addListener((ListChangeListener<XYChart.Data<Number, Number>>) c -> {
      updateXAxis();
    });
  }

  private void updateXAxis()
  {
    double window = 40;
    double maxX = 0;

    for (XYChart.Series<Number, Number> s : stockChart.getData())
    {
      if (!s.getData().isEmpty())
      {
        XYChart.Data<Number, Number> last = s.getData().getLast();
        maxX = Math.max(maxX, last.getXValue().doubleValue());
      }
    }
    if (maxX - window < 0)
      xAxis.setLowerBound(0);
    else
      xAxis.setLowerBound(maxX - window);
    if (maxX + 10 > window)
      xAxis.setUpperBound(maxX + 10);
  }

  public void handleStartGame()
  {
    viewModel.startGame();
    startGameButton.setDisable(true);
  }

  public void handleReset(ActionEvent actionEvent)
  {
    stockChart.getData().clear();
    viewModel.resetGame();
    startGameButton.setDisable(false);
  }

  public void handleBuyStock(ActionEvent actionEvent)
  {
    viewModel.buyStock();
  }

  public void handleSellStock()
  {
    viewModel.sellStock();
  }
}

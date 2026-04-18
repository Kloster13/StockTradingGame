package presentation.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import presentation.viewmodels.StockMarketViewModel;

public class StockMarketController
{
  public LineChart<Number, Number> stockChart;
  public ComboBox<String> buyStockDropdown;
  public Spinner<Integer> buyStockAmount;
  public Button buyStockButton;
  public Label buyStatusLabel;
  public ComboBox<String> sellStockDropdown;
  public Spinner<Integer> sellStockAmount;
  public Label sellStatusLabel;
  public Button sellStockButton;
  public Label bankruptLabel;
  public Label balanceLabel;
  private NumberAxis xAxis;

  private final StockMarketViewModel viewModel;

  public StockMarketController(StockMarketViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  @FXML private void initialize()
  {
    balanceLabel.textProperty().bind(viewModel.balanceProperty());
    bankruptLabel.textProperty().bind(viewModel.bankruptStatusProperty());
    //Buy
    buyStatusLabel.textProperty().bind(viewModel.buyStatusProperty());
    buyStockDropdown.valueProperty().bindBidirectional(viewModel.stockSymbolProperty());
    buyStockDropdown.setItems(viewModel.getStockSymbols());
    BooleanBinding cannotBuy = viewModel.canBuyProperty().not();
    buyStockButton.disableProperty().bind(cannotBuy);

    SpinnerValueFactory<Integer> buyStockValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
        200, 0);
    buyStockAmount.setValueFactory(buyStockValueFactory);
    buyStockAmount.setEditable(true);
    ObjectProperty<Integer> buyAmountWrapper = viewModel.buyAmountProperty().asObject();
    buyStockValueFactory.valueProperty().bindBidirectional(buyAmountWrapper);

    // Sell
    sellStatusLabel.textProperty().bind(viewModel.sellStatusProperty());
    sellStockDropdown.valueProperty().bindBidirectional(viewModel.sellStockSymbolProperty());
    sellStockDropdown.setItems(viewModel.getStockSymbols());

    SpinnerValueFactory<Integer> sellStockValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
        200, 0);
    sellStockAmount.setValueFactory(sellStockValueFactory);
    sellStockAmount.setEditable(true);
    ObjectProperty<Integer> sellAmountWrapper = viewModel.sellAmountProperty().asObject();
    sellStockValueFactory.valueProperty().bindBidirectional(sellAmountWrapper);

    // Chart Stuff
    stockChart.setAnimated(false);
    stockChart.setCreateSymbols(false);
    xAxis = (NumberAxis) stockChart.getXAxis();

    xAxis.setAutoRanging(false);
    stockChart.getData().clear();
    for (XYChart.Series<Number, Number> s : viewModel.getPriceMap().values())
    {
      attachSeries(s);
    }
    viewModel.getPriceMap()
        .addListener((MapChangeListener<? super String, ? super XYChart.Series<Number, Number>>) (change) -> {
          if (change.wasAdded())
          {
            attachSeries(change.getValueAdded());
          }
        });
  }

  public void handleBuyStock()
  {
    viewModel.buyStock();
  }

  public void handleSellStock()
  {
    viewModel.sellStock();
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
}

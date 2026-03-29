package presentation.controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import presentation.core.events.ChangeViewEvent;
import presentation.viewmodels.StockMarketViewModel;

public class StockMarketController
{
  public LineChart<Number, Number> stockChart;
  public Button startGameButton;
  private NumberAxis xAxis;
  private NumberAxis yAxis;

  private StockMarketViewModel viewModel;

  public StockMarketController(StockMarketViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  @FXML private void initialize()
  {
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
    viewModel.getPiceMap().addListener(
        (MapChangeListener<? super String, ? super XYChart.Series<Number, Number>>) (change) -> {
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
        XYChart.Data<Number, Number> last = s.getData().get(s.getData().size() - 1);
        maxX = Math.max(maxX, last.getXValue().doubleValue());
      }
    }
    if (maxX - window < 0)
      xAxis.setLowerBound(0);
    else
      xAxis.setLowerBound(maxX - window);
    if (maxX+10 > window)
      xAxis.setUpperBound(maxX+10);
  }

  public void handleStartGame()
  {
    viewModel.startGame();
    startGameButton.setDisable(true);
  }
}

package presentation.viewmodels;

import business.services.GameService;
import business.stockmarket.StockGraphDTO;
import business.stockmarket.TheStockMarket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class StockMarketViewModel implements PropertyChangeListener
{
  private final ObservableMap<String, XYChart.Series<Number, Number>> priceMap = FXCollections.observableHashMap();

  private GameService gameService;

  public StockMarketViewModel(GameService gameService)
  {
    this.gameService = gameService;
    TheStockMarket.getInstance().addListener("GraphUpdate", this);
  }

  public void addPiceData(StockGraphDTO dto)
  {
    XYChart.Series<Number, Number> series = priceMap.computeIfAbsent(dto.stockSymbol(), key -> {
      XYChart.Series<Number, Number> s = new XYChart.Series<>();
      s.setName(key);
      return s;
    });

    series.getData().add(new XYChart.Data<>(dto.secondsRun(), dto.stockPrice()));
    if (series.getData().size() > 100)
      series.getData().removeFirst();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String evtName = evt.getPropertyName();
    if (evtName.equals("GraphUpdate"))
    {
      Platform.runLater(() -> {
        addPiceData((StockGraphDTO) evt.getNewValue());
      });
    }
  }

  public void startGame()
  {
    gameService.startGame();
  }

  public void resetGame()
  {
    gameService.resetGame();
  }

  public ObservableMap<String, XYChart.Series<Number, Number>> getPiceMap()
  {
    return priceMap;
  }

}

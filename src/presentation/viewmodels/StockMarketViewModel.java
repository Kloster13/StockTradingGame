package presentation.viewmodels;

import business.services.GameService;
import business.stockmarket.StockGraphDTO;
import business.stockmarket.TheStockMarket;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class StockMarketViewModel implements PropertyChangeListener
{
  private final ObservableMap<String, XYChart.Series<Number, Number>> priceMap = FXCollections.observableHashMap();
  private final ObservableList<String> stockSymbols = FXCollections.observableArrayList();

  private GameService gameService;
  private final StringProperty buyStatus = new SimpleStringProperty("");
  private final IntegerProperty buyAmount = new SimpleIntegerProperty(0);
  private final StringProperty stockSymbol = new SimpleStringProperty();

  public StockMarketViewModel(GameService gameService)
  {
    this.gameService = gameService;
    TheStockMarket.getInstance().addListener("GraphUpdate", this);

    priceMap.addListener((MapChangeListener<String, XYChart.Series<Number, Number>>) change -> {
      if (change.wasAdded())
      {
        stockSymbols.add(change.getKey());
      }
      if (change.wasRemoved())
      {
        stockSymbols.remove(change.getKey());
      }
    });

  }

  public void addPriceData(StockGraphDTO dto)
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
        addPriceData((StockGraphDTO) evt.getNewValue());
      });
    }
  }

  public ObservableList<String> getStockSymbols()
  {
    return stockSymbols;
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

  public StringProperty buyStatusProperty()
  {
    return buyStatus;
  }

  public IntegerProperty buyAmountProperty()
  {
    return buyAmount;
  }

  public StringProperty stockSymbolProperty()
  {
    return stockSymbol;
  }
}

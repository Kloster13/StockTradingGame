package presentation.viewmodels;

import business.services.GameService;
import business.services.StockTransactionService;
import business.services.dtos.BuySellStockRequest;
import business.stockmarket.StockGraphDTO;
import business.stockmarket.TheStockMarket;
import domain.Stock;
import javafx.application.Platform;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockMarketViewModel implements PropertyChangeListener
{
  private final ObservableMap<String, XYChart.Series<Number, Number>> priceMap = FXCollections.observableHashMap();
  private final ObservableList<String> stockSymbols = FXCollections.observableArrayList();

  private GameService gameService;
  private StockTransactionService transactionService;
  private final StringProperty buyStatus = new SimpleStringProperty("");
  private final IntegerProperty buyAmount = new SimpleIntegerProperty(0);
  private final StringProperty stockSymbol = new SimpleStringProperty();
  private final BooleanProperty canBuy = new SimpleBooleanProperty(false);
  private final IntegerProperty sellAmount = new SimpleIntegerProperty(0);
  private final StringProperty sellStockSymbol = new SimpleStringProperty();
  private final StringProperty sellStatus = new SimpleStringProperty("");
  private BooleanProperty isGameRunning;

  public StockMarketViewModel(GameService gameService, StockTransactionService transactionService)
  {
    this.gameService = gameService;
    this.transactionService = transactionService;
    TheStockMarket.getInstance().addListener("GraphUpdate", this);

    stockSymbols.setAll(gameService.getAllStocks().stream().map(Stock::getSymbol).toList());

    buyAmount.addListener((obs, oldValue, newValue) -> validateStockBuy());
    stockSymbol.addListener((obs, oldValue, newValue) -> validateStockBuy());
    validateStockBuy();
    System.out.println(gameService.isGameIsRunning());
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

  private void validateStockBuy()
  {
    boolean validStock = stockSymbol.getValue() != null && !stockSymbol.getValue().isBlank();
    boolean validAmount = buyAmount.getValue() > 0;
    canBuy.set(validAmount && validStock);
  }

  public void buyStock()
  {
    try
    {
      BuySellStockRequest request = new BuySellStockRequest(1, stockSymbol.getValue(),
          buyAmount.getValue()); // TODO FIX PORTFOLIO LOGIC
      transactionService.buyStock(request);
      buyStatus.setValue("Transaction completed");
    }
    catch (Exception e)
    {
      buyStatus.setValue(e.getMessage());
    }
    System.out.println(buyAmount.getValue());
  }

  public void sellStock()
  {
    try
    {
      BuySellStockRequest request = new BuySellStockRequest(1, sellStockSymbol.getValue(),
          sellAmount.getValue());
      transactionService.sellStock(request);
      sellStatus.setValue("Transaction completed");
    }
    catch (Exception e)
    {
      sellStatus.setValue(e.getMessage());
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
    priceMap.clear();
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

  public BooleanProperty canBuyProperty()
  {
    return canBuy;
  }

  public IntegerProperty sellAmountProperty()
  {
    return sellAmount;
  }

  public StringProperty sellStockSymbolProperty()
  {
    return sellStockSymbol;
  }

  public StringProperty sellStatusProperty()
  {
    return sellStatus;
  }
}

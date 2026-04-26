package presentation.viewmodels;

import business.services.GameService;
import business.services.PortfolioService;
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
import presentation.core.ActivePortfolioCache;
import presentation.core.AppContext;
import presentation.notification.AlertAdapter;
import presentation.notification.ShowNotification;
import shared.configuration.AppConfiguration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockMarketViewModel implements PropertyChangeListener
{
  private final ObservableMap<String, XYChart.Series<Number, Number>> priceMap = FXCollections.observableHashMap();
  private final ObservableList<String> stockSymbols = FXCollections.observableArrayList();
  private ActivePortfolioCache cache;

  private final StockTransactionService transactionService;
  private final PortfolioService portfolioService;
  private final StringProperty balance = new SimpleStringProperty("");
  private final StringProperty buyStatus = new SimpleStringProperty("");
  private final IntegerProperty buyAmount = new SimpleIntegerProperty(0);
  private final StringProperty stockSymbol = new SimpleStringProperty();
  private final BooleanProperty canBuy = new SimpleBooleanProperty(false);
  private final IntegerProperty sellAmount = new SimpleIntegerProperty(0);
  private final StringProperty sellStockSymbol = new SimpleStringProperty();
  private final StringProperty sellStatus = new SimpleStringProperty("");
  private final StringProperty bankruptStatus = new SimpleStringProperty("");

  ShowNotification notifications = new AlertAdapter();
  public StockMarketViewModel(GameService gameService, StockTransactionService transactionService,
      PortfolioService portfolioService, ActivePortfolioCache cache)
  {
    this.transactionService = transactionService;
    this.portfolioService = portfolioService;
    this.cache=cache;
    TheStockMarket.getInstance().addListener("GraphUpdate", this);
    TheStockMarket.getInstance().addListener("Bankrupt", this);

    stockSymbols.setAll(gameService.getAllStocks().stream().map(Stock::getSymbol).toList());
    buyAmount.addListener((obs, oldValue, newValue) -> validateStockBuy());
    stockSymbol.addListener((obs, oldValue, newValue) -> validateStockBuy());
    updateVisualData();
    validateStockBuy();
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

  public void buyStock()
  {
    try
    {
      BuySellStockRequest request = new BuySellStockRequest(cache.getPortfolioId(),
          stockSymbol.getValue(), buyAmount.getValue());
      transactionService.buyStock(request);
      updateVisualData();
      buyStatus.setValue("Transaction completed");
    }
    catch (Exception e)
    {
      buyStatus.setValue(e.getMessage());
    }
  }

  public void sellStock()
  {
    try
    {
      BuySellStockRequest request = new BuySellStockRequest(cache.getPortfolioId(),
          sellStockSymbol.getValue(), sellAmount.getValue());
      transactionService.sellStock(request);
      updateVisualData();
      sellStatus.setValue("Transaction completed");
    }
    catch (Exception e)
    {
      sellStatus.setValue(e.getMessage());
    }
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
    if (evtName.equals("Bankrupt"))
    {
      notifications.showNotification(evt,"info",evt.getNewValue() + " went bankrupt!");
    }
  }

  private void validateStockBuy()
  {
    boolean validStock = stockSymbol.getValue() != null && !stockSymbol.getValue().isBlank();
    boolean validAmount = buyAmount.getValue() > 0;
    canBuy.set(validAmount && validStock);
  }

  private void updateVisualData()
  {
    balance.setValue(Double.toString(
        portfolioService.getPortfolioData(cache.getPortfolioId()).currentBalance()));
  }

  public ObservableList<String> getStockSymbols()
  {
    return stockSymbols;
  }

  public ObservableMap<String, XYChart.Series<Number, Number>> getPriceMap()
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

  public StringProperty bankruptStatusProperty()
  {
    return bankruptStatus;
  }

  public StringProperty balanceProperty()
  {
    return balance;
  }
}

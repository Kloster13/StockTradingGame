package presentation.viewmodels;

import business.services.PortfolioService;
import business.services.dtos.OwnedStockDTO;
import business.services.dtos.PortfolioData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.core.AppContext;

public class PortfolioViewModel
{
  private final StringProperty portfolioName = new SimpleStringProperty("");
  private final StringProperty balance = new SimpleStringProperty("");
  private final StringProperty value = new SimpleStringProperty("");

  private final ObservableList<OwnedStockDTO> portfolios = FXCollections.observableArrayList();
  private PortfolioService service;
  private PortfolioData data;

  public PortfolioViewModel(PortfolioService service)
  {
    this.service = service;
  }

  public StringProperty portfolioNameProperty()
  {
    return portfolioName;
  }

  public StringProperty balanceProperty()
  {
    return balance;
  }

  public StringProperty valueProperty()
  {
    return value;
  }

  public ObservableList<OwnedStockDTO> getPortfolios()
  {
    return portfolios;
  }

  public void refreshData()
  {
    data = service.getPortfolioData(AppContext.getAppContext().getActivePortfolio());
    portfolioName.set(data.name());
    balance.set(String.valueOf(data.currentBalance()));
    value.set(String.valueOf(data.portfolioValue()));
    portfolios.setAll(data.ownedStockInfo());
  }
}

package presentation.viewmodels;

import business.services.PortfolioService;
import business.services.dtos.OwnedStockDTO;
import business.services.dtos.PortfolioData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class PortfolioViewModel
{
  private final StringProperty balance = new SimpleStringProperty("");
  private final StringProperty value = new SimpleStringProperty("");

  private final ObservableList<OwnedStockDTO> portfolios = FXCollections.observableArrayList();
  private PortfolioService service;
  private PortfolioData data;

  public PortfolioViewModel(PortfolioService service)
  {
    this.service = service;
    data = service.getPortfolioData(1); //TODO oh oh
    balance.set(String.valueOf(data.currentBalance()));
    value.set(String.valueOf(data.portfolioValue()));
    portfolios.setAll(data.ownedStockInfo());
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
}

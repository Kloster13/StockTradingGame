package presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import presentation.core.ViewManager;
import presentation.core.events.ChangeViewEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable
{
  @FXML
  private BorderPane shell;

  public void handleStockMarket(ActionEvent evt)
  {
    Node source = (Node)evt.getSource();
    source.fireEvent(new ChangeViewEvent("StockMarketView"));
  }
  public void handleWatchPortfolio(ActionEvent evt)
  {
    Node source = (Node)evt.getSource();
    source.fireEvent(new ChangeViewEvent("PortfolioView"));
  }

  @Override public void initialize(URL location, ResourceBundle resources)
  {
    shell.addEventHandler(ChangeViewEvent.TYPE,this::changeView);
  }

  private void changeView(ChangeViewEvent evt)
  {
    ViewManager.showView(evt.getViewName());
  }
}

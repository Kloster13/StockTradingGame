package presentation.controllers;

import business.services.dtos.OwnedStockDTO;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import presentation.core.events.ChangeViewEvent;
import presentation.viewmodels.PortfolioViewModel;

public class PortfolioViewController
{
  private final PortfolioViewModel viewModel;

  public Label balanceLabel;
  public Label valueLabel;
  public TableView<OwnedStockDTO> portfolioTable;
  public TableColumn<OwnedStockDTO, String> symbolColumn;
  public TableColumn<OwnedStockDTO, Integer> ownedColumn;
  public TableColumn<OwnedStockDTO, Double> priceColumn;
  public Label portfolioNameLabel;

  public PortfolioViewController(PortfolioViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  @FXML private void initialize()
  {
    balanceLabel.textProperty().bind(viewModel.balanceProperty());
    valueLabel.textProperty().bind(viewModel.valueProperty());
    portfolioNameLabel.textProperty().bind(viewModel.portfolioNameProperty());

    symbolColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().symbol()));
    ownedColumn.setCellValueFactory(
        cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().sharesOwned()).asObject());
    priceColumn.setCellValueFactory(
        cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().currenPrice()).asObject());
    portfolioTable.setItems(viewModel.getPortfolios());
    viewModel.refreshData();
  }

  public void handleBack(ActionEvent evt)
  {
    Node source = (Node) evt.getSource();
    source.fireEvent(new ChangeViewEvent("Home"));
  }
}

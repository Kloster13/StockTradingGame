package presentation.controllers;

import business.services.dtos.OwnedStockDTO;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import persistence.interfaces.PortfolioDao;
import presentation.core.events.ChangeViewEvent;
import presentation.viewmodels.PortfolioListItem;
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

  public PortfolioViewController(PortfolioViewModel viewModel)
  {
    this.viewModel = viewModel;
  }

  @FXML private void initialize()
  {
    balanceLabel.textProperty().bind(viewModel.balanceProperty());
    valueLabel.textProperty().bind(viewModel.valueProperty());

    symbolColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().symbol()));
    ownedColumn.setCellValueFactory(
        cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().sharesOwned()).asObject());
    priceColumn.setCellValueFactory(
        cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().currenPrice()).asObject());
    portfolioTable.setItems(viewModel.getPortfolios());
  }

  public void handleBack(ActionEvent evt)
  {
    Node source = (Node) evt.getSource();
    source.fireEvent(new ChangeViewEvent("Home"));
  }
}

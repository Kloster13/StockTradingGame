package presentation.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import presentation.core.events.ChangeViewEvent;

public class PortfolioViewController
{
  public void handleBack(ActionEvent evt)
  {
    Node source = (Node)evt.getSource();
    source.fireEvent(new ChangeViewEvent("Home"));
  }
}

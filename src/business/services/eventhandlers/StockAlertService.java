package business.services.eventhandlers;

import persistence.fileimplementation.FileUnitOfWork;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockAlertService implements PropertyChangeListener
{
  private final Logger logger = Logger.getInstance();

  public StockAlertService(UnitOfWork uow)
  {
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("Reset"))
      logger.log("INFO", evt.getNewValue() + " was reset");
  }
}

package business.services;

import persistence.fileimplementation.FileUnitOfWork;
import shared.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StockAlertService implements PropertyChangeListener
{
  private FileUnitOfWork uow;
  private final Logger logger = Logger.getInstance();

  public StockAlertService(FileUnitOfWork uow)
  {
    this.uow = uow;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    // TODO later: add support for locking out/unlocking buying bankrupt stock
    if(evt.getPropertyName().equals("Reset"))
    logger.log("INFO", (String) evt.getNewValue() + " was reset");
  }
}

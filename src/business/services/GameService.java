package business.services;

import persistence.fileimplementation.FileUnitOfWork;
import persistence.fileimplementation.OwnedStockDaoFileImplementation;
import persistence.fileimplementation.PortfolioDaoFileImplementation;
import persistence.fileimplementation.StockDaoFileImplementation;
import persistence.interfaces.OwnedStockDao;
import persistence.interfaces.PortfolioDao;
import persistence.interfaces.StockDao;
import shared.logging.Logger;

public class GameService
{
  private final Logger logger = Logger.getInstance();
  private final OwnedStockDao ownedStockDao;
  private final PortfolioDao portfolioDao;
  private final StockDao stockDao;

  public GameService(FileUnitOfWork uow)
  {
    this.ownedStockDao = new OwnedStockDaoFileImplementation(uow);
    this.portfolioDao = new PortfolioDaoFileImplementation(uow);
    this.stockDao = new StockDaoFileImplementation(uow);
  }

  public void startGame(){
    // TODO think about this one same with rest
  }
}

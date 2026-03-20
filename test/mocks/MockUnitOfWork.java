package mocks;

import domain.*;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class MockUnitOfWork implements UnitOfWork
{
  private List<OwnedStock> ownedStocks;
  private List<Portfolio> portfolios;
  private List<Stock> stocks;
  private List<StockPriceHistory> stockPriceHistory;
  private List<Transaction> transactions;

  private final Logger logger = Logger.getInstance();

  public MockUnitOfWork(){}

  @Override public void begin()
  {

  }

  @Override public void commit()
  {

  }

  @Override public void rollback()
  {

  }

  @Override public void reset()
  {

  }

  public List<OwnedStock> getOwnedStocks()
  {
    if (ownedStocks == null)
    {
      ownedStocks = new ArrayList<>();
    }
    return ownedStocks;
  }

  public List<Portfolio> getPortfolios()
  {
    if (portfolios == null)
    {
      portfolios = new ArrayList<>();
    }
    return portfolios;
  }

  public List<Stock> getStocks()
  {
    if (stocks == null)
    {
      stocks = new ArrayList<>();
    }
    return stocks;
  }

  public List<StockPriceHistory> getStockPriceHistory()
  {
    if (stockPriceHistory == null)
    {
      stockPriceHistory = new ArrayList<>();
    }
    return stockPriceHistory;
  }

  public List<Transaction> getTransactions()
  {
    if (transactions == null)
    {
      transactions = new ArrayList<>();
    }
    return transactions;
  }
}

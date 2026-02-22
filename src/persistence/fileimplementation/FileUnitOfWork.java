package persistence.fileimplementation;

import domain.*;
import persistence.FileAccessException;
import persistence.interfaces.UnitOfWork;
import shared.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUnitOfWork implements UnitOfWork
{
  private List<OwnedStock> ownedStocks;
  private List<Portfolio> portfolios;
  private List<Stock> stocks;
  private List<StockPriceHistory> stockPriceHistory;
  private List<Transaction> transactions;

  private final String directoryPath;
  private static final Object FILE_WRITE_LOCK = new Object();

  public FileUnitOfWork(String directoryPath) throws FileAccessException
  {
    this.directoryPath = directoryPath;
    ensureFilesExist();
  }

  @Override public void begin()
  {
    clearLists();
  }

  @Override public void commit() throws FileAccessException
  {
    synchronized (FILE_WRITE_LOCK)
    {
      if (ownedStocks != null)
      {
        writeOwnedStockToFile();
        Logger.getInstance().log("INFO","Writing owned stock to file");
      }
      if (portfolios != null)
      {
        writePortfoliosToFile();
        Logger.getInstance().log("INFO","Writing portfolio to file");
      }
      if (stocks != null)
      {
        writeStocksToFile();
        Logger.getInstance().log("INFO","Writing stock to file");
      }
      if (stockPriceHistory != null)
      {
        writeStockPriceHistoriesToFile();
        Logger.getInstance().log("INFO","Writing stock history to file");
      }
      if (transactions != null)
      {
        writeTransactionsToFile();
        Logger.getInstance().log("INFO","Writing transactions to file");
      }
      clearLists();
      Logger.getInstance().log("INFO","Comitted");
    }
  }

  @Override public void rollback()
  {
    clearLists();
    Logger.getInstance().log("INFO", "Rolling back");
  }

  @Override public List<OwnedStock> getOwnedStocks()
  {
    if (ownedStocks == null)
    {
      ownedStocks=loadOwnedStockFromFile();
    }
    return ownedStocks;
  }

  @Override public List<Portfolio> getPortfolios()
  {
    if (portfolios == null)
    {
      portfolios=loadPortfoliosFromFile();
    }
    return portfolios;
  }

  @Override public List<Stock> getStocks()
  {
    if (stocks == null)
    {
      stocks=loadStocksFromFile();
    }
    return stocks;
  }

  @Override public List<StockPriceHistory> getStockPriceHistory()
  {
    if (stockPriceHistory == null)
    {
      stockPriceHistory=loadStockPriceHistoriesFromFile();
    }
    return stockPriceHistory;
  }

  @Override public List<Transaction> getTransactions()
  {
    if (transactions == null)
    {
      transactions=loadTransactionsFromFile();
    }
    return transactions;
  }

  private void clearLists()
  {
    if (ownedStocks != null)
    {
      ownedStocks = null;
    }
    if (portfolios != null)
    {
      portfolios = null;
    }
    if (stocks != null)
    {
      stocks = null;
    }
    if (stockPriceHistory != null)
    {
      stockPriceHistory = null;
    }
    if (transactions != null)
    {
      transactions = null;
    }
  }

  // Load
  private List<OwnedStock> loadOwnedStockFromFile()
  {
    List<String> stringList = readAllLines(getOwnedStockPath());
    List<OwnedStock> result = new ArrayList<>();
    for (String string : stringList)
    {
      result.add(ownedStockFromPSV(string));
    }
    return result;
  }

  private List<Portfolio> loadPortfoliosFromFile()
  {
    List<String> stringList = readAllLines(getPortfolioPath());
    List<Portfolio> result = new ArrayList<>();
    for (String string : stringList)
    {
      result.add(portfolioFromPSV(string));
    }
    return result;
  }

  private List<Stock> loadStocksFromFile()
  {
    List<String> stringList = readAllLines(getStockPath());
    List<Stock> result = new ArrayList<>();
    for (String string : stringList)
    {
      result.add(stockFromPSV(string));
    }
    return result;
  }

  private List<StockPriceHistory> loadStockPriceHistoriesFromFile()
  {
    List<String> stringList = readAllLines(getStockPriceHistoryPath());
    List<StockPriceHistory> result = new ArrayList<>();
    for (String string : stringList)
    {
      result.add(historyFromPSV(string));
    }
    return result;
  }

  private List<Transaction> loadTransactionsFromFile()
  {
    List<String> stringList = readAllLines(getTransactionPath());
    List<Transaction> result = new ArrayList<>();
    for (String string : stringList)
    {
      result.add(transactionFromPSV(string));
    }
    return result;
  }

  // Write to file
  private void writeOwnedStockToFile()
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(getOwnedStockPath())))
    {
      for (OwnedStock ownedStock : ownedStocks)
      {
        writer.println(ownedStockToPSV(ownedStock));
      }
    }
    catch (IOException e)
    {
      throw new FileAccessException("Failed to write Owned Stocks to file");
    }
  }

  private void writePortfoliosToFile()
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(getPortfolioPath())))
    {
      for (Portfolio portfolio : portfolios)
      {
        writer.println(portfolioToPSV(portfolio));
      }
    }
    catch (IOException e)
    {
      throw new FileAccessException("Failed to write portfolios to file");
    }
  }

  private void writeStocksToFile()
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(getStockPath())))
    {
      for (Stock stock : stocks)
      {
        writer.println(stockToPSV(stock));
      }
    }
    catch (IOException e)
    {
      throw new FileAccessException("Failed to write stocks to file");
    }
  }

  private void writeStockPriceHistoriesToFile()
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(getStockPriceHistoryPath())))
    {
      for (StockPriceHistory history : stockPriceHistory)
      {
        writer.println(stockPriceHistoryToPSV(history));
      }
    }
    catch (IOException e)
    {
      throw  new FileAccessException("Failed to write stock price histories to file");
    }
  }

  private void writeTransactionsToFile()
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(getTransactionPath())))
    {
      for (Transaction transaction : transactions)
      {
        writer.println(transactionToPSV(transaction));
      }
    }
    catch (IOException e)
    {
      throw  new FileAccessException("Failed to write transactions to file");
    }
  }

  private List<String> readAllLines(String filePath)
  {
    try
    {
      return Files.readAllLines(Paths.get(filePath));
    }
    catch (IOException e)
    {
      throw new FileAccessException("Failed to read fromm file: "+filePath);
    }
  }

  // From object to string
  private String ownedStockToPSV(OwnedStock ownedStock)
  {
    return ownedStock.getId() + "|" + ownedStock.getStockSymbol() + "|"
        + ownedStock.getNumberOfShares();
  }

  private String portfolioToPSV(Portfolio portfolio)
  {
    return portfolio.getId() + "|" + portfolio.getCurrentBalance() + "|" + portfolio.getOwnedStock()
        + "|" + portfolio.getTransactions();
  }

  private String stockToPSV(Stock stock)
  {
    return stock.getId()+"|"+ stock.getSymbol() + "|" + stock.getName() + "|" + stock.getCurrentPrice() + "|"
        + stock.getCurrentState();
  }

  private String stockPriceHistoryToPSV(StockPriceHistory history)
  {
    return history.getId() + "|" + history.getStockSymbol() + "|" + history.getPrice() + "|"
        + history.getTimestamp();
  }

  private String transactionToPSV(Transaction transaction)
  {
    return transaction.getId() + "|" + transaction.getStockSymbol() + "|" + transaction.getType()
        + "|" + transaction.getQuantity() + "|" + transaction.getPricePrShare() + "|"
        + transaction.getFee() + "|" + transaction.getTimestamp();
  }

  // From string to object
  private List<Integer> parseList(String listToParse)
  {
    return Arrays.stream(listToParse.substring(1, listToParse.length() - 1).split(","))
        .map(String::trim).map(Integer::parseInt).toList();
  }

  private OwnedStock ownedStockFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new OwnedStock(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]));
  }

  private Portfolio portfolioFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new Portfolio(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
        parseList(parts[2]), parseList(parts[3]));
  }

  private Stock stockFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new Stock(Integer.parseInt(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3]),
        parts[4]);
  }

  private StockPriceHistory historyFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new StockPriceHistory(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]),
        LocalDate.parse(parts[3]));
  }

  private Transaction transactionFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new Transaction(Integer.parseInt(parts[0]), parts[1], parts[2],
        Integer.parseInt(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]),
        LocalDate.parse(parts[6]));
  }

  private void ensureFilesExist()
  {
    if (!Files.exists(Paths.get(getOwnedStockPath())))
    {
      createFile(getOwnedStockPath());
    }
    if (!Files.exists(Paths.get(getPortfolioPath())))
    {
      createFile(getPortfolioPath());
    }
    if (!Files.exists(Paths.get(getStockPath())))
    {
      createFile(getStockPath());
    }
    if (!Files.exists(Paths.get(getStockPriceHistoryPath())))
    {
      createFile(getStockPriceHistoryPath());
    }
    if (!Files.exists(Paths.get(getTransactionPath())))
    {
      createFile(getTransactionPath());
    }
  }

  private String getOwnedStockPath()
  {
    return directoryPath + "OwnedStock.txt";
  }

  private String getPortfolioPath()
  {
    return directoryPath + "Portfolio.txt";
  }

  private String getStockPath()
  {
    return directoryPath + "Stock.txt";
  }

  private String getStockPriceHistoryPath()
  {
    return directoryPath + "StockPriceHistory.txt";
  }

  private String getTransactionPath()
  {
    return directoryPath + "Transaction.txt";
  }

  private void createFile(String filePath)
  {
    try
    {
      Files.createFile(Path.of(filePath));
      Logger.getInstance().log("INFO","No file found.... Creating new file with path: "+filePath);
    }
    catch (IOException e)
    {
      throw new FileAccessException("Could not create new file for "+filePath+"  " + e);
    }
  }
}

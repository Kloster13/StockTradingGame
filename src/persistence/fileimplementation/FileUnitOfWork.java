package persistence.fileimplementation;

import domain.*;
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

  public FileUnitOfWork(String directoryPath)
  {
    this.directoryPath = directoryPath;
    ensureFilesExist();
  }

  @Override public void begin()
  {
    clearLists();
  }

  @Override public void commit()
  {
    synchronized (FILE_WRITE_LOCK)
    {
      if (ownedStocks != null)
      {
        writeOwnedStockToFile();
      }
      if (portfolios != null)
      {
        writePortfoliosToFile();
      }
      if (stocks != null)
      {
        writeStocksToFile();
      }
      if (stockPriceHistory != null)
      {
        writeStockPriceHistoriesToFile();
      }
      if (transactions != null)
      {
        writeTransactionsToFile();
      }
      clearLists();
    }
  }

  @Override public void rollback()
  {
    clearLists();
  }

  @Override public List<OwnedStock> getOwnedStock()
  {
    if (ownedStocks == null)
    {
      return loadOwnedStockFromFile();
    }
    return ownedStocks;
  }

  @Override public List<Portfolio> getPortfolio()
  {
    if (portfolios == null)
    {
      return loadPortfoliosFromFile();
    }
    return portfolios;
  }

  @Override public List<Stock> getStock()
  {
    if (stocks == null)
    {
      return loadStocksFromFile();
    }
    return stocks;
  }

  @Override public List<StockPriceHistory> getStockPriceHistory()
  {
    if (stockPriceHistory == null)
    {
      return loadStockPriceHistoriesFromFile();
    }
    return stockPriceHistory;
  }

  @Override public List<Transaction> getTransactions()
  {
    if (transactions == null)
    {
      return loadTransactionsFromFile();
    }
    return transactions;
  }

  private void clearLists()
  {
    Logger.getInstance().log("INFO", "Clearing lists");
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
      Logger.getInstance().log("ERROR", "Failed to write owned stock to file");
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
      Logger.getInstance().log("ERROR", "Failed to write portfolios to file");
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
      Logger.getInstance().log("ERROR", "Failed to write stocks to file");
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
      Logger.getInstance().log("ERROR", "Failed to write stock price histories to file");
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
      Logger.getInstance().log("ERROR", "Failed to write transactions to file");
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
      Logger.getInstance().log("ERROR", "Failed to read from file");
      throw new RuntimeException("Failed to read fromm file: " + filePath, e);
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
    return stock.getSymbol() + "|" + stock.getName() + "|" + stock.getCurrentPrice() + "|"
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
      Logger.getInstance().log("INFO", "Created file for " + filePath);
    }
    catch (IOException e)
    {
      Logger.getInstance().log("ERROR", "Could not create new file for" + filePath);
      throw new RuntimeException("Could not create new file" + e);
    }
  }
}

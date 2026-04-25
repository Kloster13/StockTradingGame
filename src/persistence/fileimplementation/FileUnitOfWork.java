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
import java.util.Collections;
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
  private final Logger logger = Logger.getInstance();

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
        writeObjectToFile(ownedStocks, getOwnedStockPath());
        logger.log("INFO", "Writing owned stock to file");
      }
      if (portfolios != null)
      {
        writeObjectToFile(portfolios, getPortfolioPath());
        logger.log("INFO", "Writing portfolio to file");
      }
      if (stocks != null)
      {
        writeObjectToFile(stocks, getStockPath());
        logger.log("INFO", "Writing stock to file");
      }
      if (stockPriceHistory != null)
      {
        writeObjectToFile(stockPriceHistory, getStockPriceHistoryPath());
        logger.log("INFO", "Writing stock history to file");
      }
      if (transactions != null)
      {
        writeObjectToFile(transactions, getTransactionPath());
        logger.log("INFO", "Writing transactions to file");
      }
      clearLists();
      logger.log("INFO", "Comitted");
    }
  }

  @Override public void rollback()
  {
    clearLists();
    logger.log("INFO", "Rolling back");
  }

  @Override public void reset()
  {
    begin();
    portfolios = new ArrayList<>();
    ownedStocks = new ArrayList<>();
    stockPriceHistory = new ArrayList<>();
    transactions = new ArrayList<>();
    stocks = new ArrayList<>();
    commit();
  }

  public List<OwnedStock> getOwnedStocks()
  {
    if (ownedStocks == null)
    {
      ownedStocks = loadOwnedStockFromFile();
    }
    return ownedStocks;
  }

  public List<Portfolio> getPortfolios()
  {
    if (portfolios == null)
    {
      portfolios = loadPortfoliosFromFile();
    }
    return portfolios;
  }

  public List<Stock> getStocks()
  {
    if (stocks == null)
    {
      stocks = loadStocksFromFile();
    }
    return stocks;
  }

  public List<StockPriceHistory> getStockPriceHistory()
  {
    if (stockPriceHistory == null)
    {
      stockPriceHistory = loadStockPriceHistoriesFromFile();
    }
    return stockPriceHistory;
  }

  public List<Transaction> getTransactions()
  {
    if (transactions == null)
    {
      transactions = loadTransactionsFromFile();
    }
    return transactions;
  }

  private void clearLists()
  {
    ownedStocks = null;
    portfolios = null;
    stocks = null;
    stockPriceHistory = null;
    transactions = null;
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
  private void writeObjectToFile(List<?> listOfData, String path)
  {
    try (PrintWriter writer = new PrintWriter(new FileWriter(path)))
    {
      for (Object object : listOfData)
      {
        switch (object)
        {
          case Stock stock -> writer.println(stockToPSV(stock));
          case OwnedStock ownedStock -> writer.println(ownedStockToPSV(ownedStock));
          case Portfolio portfolio -> writer.println(portfolioToPSV(portfolio));
          case Transaction transaction -> writer.println(transactionToPSV(transaction));
          case StockPriceHistory history -> writer.println(stockPriceHistoryToPSV(history));
          default -> throw new FileAccessException("Failed to write to file\" + path");
        }
      }
    }
    catch (IOException e)
    {
      throw new FileAccessException("Failed to write to file" + path);
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
      throw new FileAccessException("Failed to read fromm file: " + filePath);
    }
  }

  // From object to string
  private String ownedStockToPSV(OwnedStock ownedStock)
  {
    return ownedStock.getId() + "|" + ownedStock.getPortfolioId() + "|" + ownedStock.getStockSymbol() + "|"
        + ownedStock.getNumberOfShares();
  }

  private String portfolioToPSV(Portfolio portfolio)
  {
    return portfolio.getId() + "|" + portfolio.getName() + "|" + portfolio.getCurrentBalance() + "|"
        + portfolio.getTransactions();
  }

  private String stockToPSV(Stock stock)
  {
    return stock.getId() + "|" + stock.getSymbol() + "|" + stock.getName() + "|" + stock.getCurrentPrice()
        + "|" + stock.getCurrentState();
  }

  private String stockPriceHistoryToPSV(StockPriceHistory history)
  {
    return history.getId() + "|" + history.getStockSymbol() + "|" + history.getPrice() + "|"
        + history.getTimestamp();
  }

  private String transactionToPSV(Transaction transaction)
  {
    return transaction.getId() + "|" + transaction.getStockSymbol() + "|" + transaction.getType() + "|"
        + transaction.getQuantity() + "|" + transaction.getPricePrShare() + "|" + transaction.getTimestamp()+"|"
        + transaction.getFee();
  }

  // From string to object
  private List<Integer> parseStringToList(String listToParse)
  {
    if (listToParse == null || listToParse.isBlank())
    {
      return Collections.emptyList();
    }
    String s = listToParse.trim();
    if (s.length() < 2 || s.charAt(0) != '[' || s.charAt(s.length() - 1) != ']')
    {
      return Collections.emptyList();
    }
    String inner = s.substring(1, s.length() - 1).trim();
    if (inner.isEmpty())
    {
      return Collections.emptyList();
    }
    return Arrays.stream(inner.split(",")).map(String::trim).filter(token -> !token.isEmpty())
        .map(Integer::parseInt).toList();
  }

  private OwnedStock ownedStockFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new OwnedStock(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2],
        Integer.parseInt(parts[3]));
  }

  private Portfolio portfolioFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new Portfolio(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]),
        parseStringToList(parts[3]));
  }

  private Stock stockFromPSV(String psv)
  {
    String[] parts = psv.split("\\|");
    return new Stock(Integer.parseInt(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3]), parts[4]);
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
    return new Transaction(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]),
        Double.parseDouble(parts[4]), LocalDate.parse(parts[5]), Double.parseDouble(parts[6]));
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
      logger.log("INFO", "No file found.... Creating new file with path: " + filePath);
    }
    catch (IOException e)
    {
      throw new FileAccessException("Could not create new file for " + filePath + "  " + e);
    }
  }
}

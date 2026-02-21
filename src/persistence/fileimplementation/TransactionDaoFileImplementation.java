package persistence.fileimplementation;

import domain.Transaction;
import persistence.interfaces.TransactionDao;
import shared.logging.Logger;

import java.util.List;
import java.util.Optional;

public class TransactionDaoFileImplementation implements TransactionDao
{
  private static int nextId = 0;

  private final FileUnitOfWork uow;

  public TransactionDaoFileImplementation(FileUnitOfWork uow)
  {
    this.uow = uow;
    calculateNextId();
  }

  @Override public void createTransaction(Transaction transaction)
  {
    transaction.setId(nextId);
    List<Transaction> transactions = uow.getTransactions();
    if (!transactions.contains(transaction))
      transactions.add(transaction);
    else
      Logger.getInstance().log("ERROR", "Transaction already in list");
  }

  @Override public void updateTransaction(Transaction updatedTransaction, int oldTransactionId)
  {
    Transaction oldTransaction = getTransactionById(oldTransactionId).orElseThrow(
        () -> new IllegalArgumentException("Transaction not in list"));

    updatedTransaction.setId(oldTransaction.getId());
    uow.getTransactions().remove(oldTransaction);
    uow.getTransactions().add(updatedTransaction);
  }

  @Override public Optional<Transaction> getTransactionById(int id)
  {
    for (Transaction transaction : uow.getTransactions())
    {
      if (transaction.getId() == id)
        return Optional.of(transaction);
    }
    return Optional.empty();
  }

  @Override public List<Transaction> getAllTransactions()
  {
    return List.copyOf(uow.getTransactions());
  }

  @Override public void deleteTransaction(int transactionId)
  {
    Transaction oldTransaction = getTransactionById(transactionId).orElseThrow(
        () -> new IllegalArgumentException("Transaction not in list"));
    uow.getTransactions().remove(oldTransaction);
  }

  private void calculateNextId()
  {
    int maxValue = 0;
    for (Transaction transaction : uow.getTransactions())
    {
      if (transaction.getId() > maxValue)
        maxValue = transaction.getId();
    }
    nextId = maxValue + 1;
  }
}
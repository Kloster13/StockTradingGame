package persistence.interfaces;

import domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDao
{
  void createTransaction(Transaction transaction);
  void updateTransaction(Transaction updatedTransaction, int oldTransaction);
  Optional<Transaction> getTransactionById(int id);
  List<Transaction> getAllTransactions();
  void deleteTransaction(int id);
}

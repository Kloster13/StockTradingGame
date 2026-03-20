package persistence.interfaces;

import domain.*;

import java.util.List;

public interface UnitOfWork
{
  void begin();
  void commit();
  void rollback();
  void reset();
}

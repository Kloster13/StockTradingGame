package presentation.notification;

import java.beans.PropertyChangeEvent;

public interface ShowNotification
{
  void showNotification(PropertyChangeEvent event,String level, String message);
}

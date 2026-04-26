package presentation.notification;

import external.CustomAlertBox;

import java.beans.PropertyChangeEvent;

public class AlertAdapter implements ShowNotification
{
  private CustomAlertBox alertBox;

  public AlertAdapter()
  {
    alertBox = new CustomAlertBox();
  }

  @Override public void showNotification(PropertyChangeEvent event, String level, String message)
  {
    CustomAlertBox.AlertType type = switch (level)
    {
      case "info" -> CustomAlertBox.AlertType.INFO;
      case "warning" -> CustomAlertBox.AlertType.WARNING;
      case "error" -> CustomAlertBox.AlertType.ERROR;
      default -> throw new IllegalStateException("Unexpected value: " + level);
    };
    alertBox.showAlert(message, event.getPropertyName(), type);
  }

}

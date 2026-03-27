package presentation.core.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ChangeViewEvent extends Event
{
  private String viewName;
  private String arg;

  public static final EventType<ChangeViewEvent> TYPE = new EventType<>("CHANGE_VIEW");

  public ChangeViewEvent(String viewName, String arg)
  {
    super(TYPE);
    this.viewName = viewName;
    this.arg = arg;
  }

  public ChangeViewEvent(String viewName)
  {
    this(viewName, null);
  }

  public String getViewName()
  {
    return viewName;
  }

  public String getArg()
  {
    return arg;
  }
}

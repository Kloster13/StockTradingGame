package shared.logging;

import external.FileLogOutputter;

public class FileLogAdapter implements LogOutput
{
  private FileLogOutputter fileLogOutputter;

  public FileLogAdapter()
  {
    fileLogOutputter = new FileLogOutputter("src/data/logfiles/logfile.txt", "info");
  }

  @Override public void log(String level, String message)
  {
    switch (level)
    {
      case "ERROR":
        fileLogOutputter.logError(message);
        break;
      case "WARNING":
        fileLogOutputter.logWarning(message);
        break;
      case "INFO":
        fileLogOutputter.logInfo(message);
        break;
    }
  }
}

package shared.logging;

public class Logger {
  private volatile static Logger instance;
  private LogOutput output;

  private Logger() {
    this.output = new ConsoleLogOutput();
  }

  public static synchronized Logger getInstance() {
    if (instance == null) {
      synchronized (Logger.class) { // Dobbeltcheck
        if (instance == null) {
          instance = new Logger();
        }
      }
      instance = new Logger();
    }
    return instance;
  }

  public void log(String level, String message) {
    output.log(level, message);
  }

  public void setOutput(LogOutput output) {
    this.output = output;
  }
}

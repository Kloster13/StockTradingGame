import shared.logging.Logger;

public class Test {
  public static void main(String[] args) {
    Logger logger = Logger.getInstance();
    logger.log("INFO", "Application started");
    logger.log("WARNING", "Stock not found in database");
  }
}

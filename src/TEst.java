import shared.logging.FileLogAdapter;
import shared.logging.Logger;

public class TEst
{
  public static void main(String[] args)
  {
    Logger logger = Logger.getInstance();
    logger.setOutput(new FileLogAdapter());
    logger.log("INFO","Hej");
  }
}

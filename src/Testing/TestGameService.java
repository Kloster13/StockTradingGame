package Testing;

import business.services.GameService;
import persistence.fileimplementation.FileUnitOfWork;

public class TestGameService
{
  public static void main(String[] args) throws InterruptedException
  {
    FileUnitOfWork tester = new FileUnitOfWork("src/data/testdata/");
    GameService gameService = new GameService(tester);

    gameService.startGame();
    Thread.sleep(100);
    System.out.println("jasdf");
    gameService.stopGame();
  }
}

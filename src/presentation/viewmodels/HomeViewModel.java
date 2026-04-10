package presentation.viewmodels;

import business.services.GameService;

public class HomeViewModel
{
  GameService gameService;
  public HomeViewModel(GameService gameService)
  {
    this.gameService=gameService;
  }
}

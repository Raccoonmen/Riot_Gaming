package com.riot_gaming.controller;


import com.riot_gaming.domain.Summoner;
import com.riot_gaming.service.GameDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameDataController {

  private final GameDataService gameDataService;

  public GameDataController(GameDataService gameDataService) {
    this.gameDataService = gameDataService;
  }

  @PostMapping("/gamedata")
  void searchData(
      @RequestParam(name = "gameid") String gameId) {
    gameDataService.searchSummoner(gameId);
  }
}

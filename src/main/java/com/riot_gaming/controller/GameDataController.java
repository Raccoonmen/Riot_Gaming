package com.riot_gaming.controller;


import com.riot_gaming.service.FightRecordService;
import com.riot_gaming.service.GameDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameDataController {

  private final GameDataService gameDataService;
  private final FightRecordService fightRecordService;

  public GameDataController(GameDataService gameDataService, FightRecordService fightRecordService) {
    this.gameDataService = gameDataService;
    this.fightRecordService = fightRecordService;
  }

  @PostMapping("/gamedata")
  void searchData(
      @RequestParam(name = "gameid") String gameId) {
    gameDataService.searchSummoner(gameId);
    fightRecordService.searchFightRecord(gameId);
  }
}

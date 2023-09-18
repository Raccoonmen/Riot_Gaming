package com.riot_gaming.persist.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "summoner")
@NoArgsConstructor
public class SummonerEntity {
  @Id
  private String id;

  private String account_id;

  private String puuid;
  private String name;
  private long revision_date;
  private int summoner_level;
}

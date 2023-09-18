package com.riot_gaming.service;

import com.riot_gaming.persist.Entity.SummonerEntity;
import com.riot_gaming.persist.SummonerRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FightRecordService {

  @Value("${riot.key}")
  private static String apiKey;

  private final SummonerRepository summonerRepository;

  public FightRecordService(SummonerRepository summonerRepository) {
    this.summonerRepository = summonerRepository;
  }

  public String getPuuidByName(String name) {
    SummonerEntity summonerEntity = summonerRepository.findByName(name);
    if (summonerEntity != null) {
      return summonerEntity.getPuuid();
    }
    return null; // 해당 닉네임을 찾지 못한 경우
  }


  // apiURL에서 해당 게임 아이디에 해당하는 puuid를 summonerRepository에서 가져와서 전적검색
  public String searchFightRecord(String gameId) {
    String puuid = getPuuidByName(gameId);
    String matchUrl = "https://asia.api.riotgames.com/tft/match/v1/matches/by-puuid/" + puuid
        + "/ids?start=0&count=20&api_key=" + apiKey;
    // matchUrl을 사용하여 Riot Games API 호출 및 경기 기록 가져오는 로직 구현
    // 가져온 데이터를 원하는 방식으로 처리
    try {
      URL url = new URL(matchUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      BufferedReader br;
      if (responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      }
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();
      return response.toString();
    } catch (Exception e) {
      return "failed to get response";
    }
  }


  /*
  3. 파싱된 값을 이용해서 DB에 저장한다.
  Domain에 있는 Summoner를 통해서 DB에 parse한 데이터들을 매칭해서 set 한 다음
  repository로 save해준다.
  */
  public void saveFightReocrdData(String gameId) {
    String FightRecordData = searchFightRecord(gameId);

    Map<String, Object> parseFight = parseFight(FightRecordData);

    SummonerEntity nowSummonerEntity = new SummonerEntity();
    nowSummonerEntity.setId(parseFight.get("id").toString());
    nowSummonerEntity.setAccount_id(parseFight.get("accountId").toString());
    nowSummonerEntity.setPuuid(parseFight.get("puuid").toString());
    nowSummonerEntity.setName(gameId);
    nowSummonerEntity.setRevision_date(Long.parseLong(String.valueOf(parseFight.get("revisionDate"))));
    nowSummonerEntity.setSummoner_level(
        Integer.parseInt(String.valueOf(parseFight.get("summonerLevel"))));

    summonerRepository.save(nowSummonerEntity);

  }

  /*
   2. searchData에서 실질적으로 찾은 String형태의 정보를 파싱 한다.
   파싱하는 함수를 만들어서 String을 json의 형태로 파싱 한다.
   정확하게 동작하지 않을 시를 대비아 try-catch를 사용 한다.
   성공 시에는 jsonString, 즉  JSONParser(Object타입)로 파싱 한다.
   실패 시에는 ParseException을 감지해서 exception을 throw 한다.
   */
  private Map<String, Object> parseFight(String jsonString) {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;

    try {
      jsonObject = (JSONObject) jsonParser.parse(jsonString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    Map<String, Object> resultMap = new HashMap<>();

    /*
     json안에서 사용할 정보를 빼서 resultMap에 저장하여 리턴한다.
    예를들어 id를 키, 값으로 파싱한 JsonObject에서 id값을 가지는 값을 저장한다.
     */
    resultMap.put("id", jsonObject.get("id"));
    resultMap.put("accountId", jsonObject.get("accountId"));
    resultMap.put("puuid", jsonObject.get("puuid"));
    resultMap.put("profileIconId", jsonObject.get("profileIconId"));
    resultMap.put("revisionDate", jsonObject.get("revisionDate"));
    resultMap.put("summonerLevel", jsonObject.get("summonerLevel"));
    return resultMap;
  }
}

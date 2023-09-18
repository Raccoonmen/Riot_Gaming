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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*
게임 데이터를 위한 서비스 입니다.
riot.key로 api를 통해서 서머너의 이름을 검색, 데이터를 DB에 저장합니다.
 */
@Service
public class GameDataService {

  @Value("${riot.key}")
  private String apiKey;

  @Autowired
  private RestTemplate restTemplate;

  private final SummonerRepository summonerRepository;

  public GameDataService(SummonerRepository summonerRepository) {
    this.summonerRepository = summonerRepository;
  }


  /*
  3. 파싱된 값을 이용해서 DB에 저장한다.
  Domain에 있는 Summoner를 통해서 DB에 parse한 데이터들을 매칭해서 set 한 다음
  repository로 save해준다.
  */
  public void searchSummoner(String gameId){
    String summonerData = searchSummonerData(gameId);

    Map<String, Object> parseSummoner = parseSummoner(summonerData);

    SummonerEntity nowSummonerEntity = new SummonerEntity();
    nowSummonerEntity.setId(parseSummoner.get("id").toString());
    nowSummonerEntity.setAccount_id(parseSummoner.get("accountId").toString());
    nowSummonerEntity.setPuuid(parseSummoner.get("puuid").toString());
    nowSummonerEntity.setName(gameId);
    nowSummonerEntity.setRevision_date(Long.parseLong(String.valueOf(parseSummoner.get("revisionDate"))));
    nowSummonerEntity.setSummoner_level(Integer.parseInt(String.valueOf(parseSummoner.get("summonerLevel"))));

    summonerRepository.save(nowSummonerEntity);

  }

  /*
   2. searchData에서 실질적으로 찾은 String형태의 정보를 파싱 한다.
   파싱하는 함수를 만들어서 String을 json의 형태로 파싱 한다.
   정확하게 동작하지 않을 시를 대비아 try-catch를 사용 한다.
   성공 시에는 jsonString, 즉  JSONParser(Object타입)로 파싱 한다.
   실패 시에는 ParseException을 감지해서 exception을 throw 한다.
   */
  private Map<String, Object> parseSummoner(String jsonString){
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;

    try{
      jsonObject = (JSONObject) jsonParser.parse(jsonString);
    }catch (ParseException e){
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
    resultMap.put("revisionDate",  jsonObject.get("revisionDate"));
    resultMap.put("summonerLevel", jsonObject.get("summonerLevel"));
    return resultMap;
  }


  /*
  1. 데이터를 실질적으로 찾는 부분입니다.
  완성된 apiURL을 통해서 해당 url에서 GET으로 response를 가져온다.
  그리고 해당 정보를 String으로 만들어서 리턴한다.
  */
  public String searchSummonerData(String gameId) {
    String apiUrl =
        "https://kr.api.riotgames.com/tft/summoner/v1/summoners/by-name/" + gameId + "?api_key="
            + apiKey;

    try {
      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      BufferedReader br;
      if(responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      }else {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      }
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = br.readLine()) != null){
        response.append(inputLine);
      }
      br.close();

      return response.toString();
    }catch (Exception e){
      return "failed to get response";
    }
  }

}
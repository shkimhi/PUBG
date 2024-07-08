package com.seok.pubg.service;

import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

@Service
public class PUBGApiService {

    @Value("${PUBGAPI-KEY}")
    private String API_KEY;
    public Map<String,Object> getMatchId(String platform, String playerName) throws Exception {
        String url = String.format("https://api.pubg.com/shards/%s/players?filter[playerNames]=%s", platform, playerName);
        JSONObject playerData = getJsonResponse(url);
        JSONArray matches = playerData.getJSONArray("data").getJSONObject(0)
                .getJSONObject("relationships")
                .getJSONObject("matches")
                .getJSONArray("data");

        Map<String,Object> playerInfoList = new HashMap<>();
        for (int i = 0; i < Math.min(10, matches.length()); i++) {
            String matchId = matches.getJSONObject(i).getString("id");
            JSONObject playerInfo = getMatchInfo(platform, matchId, playerName);
            playerInfoList.put("playerInfo",playerInfo);
        }

        return playerInfoList;
    }

    private JSONObject getMatchInfo(String platform, String matchId, String playerName) throws Exception {
        String url = String.format("https://api.pubg.com/shards/%s/matches/%s", platform, matchId);
        JSONObject matchData = getJsonResponse(url);

        JSONObject playerData = new JSONObject();
        JSONArray included = matchData.getJSONArray("included");

        for(int i =0; i < included.length(); i++){
            JSONObject obj = included.getJSONObject(i);
            if(obj.getString("type").equals("participant") && obj.getJSONObject("attributes").getJSONObject("stats").getString("name").equals(playerName)) {
                playerData = obj.getJSONObject("attributes").getJSONObject("stats");
                break;
            }
        }
        if(!matchData.getJSONObject("data").getJSONObject("attributes").getString("gameMode").contains("solo")) {
            Set<String> TeamInfo = getTeamInfo(matchData, playerName);
            playerData.put("TeamInfo", TeamInfo);
        }


        return playerData;
    }

    private Set<String> getTeamInfo(JSONObject matchData, String playerName) throws Exception {
        JSONArray included = matchData.getJSONArray("included");

        String playerId = null;
        String playerRosterId = null;
        Map<String, String> participantToNameMap = new HashMap<>();
        Map<String, Set<String>> rosterToParticipantsMap = new HashMap<>();

        // 참가자 정보와 로스터 매핑 구축
        for (int i = 0; i < included.length(); i++) {
            JSONObject obj = included.getJSONObject(i);
            if (obj.getString("type").equals("participant")) {
                String participantId = obj.getString("id");
                String name = obj.getJSONObject("attributes").getJSONObject("stats").getString("name");
                participantToNameMap.put(participantId, name);
                if (name.equals(playerName)) {
                    playerId = participantId;
                }
            }
        }

        // 로스터 정보 구축
        for (int i = 0; i < included.length(); i++) {
            JSONObject obj = included.getJSONObject(i);
            if (obj.getString("type").equals("roster")) {
                JSONArray participants = obj.getJSONObject("relationships").getJSONObject("participants").getJSONArray("data");
                Set<String> participantIds = new HashSet<>();
                for (int j = 0; j < participants.length(); j++) {
                    String participantId = participants.getJSONObject(j).getString("id");
                    participantIds.add(participantId);

                    if (participantId.equals(playerId)) {
                        playerRosterId = obj.getString("id");
                    }
                }
                rosterToParticipantsMap.put(obj.getString("id"), participantIds);
            }
        }

        if (playerRosterId == null) {
            throw new Exception("ID못찾음.");
        }

        // 팀원 이름 수집
        Set<String> teamMates = new HashSet<>();
        for (String participantId : rosterToParticipantsMap.get(playerRosterId)) {
            teamMates.add(participantToNameMap.get(participantId));
        }

        return teamMates;
    }

    public String getPlayerId(String platform, String playerName) throws Exception {
        String url = String.format("https://api.pubg.com/shards/%s/players?filter[playerNames]=%s", platform, playerName);
        JSONObject playerData = getJsonResponse(url);

        return playerData.getJSONArray("data").getJSONObject(0)
                .getString("id");
    }

    private JSONObject getJsonResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Accept", "application/vnd.api+json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            content.append(inputLine);
        }
        br.close();
        conn.disconnect();

        return new JSONObject(content.toString());
    }
}

package com.seok.pubg.service;

import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PUBGApiService {
    private static final String API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhOTNkMTA5MC0wYWMyLTAxM2QtNzFmNC00ZTYyYmI5Mjk3MWEiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNzE4MTgwNDc1LCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6Ii03ZTViYjNlOC1kYWRjLTQzNWYtYjU0ZS0xMGM1MzY3ZTgyODgifQ.csmnQBBHZrhRhTI1dxcMVhkz56x6l7zPACa_XQpxvgc";

    public List<Set<String>> getMatchId(String platform,String player_name) throws Exception {
        String url = String.format("https://api.pubg.com/shards/%s/players?filter[playerNames]=%s",platform, player_name);
        JSONObject playerData = getJsonResponse(url);
        JSONArray matches = playerData.getJSONArray("data").getJSONObject(0)
                .getJSONObject("relationships")
                .getJSONObject("matches")
                .getJSONArray("data");
        List<Set<String>> recentMatchTeamMates = new ArrayList<>();
        for(int i =0; i < Math.min(10, matches.length()); i++){
            String matchId = matches.getJSONObject(i).getString("id");
            Set<String> teamMates = getMatchInfo(platform, matchId, player_name);
            recentMatchTeamMates.add(teamMates);
        }
        return recentMatchTeamMates;
    }

    public Set<String> getMatchInfo(String platform, String matchId, String playerName) throws Exception {
        String url = String.format("https://api.pubg.com/shards/%s/matches/%s", platform, matchId);
        JSONObject matchData = getJsonResponse(url);
        JSONArray included = matchData.getJSONArray("included");

        String playerRosterId = null;
        for (int i = 0; i < included.length(); i++) {
            JSONObject obj = included.getJSONObject(i);
            if (obj.getString("type").equals("participant") && obj.getJSONObject("attributes").getJSONObject("stats").getString("name").equals(playerName)) {
                playerRosterId = obj.getString("id");
                break;
            }
        }

        Set<String> teamMates = new HashSet<>();
        List<String> teamMateRostId = new ArrayList<>();
        for (int i = 0; i < included.length(); i++) {
            JSONObject obj = included.getJSONObject(i);
            if (obj.getString("type").equals("roster")) {
                JSONArray teamArray = obj.getJSONObject("relationships").getJSONObject("participants").getJSONArray("data");
                JSONObject teamObj;
                for(int j =0; j < teamArray.length(); j++ ){
                     teamObj = teamArray.getJSONObject(j);
                    if(teamObj.getString("id").equals(playerRosterId)){
                        break;
                    }
                }
                for(int j =0; j < teamArray.length(); j++){
                    teamObj = teamArray.getJSONObject(j);
                    teamMateRostId.add(teamObj.getString("id"));
                }
            }
        }
        for(String id : teamMateRostId) {
            for (int i = 0; i < included.length(); i++) {
                JSONObject obj = included.getJSONObject(i);
                if (obj.getString("type").equals("participant") && obj.getString("id").equals(id)) {
                    teamMates.add(obj.getJSONObject("attributes").getJSONObject("stats").getString("name"));
                    break;
                }
            }
        }



        return teamMates;
        }

    private static JSONObject getJsonResponse(String urlString) throws Exception{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer " + API_KEY);
        conn.setRequestProperty("Accept", "application/vnd.api+json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = br.readLine()) != null){
            content.append(inputLine);
        }
        br.close();
        conn.disconnect();

        return new JSONObject(content.toString());
    }
}

/*
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                }
                return response.toString();
                }
*/

package com.seok.pubg.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@Service
public class PUBGApiService {
    public JsonNode getMatchId(String userId) throws IOException {
        URL url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]="+userId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhOTNkMTA5MC0wYWMyLTAxM2QtNzFmNC00ZTYyYmI5Mjk3MWEiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNzE4MTgwNDc1LCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6Ii03ZTViYjNlOC1kYWRjLTQzNWYtYjU0ZS0xMGM1MzY3ZTgyODgifQ.csmnQBBHZrhRhTI1dxcMVhkz56x6l7zPACa_XQpxvgc");
        conn.setRequestProperty("Accept", "application/vnd.api+json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(conn.getInputStream());
        conn.getInputStream().close();
        System.out.println(jsonNode);
        return jsonNode;
    }

    public String getMatchInfo(String matchId){
        return "1";
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

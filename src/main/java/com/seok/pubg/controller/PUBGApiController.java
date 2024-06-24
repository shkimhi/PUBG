package com.seok.pubg.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class PUBGApiController {

    @GetMapping("/api/matchid")
    public String GetMatchId() throws IOException {
        URL url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=Kimputer");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhOTNkMTA5MC0wYWMyLTAxM2QtNzFmNC00ZTYyYmI5Mjk3MWEiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNzE4MTgwNDc1LCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6Ii03ZTViYjNlOC1kYWRjLTQzNWYtYjU0ZS0xMGM1MzY3ZTgyODgifQ.csmnQBBHZrhRhTI1dxcMVhkz56x6l7zPACa_XQpxvgc");
        conn.setRequestProperty("Accept", "application/vnd.api+json");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }

    }
}

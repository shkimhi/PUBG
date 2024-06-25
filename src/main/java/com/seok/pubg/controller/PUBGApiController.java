package com.seok.pubg.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.seok.pubg.service.PUBGApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api/pubg/")
public class PUBGApiController {

    private PUBGApiService pubgApiService;

    public PUBGApiController(PUBGApiService pubgApiService) {
        this.pubgApiService = pubgApiService;
    }

    @GetMapping("/matchid/{userId}")
    public JsonNode GetMatchId(@PathVariable String userId) throws IOException {
        JsonNode matchId = pubgApiService.getMatchId(userId);
        return matchId;
    }

}

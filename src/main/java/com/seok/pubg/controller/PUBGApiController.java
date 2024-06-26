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
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/pubg/")
public class PUBGApiController {

    private PUBGApiService pubgApiService;

    public PUBGApiController(PUBGApiService pubgApiService) {
        this.pubgApiService = pubgApiService;
    }

    @GetMapping("/matchid/{platform}/{player_name}")
    public List<Set<String>> GetMatchId(@PathVariable String platform, @PathVariable String player_name) throws Exception {
        return pubgApiService.getMatchId(platform,player_name);
    }

}

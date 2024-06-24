package com.seok.pubg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String loginpage(){
        return "login";
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/match")
    public String match(){
        return "MatchHistoryDetailPage";
    }
    @RequestMapping("/match2")
    public String match2(){
        return "MatchHistoryDetailPage2";
    }
}

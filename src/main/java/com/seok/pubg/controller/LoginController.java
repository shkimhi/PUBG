package com.seok.pubg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

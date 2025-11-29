package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user/")
public class UserController {
    @GetMapping("/")
    public String home(){
        return "/home";
    }
}
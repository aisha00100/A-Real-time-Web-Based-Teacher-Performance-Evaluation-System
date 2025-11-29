package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;


@Controller
public class HomeController {
    @Autowired PasswordEncoder passwordEncoder;
  
    @Autowired
    UserRepository userRepository;



@GetMapping("/index")
public String index() {
    return  "index";
}
@GetMapping("/signin")
public String login() {
    return "login";
}
@GetMapping("/register")
public String register() {
    return "register";
}
@GetMapping("/base")
public String base() {
    return "base";
}

@PostMapping("/creatUser")
//what is modelattribute 
public String registerUser(@ModelAttribute MyUser user) {
   user.setPassword(passwordEncoder.encode(user.getPassword()));//this logic also exist in UserService so why i reapet it again here
   user.setRole(user.getRole());
   userRepository.save(user);
    return "redirect:/signin"; 
    
}
@GetMapping("/admin/")
public String admin() {
    return  "admin";
}

/* 

@GetMapping("/teacher/")
public String teacher() {
    return  "teacher";
}*/





}
package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

/*@PostMapping("/creatUser")
//what is modelattribute 
public String registerUser(@ModelAttribute MyUser user) {
   user.setPassword(passwordEncoder.encode(user.getPassword()));//this logic also exist in UserService so why i reapet it again here
   user.setRole(user.getRole());
   userRepository.save(user);
     
    return "redirect:/register";
    
}
*/
@PostMapping("/creatUser")
public String registerUser(@ModelAttribute MyUser user, RedirectAttributes ra) {

    // تحقق من اسم المستخدم
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        ra.addFlashAttribute("errorMessage", 
            "❌ اسم المستخدم \"" + user.getUsername() + "\" مستخدم بالفعل");
        return "redirect:/register";
    }

    // تحقق من البريد الإلكتروني
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        ra.addFlashAttribute("errorMessage", 
            "❌ البريد الإلكتروني \"" + user.getEmail() + "\" مسجل بالفعل");
        return "redirect:/register";
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    ra.addFlashAttribute("successMessage", 
        "✅ تم إنشاء حساب المستخدم \"" + user.getUsername() + "\" بنجاح");

    return "redirect:/register";
}




}
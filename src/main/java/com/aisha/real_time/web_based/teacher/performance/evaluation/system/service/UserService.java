package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;

@Service
public class UserService implements UserInterface{
    @Autowired
    private UserRepository userRepository;//filed
  //هذا غيرته بسبب شات جان bcy...
    @Autowired
private PasswordEncoder passwordEncoder;


     
    @Override //here is the logic business so why i didnt put the if else that is in controller (@GetMapping)
    public MyUser creatUser(MyUser user){
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());//because here i did mustake the  teacher role here by
        return userRepository.save(user);

    }
   

}
package com.aisha.real_time.web_based.teacher.performance.evaluation.system.SecurityConfig;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
       
       //Why optional?
       Optional<MyUser> user = userRepository.findByUsername(username);
       if(user.isPresent()){
        var userObj = user.get();
        //why i used here User interface from spring why i didnt used MyUser
            return User.builder()
                       .username(userObj.getUsername())
                       .password(userObj.getPassword())
                       .authorities(userObj.getRole()) 
                       .build();
        } else {
            throw new UsernameNotFoundException(username);
       }
                
   
       
    
               
    }   
}








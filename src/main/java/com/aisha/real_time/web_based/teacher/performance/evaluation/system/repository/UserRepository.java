package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;




public interface UserRepository extends JpaRepository<MyUser, Long> {
   //again what we mean by Optinal<MyUser>???!!!
    Optional<MyUser> findByUsername(String username);
     Optional<MyUser> findByEmail(String email);

}
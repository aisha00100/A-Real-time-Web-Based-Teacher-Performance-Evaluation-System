package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.Penalty;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    
   
    Optional<Penalty> findByUserAndItemName(MyUser user, String itemName);

    List<Penalty> findByUser(MyUser user);
}
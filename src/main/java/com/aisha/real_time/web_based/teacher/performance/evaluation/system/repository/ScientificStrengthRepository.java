package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificStrength;

@Repository
public interface ScientificStrengthRepository extends JpaRepository<ScientificStrength, Long> {
    
Optional<ScientificStrength> findByUserAndItemName(MyUser user, String itemName);

    List<ScientificStrength> findByUser(MyUser user);
}
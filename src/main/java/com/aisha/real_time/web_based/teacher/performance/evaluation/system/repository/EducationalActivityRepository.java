package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.EducationalActivity;

@Repository
public interface EducationalActivityRepository extends JpaRepository<EducationalActivity, Long> {

    List<EducationalActivity> findByUserId(Long userId);

    Optional<EducationalActivity> findByItemNameAndUserId(String itemName, Long userId);
}
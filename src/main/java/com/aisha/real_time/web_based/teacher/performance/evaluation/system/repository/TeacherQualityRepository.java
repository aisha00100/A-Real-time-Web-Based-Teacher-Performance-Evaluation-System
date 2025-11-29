package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;

@Repository
public interface TeacherQualityRepository extends JpaRepository<TeacherQuality, Long> {

    
  List<TeacherQuality> findByUserId(Long userId);

}



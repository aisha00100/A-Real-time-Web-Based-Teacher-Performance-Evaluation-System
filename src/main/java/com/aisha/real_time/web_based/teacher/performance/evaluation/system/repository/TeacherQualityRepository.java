package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;

@Repository
public interface TeacherQualityRepository extends JpaRepository<TeacherQuality, Long> {

    // Get all 5 items for a specific teacher by user id
    List<TeacherQuality> findByUserId(Long userId);

    // Get one specific item for a specific teacher
    Optional<TeacherQuality> findByItemNameAndUserId(String itemName, Long userId);
}




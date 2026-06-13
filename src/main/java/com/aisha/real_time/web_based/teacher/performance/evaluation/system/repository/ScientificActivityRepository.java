package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScientificActivityRepository extends JpaRepository<ScientificActivity, Long> {

    List<ScientificActivity> findByUserId(Long userId);

    Optional<ScientificActivity> findByItemNameAndUserId(String itemName, Long userId);
}
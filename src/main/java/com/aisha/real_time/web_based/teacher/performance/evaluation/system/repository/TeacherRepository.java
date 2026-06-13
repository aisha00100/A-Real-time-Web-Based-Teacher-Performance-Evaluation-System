package com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherProfile, Long> {
     Optional<TeacherProfile> findByUser(MyUser user);
    Optional<TeacherProfile> findByUserId(Long userId);
    boolean existsByUser(MyUser user);
    boolean existsByUserId(Long userId);


      // ⬇️ أضف هؤلاء فقط للمدير
    List<TeacherProfile> findByDepartment(String department);
    List<TeacherProfile> findByCollege(String college);
    List<TeacherProfile> findByUniversity(String university);
    List<TeacherProfile> findByFirstNameContaining(String name);
}
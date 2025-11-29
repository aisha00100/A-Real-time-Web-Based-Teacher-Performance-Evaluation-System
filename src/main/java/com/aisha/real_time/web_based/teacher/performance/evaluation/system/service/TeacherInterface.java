package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.util.List;
import java.util.Optional;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;

public interface TeacherInterface {
        TeacherProfile saveTeacherProfile(TeacherProfile teacherProfile);
    Optional<TeacherProfile> getTeacherProfileById(Long id);
    Optional<TeacherProfile> getTeacherProfileByUser(MyUser user);
    Optional<TeacherProfile> getTeacherProfileByUserId(Long userId);
    List<TeacherProfile> getAllTeacherProfiles();
    void deleteTeacherProfile(Long id);
    boolean existsByUser(MyUser user);
    boolean existsByUserId(Long userId);
}
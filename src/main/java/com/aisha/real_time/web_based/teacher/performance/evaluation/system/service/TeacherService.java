package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;

@Service
public class TeacherService implements  TeacherInterface  {
    public TeacherService() {
       
    }
    private  TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public TeacherProfile saveTeacherProfile(TeacherProfile teacherProfile) {
        // Add any business logic/validation here before saving
        if (teacherProfile.getEmail() != null && !teacherProfile.getEmail().isEmpty()) {
            // You can add email validation logic here
        }
        
        return teacherRepository.save(teacherProfile);
    }

    @Override
    public Optional<TeacherProfile> getTeacherProfileById(Long id) {
        return teacherRepository.findById(id);
    }

    @Override
    public Optional<TeacherProfile> getTeacherProfileByUser(MyUser user) {
        return teacherRepository.findByUser(user);
    }

    @Override
    public Optional<TeacherProfile> getTeacherProfileByUserId(Long userId) {
        return teacherRepository.findByUserId(userId);
    }

    @Override
    public List<TeacherProfile> getAllTeacherProfiles() {
        return teacherRepository.findAll();
    }

    @Override
    public void deleteTeacherProfile(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public boolean existsByUser(MyUser user) {
        return teacherRepository.existsByUser(user);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return teacherRepository.existsByUserId(userId);
    }
}

package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherQualityRepository;


@Service
public class TeacherQualityService {
    public TeacherQualityService() {
        this.fileStorageLocation = null;
     
    }
      @Autowired
    private TeacherQualityRepository repository;
    
    public List<TeacherQuality> getAll() {
        return repository.findAll();
    }

    public List<TeacherQuality> getQualitiesByUserId(Long userId) {
    return repository.findByUserId(userId);
}
    
    public TeacherQuality save(TeacherQuality quality) {
        return repository.save(quality);
    }
    private Path fileStorageLocation;
    
   public String storeFile(MultipartFile file) {
        System.out.println("🟡 بدء حفظ الملف: " + file.getOriginalFilename());
        
        try {
            Path uploadPath = Paths.get("uploads");
            System.out.println("🟡 المسار: " + uploadPath.toAbsolutePath());
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("🟡 تم إنشاء المجلد");
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(fileName);
            
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("✅ تم حفظ الملف: " + targetPath.toAbsolutePath());
            return fileName;
            
        } catch (Exception e) {
            System.out.println("❌ خطأ في حفظ الملف: " + e.getMessage());
            throw new RuntimeException("فشل في حفظ الملف: " + e.getMessage());
        }
    }
}
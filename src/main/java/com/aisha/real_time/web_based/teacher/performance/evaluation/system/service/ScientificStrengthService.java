package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificStrength;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.ScientificStrengthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScientificStrengthService {

    @Autowired
    private ScientificStrengthRepository repository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/scientific-strength/";

    public List<ScientificStrength> loadDashboard(MyUser user, TeacherProfile teacherProfile) {
        List<ScientificStrength> items = new ArrayList<>();
        
        for (String itemKey : ScientificStrength.ALL_ITEMS) {
            ScientificStrength item = repository.findByUserAndItemName(user, itemKey)
                    .orElseGet(() -> {
                        ScientificStrength newItem = new ScientificStrength(itemKey, user, teacherProfile);
                        return repository.save(newItem);
                    });
            items.add(item);
        }
        
        return items;
    }

    public void uploadFile(String itemName, MultipartFile file, MyUser user, TeacherProfile teacherProfile)
            throws IOException {
        
        ScientificStrength item = repository.findByUserAndItemName(user, itemName)
                .orElseGet(() -> new ScientificStrength(itemName, user, teacherProfile));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String uniqueFilename = user.getId() + "_" + itemName + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.write(filePath, file.getBytes());

        item.setFileName(uniqueFilename);
        repository.save(item);
    }

    public void saveGrades(MyUser targetUser, Map<String, String> allParams) {
        for (String itemKey : ScientificStrength.ALL_ITEMS) {
            String gradeParam = allParams.get("grade_" + itemKey);
            String noteParam = allParams.get("note_" + itemKey);

            ScientificStrength item = repository.findByUserAndItemName(targetUser, itemKey).orElse(null);
            if (item != null) {
                if (gradeParam != null && !gradeParam.isEmpty()) {
                    item.setGrade(Integer.parseInt(gradeParam));
                }
                if (noteParam != null) {
                    item.setNote(noteParam);
                }
                repository.save(item);
            }
        }
    }

    public int calculateTotalGrade(List<ScientificStrength> items) {
        return items.stream()
                .mapToInt(ScientificStrength::getFinalScore)
                .sum();
    }
}
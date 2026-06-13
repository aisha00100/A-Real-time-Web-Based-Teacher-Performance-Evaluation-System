package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.Penalty;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.PenaltyRepository;

@Service
public class PenaltyService {

    @Autowired
    private PenaltyRepository repository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/penalties/";

    public List<Penalty> loadDashboard(MyUser user, TeacherProfile teacherProfile) {
        List<Penalty> items = new ArrayList<>();
        
        for (String itemKey : Penalty.ALL_ITEMS) {
            Penalty item = repository.findByUserAndItemName(user, itemKey)
                    .orElseGet(() -> {
                        Penalty newItem = new Penalty(itemKey, user, teacherProfile);
                        return repository.save(newItem);
                    });
            items.add(item);
        }
        
        return items;
    }

    public void uploadFile(String itemName, MultipartFile file, MyUser user, TeacherProfile teacherProfile)
            throws IOException {
        
        Penalty item = repository.findByUserAndItemName(user, itemName)
                .orElseGet(() -> new Penalty(itemName, user, teacherProfile));

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

    public void saveDeductions(MyUser targetUser, Map<String, String> allParams) {
        for (String itemKey : Penalty.ALL_ITEMS) {
            String deductionParam = allParams.get("deduction_" + itemKey);
            String noteParam = allParams.get("note_" + itemKey);

            Penalty item = repository.findByUserAndItemName(targetUser, itemKey).orElse(null);
            if (item != null) {
                if (deductionParam != null && !deductionParam.isEmpty()) {
                    item.setDeduction(Integer.parseInt(deductionParam));
                }
                if (noteParam != null) {
                    item.setNote(noteParam);
                }
                repository.save(item);
            }
        }
    }

    public int calculateTotalDeduction(List<Penalty> items) {
        return items.stream()
                .mapToInt(Penalty::getFinalScore)
                .sum();
    }
}
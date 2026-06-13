package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.EducationalActivity;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.EducationalActivityRepository;

@Service
public class EducationalActivityService {

    @Autowired
    private EducationalActivityRepository educationalActivityRepository;

    // ─────────────────────────────────────────────────────────────────
    // 1. Load Dashboard — always returns all 5 items
    // ─────────────────────────────────────────────────────────────────
    public List<EducationalActivity> loadDashboard(MyUser user, TeacherProfile teacherProfile) {
        List<EducationalActivity> result = new ArrayList<>();

        for (String itemKey : EducationalActivity.ALL_ITEMS) {
            Optional<EducationalActivity> existing =
                    educationalActivityRepository.findByItemNameAndUserId(itemKey, user.getId());

            if (existing.isPresent()) {
                result.add(existing.get());
            } else {
                // In-memory placeholder — not saved to DB yet
                result.add(new EducationalActivity(itemKey, user, teacherProfile));
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    // 2. Handle Teacher File Upload
    //    - Saves file to static/uploads/educational-activity/
    //    - Sets grade = item's max grade automatically
    // ─────────────────────────────────────────────────────────────────
    public void uploadFile(String itemName, MultipartFile file,
                           MyUser user, TeacherProfile teacherProfile) throws IOException {

        String uploadDir = "src/main/resources/static/uploads/educational-activity/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(originalFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Optional<EducationalActivity> existing =
                educationalActivityRepository.findByItemNameAndUserId(itemName, user.getId());

        EducationalActivity record;
        if (existing.isPresent()) {
            record = existing.get();
        } else {
            record = new EducationalActivity(itemName, user, teacherProfile);
        }

        record.setFileName(originalFileName);
        // Auto set grade to max grade for this specific item
        record.setGrade(EducationalActivity.getMaxGrade(itemName));

        educationalActivityRepository.save(record);
    }

    // ─────────────────────────────────────────────────────────────────
    // 3. Save Admin Changes — grades and notes for all 5 items
    // ─────────────────────────────────────────────────────────────────
    public void saveAdminChanges(Long userId,
                                 Map<String, Integer> grades,
                                 Map<String, String> notes) {

        for (String itemKey : EducationalActivity.ALL_ITEMS) {
            Optional<EducationalActivity> existing =
                    educationalActivityRepository.findByItemNameAndUserId(itemKey, userId);

            if (existing.isPresent()) {
                EducationalActivity record = existing.get();

                if (grades != null && grades.containsKey(itemKey)) {
                    record.setGrade(grades.get(itemKey));
                }

                if (notes != null && notes.containsKey(itemKey)) {
                    record.setNote(notes.get(itemKey));
                }

                educationalActivityRepository.save(record);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // 4. Calculate total grade across all 5 items
    // ─────────────────────────────────────────────────────────────────
    public int calculateTotalGrade(List<EducationalActivity> items) {
        return items.stream()
                .mapToInt(item -> item.getGrade() != null ? item.getGrade() : 0)
                .sum();
    }
}


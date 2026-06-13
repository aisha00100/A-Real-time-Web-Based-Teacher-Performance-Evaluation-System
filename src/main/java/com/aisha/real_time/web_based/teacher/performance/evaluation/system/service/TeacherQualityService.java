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

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherQualityRepository;

@Service
public class TeacherQualityService {

    @Autowired
    private TeacherQualityRepository teacherQualityRepository;

    // ─────────────────────────────────────────────────────────────────
    // 1. Load Dashboard
    //    Always returns exactly 5 items for a teacher.
    //    If a row does not exist in DB yet, returns an empty placeholder
    //    so the view always renders all 5 rows.
    // ─────────────────────────────────────────────────────────────────
    public List<TeacherQuality> loadDashboard(MyUser user, TeacherProfile teacherProfile) {
        List<TeacherQuality> result = new ArrayList<>();

        for (String itemKey : TeacherQuality.ALL_ITEMS) {
            Optional<TeacherQuality> existing =
                    teacherQualityRepository.findByItemNameAndUserId(itemKey, user.getId());

            if (existing.isPresent()) {
                result.add(existing.get());
            } else {
                // Not saved yet — create in-memory placeholder for display only
                TeacherQuality placeholder = new TeacherQuality(itemKey, user, teacherProfile);
                result.add(placeholder);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    // 2. Handle Teacher File Upload
    //    - Saves file inside project: src/main/resources/static/uploads/teacher-quality/
    //    - Creates or updates the DB row for that item
    //    - Sets grade = 20 automatically
    // ─────────────────────────────────────────────────────────────────
    public void uploadFile(String itemName, MultipartFile file,
                           MyUser user, TeacherProfile teacherProfile) throws IOException {

        // Save file inside the project static folder
        String uploadDir = "src/main/resources/static/uploads/teacher-quality/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(originalFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create or update DB row
        Optional<TeacherQuality> existing =
                teacherQualityRepository.findByItemNameAndUserId(itemName, user.getId());

        TeacherQuality record;
        if (existing.isPresent()) {
            record = existing.get();
        } else {
            record = new TeacherQuality(itemName, user, teacherProfile);
        }

        record.setFileName(originalFileName);
        record.setGrade(20); // Auto default max grade

        teacherQualityRepository.save(record);
    }

    // ─────────────────────────────────────────────────────────────────
    // 3. Save Admin Changes
    //    Called when admin clicks "حفظ التغييرات"
    //    Updates grade and note for each of the 5 items in one batch
    // ─────────────────────────────────────────────────────────────────
    public void saveAdminChanges(Long userId,
                                 Map<String, Integer> grades,
                                 Map<String, String> notes) {

        for (String itemKey : TeacherQuality.ALL_ITEMS) {
            Optional<TeacherQuality> existing =
                    teacherQualityRepository.findByItemNameAndUserId(itemKey, userId);

            if (existing.isPresent()) {
                TeacherQuality record = existing.get();

                if (grades != null && grades.containsKey(itemKey)) {
                    record.setGrade(grades.get(itemKey));
                }

                if (notes != null && notes.containsKey(itemKey)) {
                    record.setNote(notes.get(itemKey));
                }

                teacherQualityRepository.save(record);
            }
            // If teacher never uploaded for this item, skip silently
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // 4. Calculate total grade across all 5 items
    // ─────────────────────────────────────────────────────────────────
    public int calculateTotalGrade(List<TeacherQuality> items) {
        return items.stream()
                .mapToInt(item -> item.getGrade() != null ? item.getGrade() : 0)
                .sum();
    }
}


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
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificActivity;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.ScientificActivityRepository;

@Service
public class ScientificActivityService {

    @Autowired
    private ScientificActivityRepository scientificActivityRepository;

    // ─────────────────────────────────────────────────────────────────
    // 1. Load Dashboard — always returns all 3 items
    // ─────────────────────────────────────────────────────────────────
    public List<ScientificActivity> loadDashboard(MyUser user, TeacherProfile teacherProfile) {
        List<ScientificActivity> result = new ArrayList<>();

        for (String itemKey : ScientificActivity.ALL_ITEMS) {
            Optional<ScientificActivity> existing =
                    scientificActivityRepository.findByItemNameAndUserId(itemKey, user.getId());

            if (existing.isPresent()) {
                result.add(existing.get());
            } else {
                // In-memory placeholder — not saved to DB yet
                result.add(new ScientificActivity(itemKey, user, teacherProfile));
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    // 2. Handle Teacher File Upload
    //    - Saves file to static/uploads/scientific-activity/
    //    - Sets grade = item's max grade automatically
    // ─────────────────────────────────────────────────────────────────
    public void uploadFile(String itemName, MultipartFile file,
                           MyUser user, TeacherProfile teacherProfile) throws IOException {

        String uploadDir = "src/main/resources/static/uploads/scientific-activity/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(originalFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Optional<ScientificActivity> existing =
                scientificActivityRepository.findByItemNameAndUserId(itemName, user.getId());

        ScientificActivity record;
        if (existing.isPresent()) {
            record = existing.get();
        } else {
            record = new ScientificActivity(itemName, user, teacherProfile);
        }

        record.setFileName(originalFileName);
        // Auto set grade to max grade for this specific item
        record.setGrade(ScientificActivity.getMaxGrade(itemName));

        scientificActivityRepository.save(record);
    }

    // ─────────────────────────────────────────────────────────────────
    // 3. Save Admin Changes — grades and notes for all 3 items
    // ─────────────────────────────────────────────────────────────────
    public void saveAdminChanges(Long userId,
                                 Map<String, Integer> grades,
                                 Map<String, String> notes) {

        for (String itemKey : ScientificActivity.ALL_ITEMS) {
            Optional<ScientificActivity> existing =
                    scientificActivityRepository.findByItemNameAndUserId(itemKey, userId);

            if (existing.isPresent()) {
                ScientificActivity record = existing.get();

                if (grades != null && grades.containsKey(itemKey)) {
                    record.setGrade(grades.get(itemKey));
                }

                if (notes != null && notes.containsKey(itemKey)) {
                    record.setNote(notes.get(itemKey));
                }

                scientificActivityRepository.save(record);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // 4. Calculate total grade across all 3 items
    // ─────────────────────────────────────────────────────────────────
    public int calculateTotalGrade(List<ScientificActivity> items) {
        return items.stream()
                .mapToInt(item -> item.getGrade() != null ? item.getGrade() : 0)
                .sum();
    }
}

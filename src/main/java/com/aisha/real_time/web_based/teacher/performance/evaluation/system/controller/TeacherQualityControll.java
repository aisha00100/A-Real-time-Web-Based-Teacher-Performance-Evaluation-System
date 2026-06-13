package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.TeacherQualityService;

@Controller
@RequestMapping("/teacher-quality")
public class TeacherQualityControll {

    @Autowired
    private TeacherQualityService teacherQualityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // ─────────────────────────────────────────────────────────────────
    // Helper: get MyUser from Authentication
    // Uses authentication.getName() directly — NO cast to CustomUser
    // This avoids the ClassCastException caused by DevTools classloader
    // ─────────────────────────────────────────────────────────────────
    private MyUser getLoggedInUser(Authentication authentication) {
        String username = authentication.getName(); // always safe, no cast needed
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // ─────────────────────────────────────────────────────────────────
    // GET /teacher-quality
    // Shows dashboard for both TEACHER and ADMIN
    // ─────────────────────────────────────────────────────────────────
    @GetMapping
    public String showDashboard(Model model, Authentication authentication) {

        MyUser currentUser = getLoggedInUser(authentication);
        String role = currentUser.getRole(); // "ROLE_TEACHER" or "ROLE_ADMIN"

        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        List<TeacherQuality> items =
                teacherQualityService.loadDashboard(currentUser, teacherProfile);

        int totalGrade = teacherQualityService.calculateTotalGrade(items);

        Map<String, String> arabicNames = new LinkedHashMap<>();
        for (String key : TeacherQuality.ALL_ITEMS) {
            arabicNames.put(key, TeacherQuality.getArabicName(key));
        }

        model.addAttribute("items", items);
        model.addAttribute("role", role);
        model.addAttribute("totalGrade", totalGrade);
        model.addAttribute("arabicNames", arabicNames);
        model.addAttribute("currentUser", currentUser);

        return "teacherquality";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /teacher-quality/upload/{itemName}
    // Teacher uploads a file — grade auto set to 20
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/upload/{itemName}")
    public String uploadFile(@PathVariable String itemName,
                             @RequestParam("file") MultipartFile file,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return "redirect:/teacher-quality";
        }

        try {
            teacherQualityService.uploadFile(itemName, file, currentUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }

        return "redirect:/teacher-quality";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /teacher-quality/save
    // Admin saves updated grades and notes
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String saveAdminChanges(
            @RequestParam Map<String, String> allParams,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);

        if (!"ROLE_ADMIN".equals(currentUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "غير مصرح لك بهذه العملية");
            return "redirect:/teacher-quality";
        }

        Map<String, Integer> grades = new HashMap<>();
        Map<String, String>  notes  = new HashMap<>();

        for (String key : TeacherQuality.ALL_ITEMS) {
            String gradeParam = allParams.get("grade_" + key);
            String noteParam  = allParams.get("note_"  + key);

            if (gradeParam != null && !gradeParam.isEmpty()) {
                try {
                    grades.put(key, Integer.parseInt(gradeParam));
                } catch (NumberFormatException ignored) {}
            }

            if (noteParam != null) {
                notes.put(key, noteParam);
            }
        }

        teacherQualityService.saveAdminChanges(currentUser.getId(), grades, notes);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ التغييرات بنجاح ✓");
        return "redirect:/teacher-quality";
    }
}

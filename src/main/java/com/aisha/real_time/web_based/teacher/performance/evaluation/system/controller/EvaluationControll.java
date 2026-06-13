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

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.EducationalActivity;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.Penalty;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificActivity;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.ScientificStrength;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.EducationalActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.PenaltyService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.ScientificActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.ScientificStrengthService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.TeacherQualityService;

@Controller
@RequestMapping("/evaluation")
public class EvaluationControll {

    @Autowired private TeacherQualityService teacherQualityService;
    @Autowired private ScientificActivityService scientificActivityService;
    @Autowired private EducationalActivityService educationalActivityService;
    @Autowired private ScientificStrengthService scientificStrengthService;
    @Autowired private PenaltyService penaltyService;
    @Autowired private UserRepository userRepository;
    @Autowired private TeacherRepository teacherRepository;

    // ─────────────────────────────────────────────────────────────────
    // Helper: get logged-in MyUser safely
    // ─────────────────────────────────────────────────────────────────
    private MyUser getLoggedInUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // ─────────────────────────────────────────────────────────────────
    // Helper: build Arabic names map for a given ALL_ITEMS array
    // ─────────────────────────────────────────────────────────────────
    private Map<String, String> buildArabicNames(String[] items, java.util.function.Function<String, String> nameFunc) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String key : items) {
            map.put(key, nameFunc.apply(key));
        }
        return map;
    }

    // ─────────────────────────────────────────────────────────────────
    // Helper: populate model with all 5 axes data for a given teacher
    // ─────────────────────────────────────────────────────────────────
    private void populateModel(Model model, MyUser targetUser,
                                TeacherProfile teacherProfile, String role) {

        // ── Axis 1: TeacherQuality ──
        List<TeacherQuality> axis1Items =
                teacherQualityService.loadDashboard(targetUser, teacherProfile);

        // ── Axis 2: ScientificActivity ──
        List<ScientificActivity> axis2Items =
                scientificActivityService.loadDashboard(targetUser, teacherProfile);

        // ── Axis 3: EducationalActivity ──
        List<EducationalActivity> axis3Items =
                educationalActivityService.loadDashboard(targetUser, teacherProfile);

        // ── Axis 4: ScientificStrength ──
        List<ScientificStrength> axis4Items =
                scientificStrengthService.loadDashboard(targetUser, teacherProfile);

        // ── Axis 5: Penalty ──
        List<Penalty> axis5Items =
                penaltyService.loadDashboard(targetUser, teacherProfile);

        // Arabic name maps
        Map<String, String> arabicNames1 = buildArabicNames(
                TeacherQuality.ALL_ITEMS, TeacherQuality::getArabicName);
        Map<String, String> arabicNames2 = buildArabicNames(
                ScientificActivity.ALL_ITEMS, ScientificActivity::getArabicName);
        Map<String, String> arabicNames3 = buildArabicNames(
                EducationalActivity.ALL_ITEMS, EducationalActivity::getArabicName);
        Map<String, String> arabicNames4 = buildArabicNames(
                ScientificStrength.ALL_ITEMS, ScientificStrength::getArabicName);
        Map<String, String> arabicNames5 = buildArabicNames(
                Penalty.ALL_ITEMS, Penalty::getArabicName);

        // Deduction text map for Axis 5
        Map<String, String> deductionTexts = new LinkedHashMap<>();
        for (String key : Penalty.ALL_ITEMS) {
            deductionTexts.put(key, Penalty.getDeductionText(key));
        }

        // Totals
        int total1 = teacherQualityService.calculateTotalGrade(axis1Items);
        int total2 = scientificActivityService.calculateTotalGrade(axis2Items);
        int total3 = educationalActivityService.calculateTotalGrade(axis3Items);
        int total4 = scientificStrengthService.calculateTotalGrade(axis4Items);
        // Cap total4 at 9
        if (total4 > 9) total4 = 9;
        int total5 = penaltyService.calculateTotalDeduction(axis5Items); // negative value

        // Grand total across all 5 axes
        int grandTotal = total1 + total2 + total3 + total4 + total5;

        model.addAttribute("axis1Items",    axis1Items);
        model.addAttribute("axis2Items",    axis2Items);
        model.addAttribute("axis3Items",    axis3Items);
        model.addAttribute("axis4Items",    axis4Items);
        model.addAttribute("axis5Items",    axis5Items);
        model.addAttribute("arabicNames1",  arabicNames1);
        model.addAttribute("arabicNames2",  arabicNames2);
        model.addAttribute("arabicNames3",  arabicNames3);
        model.addAttribute("arabicNames4",  arabicNames4);
        model.addAttribute("arabicNames5",  arabicNames5);
        model.addAttribute("deductionTexts", deductionTexts);
        model.addAttribute("total1",        total1);
        model.addAttribute("total2",        total2);
        model.addAttribute("total3",        total3);
        model.addAttribute("total4",        total4);
        model.addAttribute("total5",        total5);
        model.addAttribute("grandTotal",    grandTotal);
        model.addAttribute("role",          role);
        model.addAttribute("viewedTeacherId", targetUser.getId());
        model.addAttribute("teacherProfile", teacherProfile);
    }

    // ─────────────────────────────────────────────────────────────────
    // GET /evaluation
    // Teacher views their OWN evaluation page (all 3 axes)
    // ─────────────────────────────────────────────────────────────────
    @GetMapping
    public String showEvaluation(Model model, Authentication authentication) {

        MyUser currentUser = getLoggedInUser(authentication);
        String role = currentUser.getRole();

        // Admin should not use this URL — redirect to admin dashboard
        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/";
        }

        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        populateModel(model, currentUser, teacherProfile, role);
        model.addAttribute("currentUser", currentUser);

        return "evaluation"; // → templates/evaluation.html
    }

    // ─────────────────────────────────────────────────────────────────
    // GET /evaluation/view/{userId}
    // Admin views a SPECIFIC teacher's full evaluation (all 3 axes)
    // Called from the تقييم button in admin.html
    // ─────────────────────────────────────────────────────────────────
    @GetMapping("/view/{userId}")
    public String viewTeacherEvaluation(@PathVariable Long userId,
                                         Model model,
                                         Authentication authentication) {

        MyUser adminUser = getLoggedInUser(authentication);

        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        MyUser targetTeacher = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + userId));

        TeacherProfile teacherProfile = teacherRepository.findByUser(targetTeacher).orElse(null);

        // Build teacher display name
        String teacherName = targetTeacher.getUsername();
        if (teacherProfile != null && teacherProfile.getFirstName() != null) {
            teacherName = teacherProfile.getFirstName()
                    + " " + (teacherProfile.getFatherName() != null ? teacherProfile.getFatherName() : "")
                    + " " + (teacherProfile.getFamilyName() != null ? teacherProfile.getFamilyName() : "");
        }

        populateModel(model, targetTeacher, teacherProfile, adminUser.getRole());
        model.addAttribute("currentUser",  adminUser);
        model.addAttribute("teacherName",  teacherName);

        return "evaluation";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/upload/axis1/{itemName}
    // Teacher uploads file for Axis 1 item
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/upload/axis1/{itemName}")
    public String uploadAxis1(@PathVariable String itemName,
                              @RequestParam("file") MultipartFile file,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return "redirect:/evaluation";
        }
        try {
            teacherQualityService.uploadFile(itemName, file, currentUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }
        return "redirect:/evaluation";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/upload/axis2/{itemName}
    // Teacher uploads file for Axis 2 item
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/upload/axis2/{itemName}")
    public String uploadAxis2(@PathVariable String itemName,
                              @RequestParam("file") MultipartFile file,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return "redirect:/evaluation";
        }
        try {
            scientificActivityService.uploadFile(itemName, file, currentUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }
        return "redirect:/evaluation";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/upload/axis3/{itemName}
    // Teacher uploads file for Axis 3 item
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/upload/axis3/{itemName}")
    public String uploadAxis3(@PathVariable String itemName,
                              @RequestParam("file") MultipartFile file,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        TeacherProfile teacherProfile = teacherRepository.findByUser(currentUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return "redirect:/evaluation";
        }
        try {
            educationalActivityService.uploadFile(itemName, file, currentUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }
        return "redirect:/evaluation";
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/save/axis1
    // Admin saves grades + notes for Axis 1
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/save/axis1")
    public String saveAxis1(@RequestParam Map<String, String> allParams,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        MyUser adminUser = getLoggedInUser(authentication);
        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        Long viewedTeacherId = Long.parseLong(allParams.get("viewedTeacherId"));
        Map<String, Integer> grades = new HashMap<>();
        Map<String, String>  notes  = new HashMap<>();

        for (String key : TeacherQuality.ALL_ITEMS) {
            parseGradeAndNote(allParams, key, grades, notes);
        }

        teacherQualityService.saveAdminChanges(viewedTeacherId, grades, notes);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ تغييرات المحور الأول ✓");
        return "redirect:/evaluation/view/" + viewedTeacherId;
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/save/axis2
    // Admin saves grades + notes for Axis 2
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/save/axis2")
    public String saveAxis2(@RequestParam Map<String, String> allParams,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        MyUser adminUser = getLoggedInUser(authentication);
        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        Long viewedTeacherId = Long.parseLong(allParams.get("viewedTeacherId"));
        Map<String, Integer> grades = new HashMap<>();
        Map<String, String>  notes  = new HashMap<>();

        for (String key : ScientificActivity.ALL_ITEMS) {
            parseGradeAndNote(allParams, key, grades, notes);
        }

        scientificActivityService.saveAdminChanges(viewedTeacherId, grades, notes);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ تغييرات المحور الثاني ✓");
        return "redirect:/evaluation/view/" + viewedTeacherId;
    }

    // ─────────────────────────────────────────────────────────────────
    // POST /evaluation/save/axis3
    // Admin saves grades + notes for Axis 3
    // ─────────────────────────────────────────────────────────────────
    @PostMapping("/save/axis3")
    public String saveAxis3(@RequestParam Map<String, String> allParams,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        MyUser adminUser = getLoggedInUser(authentication);
        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        Long viewedTeacherId = Long.parseLong(allParams.get("viewedTeacherId"));
        Map<String, Integer> grades = new HashMap<>();
        Map<String, String>  notes  = new HashMap<>();

        for (String key : EducationalActivity.ALL_ITEMS) {
            parseGradeAndNote(allParams, key, grades, notes);
        }

        educationalActivityService.saveAdminChanges(viewedTeacherId, grades, notes);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ تغييرات المحور الثالث ✓");
        return "redirect:/evaluation/view/" + viewedTeacherId;
    }

    // ─────────────────────────────────────────────────────────────────
    // Private helper: parse grade and note from form params
    // ─────────────────────────────────────────────────────────────────
    private void parseGradeAndNote(Map<String, String> allParams, String key,
                                    Map<String, Integer> grades, Map<String, String> notes) {
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

    // ═════════════════════════════════════════════════════════════════
    // AXIS 4: Scientific Strength (مواطن القوة العلمية)
    // ═════════════════════════════════════════════════════════════════

    @PostMapping("/upload/axis4/{itemName}")
    public String uploadAxis4(@PathVariable String itemName,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam(value = "viewedTeacherId", required = false) Long viewedTeacherId,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        MyUser targetUser = (viewedTeacherId != null && "ROLE_ADMIN".equals(currentUser.getRole()))
                ? userRepository.findById(viewedTeacherId).orElse(currentUser)
                : currentUser;

        TeacherProfile teacherProfile = teacherRepository.findByUser(targetUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return redirectUrl(currentUser.getRole(), viewedTeacherId);
        }
        try {
            scientificStrengthService.uploadFile(itemName, file, targetUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }
        return redirectUrl(currentUser.getRole(), viewedTeacherId);
    }

    @PostMapping("/save/axis4")
    public String saveAxis4(@RequestParam Map<String, String> allParams,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        MyUser adminUser = getLoggedInUser(authentication);
        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        Long viewedTeacherId = Long.parseLong(allParams.get("viewedTeacherId"));
        MyUser targetTeacher = userRepository.findById(viewedTeacherId).orElse(null);

        if (targetTeacher == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "المعلم غير موجود");
            return "redirect:/admin/";
        }

        scientificStrengthService.saveGrades(targetTeacher, allParams);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ درجات المحور الرابع بنجاح ✓");

        return "redirect:/evaluation/view/" + viewedTeacherId;
    }

    // ═════════════════════════════════════════════════════════════════
    // AXIS 5: Penalties (العقوبات)
    // ═════════════════════════════════════════════════════════════════

    @PostMapping("/upload/axis5/{itemName}")
    public String uploadAxis5(@PathVariable String itemName,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam(value = "viewedTeacherId", required = false) Long viewedTeacherId,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        MyUser currentUser = getLoggedInUser(authentication);
        MyUser targetUser = (viewedTeacherId != null && "ROLE_ADMIN".equals(currentUser.getRole()))
                ? userRepository.findById(viewedTeacherId).orElse(currentUser)
                : currentUser;

        TeacherProfile teacherProfile = teacherRepository.findByUser(targetUser).orElse(null);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "يرجى اختيار ملف للرفع");
            return redirectUrl(currentUser.getRole(), viewedTeacherId);
        }
        try {
            penaltyService.uploadFile(itemName, file, targetUser, teacherProfile);
            redirectAttributes.addFlashAttribute("successMessage", "تم رفع الملف بنجاح ✓");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "حدث خطأ أثناء رفع الملف");
            e.printStackTrace();
        }
        return redirectUrl(currentUser.getRole(), viewedTeacherId);
    }

    @PostMapping("/save/axis5")
    public String saveAxis5(@RequestParam Map<String, String> allParams,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        MyUser adminUser = getLoggedInUser(authentication);
        if (!"ROLE_ADMIN".equals(adminUser.getRole())) {
            return "redirect:/evaluation";
        }

        Long viewedTeacherId = Long.parseLong(allParams.get("viewedTeacherId"));
        MyUser targetTeacher = userRepository.findById(viewedTeacherId).orElse(null);

        if (targetTeacher == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "المعلم غير موجود");
            return "redirect:/admin/";
        }

        penaltyService.saveDeductions(targetTeacher, allParams);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ خصومات المحور الخامس بنجاح ✓");

        return "redirect:/evaluation/view/" + viewedTeacherId;
    }

    // Helper method for redirect URLs
    private String redirectUrl(String role, Long viewedTeacherId) {
        if ("ROLE_ADMIN".equals(role) && viewedTeacherId != null) {
            return "redirect:/evaluation/view/" + viewedTeacherId;
        }
        return "redirect:/evaluation";
    }
}












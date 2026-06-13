/*package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.AdminService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.EducationalActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.ScientificActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.TeacherQualityService;

@Controller
public class AdminControll {

    private final AdminService adminService;
    private final TeacherQualityService teacherQualityService;
    private final ScientificActivityService scientificActivityService;
    private final EducationalActivityService educationalActivityService;
    private final TeacherRepository teacherRepository;

    public AdminControll(AdminService adminService,
                         TeacherQualityService teacherQualityService,
                         ScientificActivityService scientificActivityService,
                         EducationalActivityService educationalActivityService,
                         TeacherRepository teacherRepository) {
        this.adminService = adminService;
        this.teacherQualityService = teacherQualityService;
        this.scientificActivityService = scientificActivityService;
        this.educationalActivityService = educationalActivityService;
        this.teacherRepository = teacherRepository;
    }

    // ─────────────────────────────────────────────────────────────────
    // Helper: build grades map for all teachers
    // Returns Map<teacherProfileId, double[]>
    // double[0]=total1, [1]=total2, [2]=total3, [3]=total4, [4]=total5, [5]=finalGrade
    // ─────────────────────────────────────────────────────────────────
    private Map<Long, double[]> buildGradesMap(List<TeacherProfile> teachers) {
        Map<Long, double[]> gradesMap = new LinkedHashMap<>();
        for (TeacherProfile profile : teachers) {
            MyUser user = profile.getUser();
            if (user == null) continue;

            // loadDashboard returns the List, then pass it to calculateTotalGrade
            double t1 = teacherQualityService.calculateTotalGrade(
                            teacherQualityService.loadDashboard(user, profile));
            double t2 = scientificActivityService.calculateTotalGrade(
                            scientificActivityService.loadDashboard(user, profile));
            double t3 = educationalActivityService.calculateTotalGrade(
                            educationalActivityService.loadDashboard(user, profile));
            double t4 = 0; // axis4 — add when service ready
            double t5 = 0; // axis5 — add when service ready

            // Final grade: (t1×40%) + (t2×40%) + (t3×20%) + t4 - t5
            double finalGrade = (t1 * 0.4) + (t2 * 0.4) + (t3 * 0.2) + t4 - t5;

            gradesMap.put(profile.getId(), new double[]{t1, t2, t3, t4, t5, finalGrade});
        }
        return gradesMap;
    }

    // الصفحة الرئيسية للأدمن (startmenu)
    @GetMapping("/admin/startmenu")
    public String startMenu() {
        return "startmenu";
    }

    // قائمة المستخدمين
    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        model.addAttribute("teachers", adminService.getAllTeachers());
        return "usermanagement";
    }

    // التقارير
    @GetMapping("/admin/reports")
    public String reports(Model model) {
        List<TeacherProfile> teachers = adminService.getAllTeachers();
        model.addAttribute("teachers",          teachers);
        model.addAttribute("totalTeachers",     adminService.getTotalTeachers());
        model.addAttribute("evaluatedTeachers", adminService.getEvaluatedTeachers());
        model.addAttribute("pendingTeachers",   adminService.getPendingTeachers());
        model.addAttribute("gradesMap",         buildGradesMap(teachers));
        return "admin";
    }

    // لوحة التحكم الرئيسية
    @GetMapping("/admin/")
    public String adminDashboard(Model model) {
        List<TeacherProfile> teachers = adminService.getAllTeachers();
        model.addAttribute("teachers",          teachers);
        model.addAttribute("totalTeachers",     adminService.getTotalTeachers());
        model.addAttribute("evaluatedTeachers", adminService.getEvaluatedTeachers());
        model.addAttribute("pendingTeachers",   adminService.getPendingTeachers());
        model.addAttribute("gradesMap",         buildGradesMap(teachers));
        return "admin";
    }

    @GetMapping("/admin/teacher/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        model.addAttribute("teacherProfile", adminService.getTeacherById(id));
        model.addAttribute("evaluations",    adminService.getTeacherEvaluations(id));
        return "teacher-profile-view";
    }

    @GetMapping("/admin/teacher/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("teacherProfile", adminService.getTeacherById(id));
        model.addAttribute("teacherId",      id);
        return "teacher-form";
    }

    @PostMapping("/admin/teacher/{id}/edit")
    public String updateTeacher(@PathVariable Long id,
                                @ModelAttribute TeacherProfile updated,
                                RedirectAttributes ra) {
        adminService.updateTeacher(id, updated);
        ra.addFlashAttribute("successMessage", "✅ تم التحديث بنجاح");
        return "redirect:/admin/teacher/" + id;
    }

    @PostMapping("/admin/teacher/{id}/delete")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes ra) {
        adminService.deleteTeacher(id);
        ra.addFlashAttribute("successMessage", "✅ تم الحذف بنجاح");
        return "redirect:/admin/";
    }
}*/

package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.AdminService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.EducationalActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.ScientificActivityService;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.TeacherQualityService;

@Controller
public class AdminControll {

    private final AdminService adminService;
    private final TeacherQualityService teacherQualityService;
    private final ScientificActivityService scientificActivityService;
    private final EducationalActivityService educationalActivityService;
    private final TeacherRepository teacherRepository;

    public AdminControll(AdminService adminService,
                         TeacherQualityService teacherQualityService,
                         ScientificActivityService scientificActivityService,
                         EducationalActivityService educationalActivityService,
                         TeacherRepository teacherRepository) {
        this.adminService = adminService;
        this.teacherQualityService = teacherQualityService;
        this.scientificActivityService = scientificActivityService;
        this.educationalActivityService = educationalActivityService;
        this.teacherRepository = teacherRepository;
    }

    // ─────────────────────────────────────────────────────────────────
    // Helper: build grades map for all teachers
    // Returns Map<teacherProfileId, double[]>
    // double[0]=total1, [1]=total2, [2]=total3, [3]=total4, [4]=total5, [5]=finalGrade
    // ─────────────────────────────────────────────────────────────────
    private Map<Long, double[]> buildGradesMap(List<TeacherProfile> teachers) {
        Map<Long, double[]> gradesMap = new LinkedHashMap<>();
        for (TeacherProfile profile : teachers) {
            MyUser user = profile.getUser();
            if (user == null) continue;

            // loadDashboard returns the List, then pass it to calculateTotalGrade
            double t1 = teacherQualityService.calculateTotalGrade(
                            teacherQualityService.loadDashboard(user, profile));
            double t2 = scientificActivityService.calculateTotalGrade(
                            scientificActivityService.loadDashboard(user, profile));
            double t3 = educationalActivityService.calculateTotalGrade(
                            educationalActivityService.loadDashboard(user, profile));
            double t4 = 0; // axis4 — add when service ready
            double t5 = 0; // axis5 — add when service ready

            // Final grade: (t1×40%) + (t2×40%) + (t3×20%) + t4 - t5
            double finalGrade = (t1 * 0.4) + (t2 * 0.4) + (t3 * 0.2) + t4 - t5;

            gradesMap.put(profile.getId(), new double[]{t1, t2, t3, t4, t5, finalGrade});
        }
        return gradesMap;
    }

    // الصفحة الرئيسية للأدمن (startmenu)
    @GetMapping("/admin/startmenu")
    public String startMenu() {
        return "startmenu";
    }

    // قائمة المستخدمين
    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        model.addAttribute("teachers", adminService.getAllTeachers());
        return "usermanagement";
    }

    // التقارير
    @GetMapping("/admin/reports")
    public String reports(Model model) {
        List<TeacherProfile> teachers = adminService.getAllTeachers();
        model.addAttribute("teachers",          teachers);
        model.addAttribute("totalTeachers",     adminService.getTotalTeachers());
        model.addAttribute("evaluatedTeachers", adminService.getEvaluatedTeachers());
        model.addAttribute("pendingTeachers",   adminService.getPendingTeachers());
        model.addAttribute("gradesMap",         buildGradesMap(teachers));
        return "admin";
    }

    // لوحة التحكم الرئيسية
    @GetMapping("/admin/")
    public String adminDashboard(Model model) {
        List<TeacherProfile> teachers = adminService.getAllTeachers();
        model.addAttribute("teachers",          teachers);
        model.addAttribute("totalTeachers",     adminService.getTotalTeachers());
        model.addAttribute("evaluatedTeachers", adminService.getEvaluatedTeachers());
        model.addAttribute("pendingTeachers",   adminService.getPendingTeachers());
        model.addAttribute("gradesMap",         buildGradesMap(teachers));
        return "admin";
    }

    @GetMapping("/admin/teacher/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        model.addAttribute("teacherProfile", adminService.getTeacherById(id));
        model.addAttribute("evaluations",    adminService.getTeacherEvaluations(id));
        return "teacher-profile-view";
    }

    @GetMapping("/admin/teacher/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("teacherProfile", adminService.getTeacherById(id));
        model.addAttribute("teacherId",      id);
        return "teacher-form";
    }

    @PostMapping("/admin/teacher/{id}/edit")
    public String updateTeacher(@PathVariable Long id,
                                @ModelAttribute TeacherProfile updated,
                                RedirectAttributes ra) {
        adminService.updateTeacher(id, updated);
        ra.addFlashAttribute("successMessage", "✅ تم التحديث بنجاح");
        return "redirect:/admin/teacher/" + id;
    }

    @PostMapping("/admin/teacher/{id}/delete")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes ra) {
        adminService.deleteTeacher(id);
        ra.addFlashAttribute("successMessage", "✅ تم حذف المستخدم بنجاح");
        return "redirect:/admin/users";
    }
}
package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ⬅️ ناقص

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.service.TeacherQualityService;

@Controller
public class TeacherQualityControll {

@Autowired
    private UserRepository myUserRepository; // ⭐ إضافة هذا
   @Autowired
    private TeacherQualityService teacherQualityService;
    
    @GetMapping("/axis")
    public String axisPage(Model model) {
         MyUser currentUser = getCurrentUser();
        List<TeacherQuality> qualities = teacherQualityService.getAll();
        TeacherQuality qualityData = qualities.isEmpty() ? new TeacherQuality() : qualities.get(0);
        model.addAttribute("quality", new TeacherQuality()); // ⭐ نموذج جديد
        
        
         // ⭐ إضافة قائمة المستخدمين للربط
        List<MyUser> users = myUserRepository.findAll();
        return "teacherquality";
    }


@PostMapping("/axis/save")
    public String saveQuality(@ModelAttribute TeacherQuality quality, 
                             @RequestParam Long userId, // ⭐ أخذ userId من النموذج
                             RedirectAttributes redirectAttributes) {
        try {
MyUser currentUser = getCurrentUser();
 //MyUser currentUser = getCurrentUser();
            quality.setUser(currentUser);
           // quality.calculateFinalScore();

            // البحث عن المستخدم
            MyUser user = myUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
            
            // ربط التقييم بالمستخدم ⭐
            quality.setUser(user);
            quality.calculateFinalScore(); // حساب الدرجة النهائية
            
            teacherQualityService.save(quality);
            redirectAttributes.addFlashAttribute("successMessage", "✅ تم حفظ التقييم بنجاح!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ خطأ في حفظ التقييم: " + e.getMessage());
        }
        return "redirect:/axis";
    }







    // ⬇️ method واحد فقط لرفع الملفات
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        teacherQualityService.storeFile(file);
        return "redirect:/axis";
    }










  private MyUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
    }












}


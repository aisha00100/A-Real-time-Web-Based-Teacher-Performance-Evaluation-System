package com.aisha.real_time.web_based.teacher.performance.evaluation.system.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
 
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;

import jakarta.validation.Valid;



@Controller
public class TeacherController {

       private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public TeacherController(UserRepository userRepository,
                             TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

@GetMapping("/teacher/")
public String teacher() {
    return  "teacher";
}

   @GetMapping("/teacher/form")
   public String teacherForm(Model model, Principal principal) {

String username = principal.getName();
        MyUser user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            // حالة نادرة: المستخدم غير موجود في قاعدة البيانات
            return "redirect:/login";
        }

        // حاول إيجاد ملف موجود للمستخدم
        Optional<TeacherProfile> existing = teacherRepository.findByUser(user);

        if (existing.isPresent()) {
            // إذا أردت السماح بالتعديل بدل إعادة التوجيه: استخدم model.addAttribute("teacherProfile", existing.get()); return "teacher-form";
            return "redirect:/teacher/profile"; // ملف وجاهز — اذهب لصفحة العرض
        }

        // لا يوجد ملف بعد -> أرسل كائن جديد للفورم (th:object="${teacherProfile}")
        TeacherProfile profile = new TeacherProfile();
        profile.setUser(user); // ربطها بالمستخدم قبل الإرسال (آمن)
        model.addAttribute("teacherProfile", profile);
        return "teacher-form"; // اسم قالب Thymeleaf عندك (تأكد أن الملف templates/teacher-form.html موجود)
    }

    /**
     * معالجة إرسال الفورم (POST).
     * نتحقق من الصحة (@Valid) ونتأكد أننا نربط السجل بالمستخدم الحالي قبل الحفظ.
     */
    @PostMapping("/teacher/form")
    public String submitTeacherForm(
            @Valid @ModelAttribute("teacherProfile") TeacherProfile teacherProfile,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        String username = principal.getName();
        MyUser user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // تحقق من الأخطاء في الإدخال
        if (bindingResult.hasErrors()) {
            // أعد الفورم مع الأخطاء ليعرضها Thymeleaf
            model.addAttribute("teacherProfile", teacherProfile);
            return "teacher-form";
        }

        // اجعل الحقل user يتم ضبطه دائماً من جهة الخادم لتفادي تزوير المعرف
        teacherProfile.setUser(user);

        // احفظ الملف الشخصي
        teacherRepository.save(teacherProfile);

        // إذا أردت تخزين علم بأن المستخدم أكمل الملف (مفيد لإعادة التوجيه عند تسجيل الدخول)
        // إذا كان MyUser يحتوي على حقل profileCompleted ففعّل التالي:
        // user.setProfileCompleted(true);
        // userRepository.save(user);

        return "redirect:/teacher/profile"; // بعد الحفظ اعرض الصفحة الشخصية
    }





 @GetMapping("/teacher/profile")
   public String teacherProfile(Model model, Principal principal) {

    String username = principal.getName();
        MyUser user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        Optional<TeacherProfile> profile = teacherRepository.findByUser(user);
        if (profile.isEmpty()) {
            // إذا لم يعثر على ملف، أعد التوجيه لنموذج الإنشاء
            return "redirect:/teacher/form";
        }

        model.addAttribute("teacherProfile", profile.get());
        return "teacher-profile-view"; // قالب لعرض معلومات المعلم (templates/teacher-profile-view.html)
    
       
   }







    

   
   
    
    
}
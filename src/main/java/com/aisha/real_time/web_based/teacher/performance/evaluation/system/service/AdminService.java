/*package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherQualityRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;

@Service
public class AdminService {

    private final TeacherRepository teacherRepository;
    private final TeacherQualityRepository teacherQualityRepository;
    private final UserRepository userRepository;

    public AdminService(TeacherRepository teacherRepository,
                        TeacherQualityRepository teacherQualityRepository,
                        UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.teacherQualityRepository = teacherQualityRepository;
        this.userRepository = userRepository;
    }

    // ===== جلب كل المعلمين =====
    public List<TeacherProfile> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // ===== جلب معلم واحد =====
    public TeacherProfile getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المعلم غير موجود"));
    }

    // ===== حذف معلم =====
   // public void deleteTeacher(Long id) {
     //   if (!teacherRepository.existsById(id)) {
       //     throw new RuntimeException("المعلم غير موجود");
        //}
        //teacherRepository.deleteById(id);
    //}













public void deleteTeacher(Long id) {
    TeacherProfile teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("المعلم غير موجود"));

    // 1. افصل العلاقة مع MyUser أولاً
    MyUser user = teacher.getUser();
    if (user != null) {
        user.setTeacherProfile(null);
        teacher.setUser(null);
    }

    // 2. احذف TeacherProfile
    // TeacherQuality ستحذف تلقائياً بسبب cascade = CascadeType.ALL
    teacherRepository.delete(teacher);
}















    // ===== تعديل معلم =====
    public void updateTeacher(Long id, TeacherProfile updatedData) {
        TeacherProfile existing = getTeacherById(id);

        existing.setFirstName(updatedData.getFirstName());
        existing.setFatherName(updatedData.getFatherName());
        existing.setGrandfatherName(updatedData.getGrandfatherName());
        existing.setFamilyName(updatedData.getFamilyName());
        existing.setUniversity(updatedData.getUniversity());
        existing.setCollege(updatedData.getCollege());
        existing.setDepartment(updatedData.getDepartment());
        existing.setAcademicTitle(updatedData.getAcademicTitle());
        existing.setCertificateType(updatedData.getCertificateType());
        existing.setGeneralSpecialization(updatedData.getGeneralSpecialization());
        existing.setDetailedSpecialization(updatedData.getDetailedSpecialization());
        existing.setEmail(updatedData.getEmail());

        teacherRepository.save(existing);
    }

    // ===== جلب تقييمات معلم =====
    public List<TeacherQuality> getTeacherEvaluations(Long teacherId) {
        TeacherProfile teacher = getTeacherById(teacherId);
        return teacherQualityRepository.findByUserId(teacher.getUser().getId());
    }

    // ===== إحصائيات الداشبورد =====
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    public long getEvaluatedTeachers() {
        return teacherRepository.findAll().stream()
                .filter(t -> !t.getTeacherQualities().isEmpty())
                .count();
    }

    public long getPendingTeachers() {
        return getTotalTeachers() - getEvaluatedTeachers();
    }
}*/

package com.aisha.real_time.web_based.teacher.performance.evaluation.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.MyUser;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherProfile;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.model.TeacherQuality;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.EducationalActivityRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.PenaltyRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.ScientificActivityRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.ScientificStrengthRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherQualityRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.TeacherRepository;
import com.aisha.real_time.web_based.teacher.performance.evaluation.system.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AdminService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TeacherRepository            teacherRepository;
    private final TeacherQualityRepository     teacherQualityRepository;
    private final ScientificActivityRepository scientificActivityRepository;
    private final EducationalActivityRepository educationalActivityRepository;
    private final ScientificStrengthRepository scientificStrengthRepository;
    private final PenaltyRepository            penaltyRepository;
    private final UserRepository               userRepository;

    public AdminService(TeacherRepository teacherRepository,
                        TeacherQualityRepository teacherQualityRepository,
                        ScientificActivityRepository scientificActivityRepository,
                        EducationalActivityRepository educationalActivityRepository,
                        ScientificStrengthRepository scientificStrengthRepository,
                        PenaltyRepository penaltyRepository,
                        UserRepository userRepository) {
        this.teacherRepository             = teacherRepository;
        this.teacherQualityRepository      = teacherQualityRepository;
        this.scientificActivityRepository  = scientificActivityRepository;
        this.educationalActivityRepository = educationalActivityRepository;
        this.scientificStrengthRepository  = scientificStrengthRepository;
        this.penaltyRepository             = penaltyRepository;
        this.userRepository                = userRepository;
    }

    // ===== جلب كل المعلمين =====
    public List<TeacherProfile> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // ===== جلب معلم واحد =====
    public TeacherProfile getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المعلم غير موجود"));
    }

    // ===== حذف معلم مع كل بياناته =====
    @Transactional
    public void deleteTeacher(Long id) {
        TeacherProfile teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المعلم غير موجود"));

        MyUser user = teacher.getUser();

        if (user != null) {
            Long userId = user.getId();

            // 1. احذف سجلات المحور الأول (teacher_quality)
            teacherQualityRepository.deleteAll(
                teacherQualityRepository.findByUserId(userId));

            // 1b. احذف جدول axis_first القديم مباشرة بـ SQL (legacy table)
            entityManager.createNativeQuery("DELETE FROM axis_first WHERE user_id = :uid")
                         .setParameter("uid", userId)
                         .executeUpdate();

            // 2. احذف سجلات المحور الثاني (scientific_activity)
            scientificActivityRepository.deleteAll(
                scientificActivityRepository.findByUserId(userId));

            // 3. احذف سجلات المحور الثالث (educational_activity)
            educationalActivityRepository.deleteAll(
                educationalActivityRepository.findByUserId(userId));

            // 4. احذف سجلات المحور الرابع (scientific_strength)
            scientificStrengthRepository.deleteAll(
                scientificStrengthRepository.findByUser(user));

            // 5. احذف سجلات المحور الخامس (penalty)
            penaltyRepository.deleteAll(
                penaltyRepository.findByUser(user));

            // 6. افصل العلاقة بين المستخدم والملف الشخصي
            user.setTeacherProfile(null);
            teacher.setUser(null);
            userRepository.save(user);
        }

        // 7. احذف الملف الشخصي
        teacherRepository.delete(teacher);

        // 8. احذف حساب المستخدم
        if (user != null) {
            userRepository.delete(user);
        }
    }

    // ===== تعديل معلم =====
    public void updateTeacher(Long id, TeacherProfile updatedData) {
        TeacherProfile existing = getTeacherById(id);

        existing.setFirstName(updatedData.getFirstName());
        existing.setFatherName(updatedData.getFatherName());
        existing.setGrandfatherName(updatedData.getGrandfatherName());
        existing.setFamilyName(updatedData.getFamilyName());
        existing.setUniversity(updatedData.getUniversity());
        existing.setCollege(updatedData.getCollege());
        existing.setDepartment(updatedData.getDepartment());
        existing.setAcademicTitle(updatedData.getAcademicTitle());
        existing.setCertificateType(updatedData.getCertificateType());
        existing.setGeneralSpecialization(updatedData.getGeneralSpecialization());
        existing.setDetailedSpecialization(updatedData.getDetailedSpecialization());
        existing.setEmail(updatedData.getEmail());

        teacherRepository.save(existing);
    }

    // ===== جلب تقييمات معلم =====
    public List<TeacherQuality> getTeacherEvaluations(Long teacherId) {
        TeacherProfile teacher = getTeacherById(teacherId);
        return teacherQualityRepository.findByUserId(teacher.getUser().getId());
    }

    // ===== إحصائيات الداشبورد =====
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    public long getEvaluatedTeachers() {
        return teacherRepository.findAll().stream()
                .filter(t -> !t.getTeacherQualities().isEmpty())
                .count();
    }

    public long getPendingTeachers() {
        return getTotalTeachers() - getEvaluatedTeachers();
    }
}
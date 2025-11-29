package com.aisha.real_time.web_based.teacher.performance.evaluation.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "axis_first")  
public class TeacherQuality {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // الفقرة 1: المقررات والحقيبة التدريسية
    @Column(name = "courses_and_teaching_portfolio")
    private int coursesAndTeachingPortfolio;  // رقمي ليس نص
    
    // الفقرة 2: إدارة الصف والعلاقة مع الطلبة
    @Column(name = "classroom_management_and_student_relations")
    private int classroomManagementAndStudentRelations;
    
    // الفقرة 3: التعليم المدمج
    @Column(name = "blended_learning")
    private int blendedLearning;
    
    // الفقرة 4: وصف المقرر وأساليب التقييم
    @Column(name = "course_description_and_assessment_methods")
    private int courseDescriptionAndAssessmentMethods;
    
    // الفقرة 5: الالتزام الوظيفي
    @Column(name = "professional_commitment")
    private int professionalCommitment;
    
    // الفقرة 6: الدرجة النهائية (تحسب تلقائياً)
    @Column(name = "final_score")
    private int finalScore;

    public TeacherQuality(int blendedLearning, int classroomManagementAndStudentRelations, int courseDescriptionAndAssessmentMethods, int coursesAndTeachingPortfolio, int finalScore, Long id, int professionalCommitment) {
        this.blendedLearning = blendedLearning;
        this.classroomManagementAndStudentRelations = classroomManagementAndStudentRelations;
        this.courseDescriptionAndAssessmentMethods = courseDescriptionAndAssessmentMethods;
        this.coursesAndTeachingPortfolio = coursesAndTeachingPortfolio;
        this.finalScore = finalScore;
        this.id = id;
        this.professionalCommitment = professionalCommitment;
    }











    // ⭐ ⭐ ⭐ تأكد من وجود هذا الحقل ⭐ ⭐ ⭐
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUser user;  // هذا الحقل ضروري

    // ⭐ تأكد من وجود الـ Getter و Setter له
    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
















 @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_profile_id")
    private TeacherProfile teacherProfile;
    
    // المنشئات المعدلة
    public TeacherQuality() {}
    
    public TeacherQuality(int blendedLearning, int classroomManagementAndStudentRelations, 
                         int courseDescriptionAndAssessmentMethods, int coursesAndTeachingPortfolio, 
                         int finalScore, Long id, int professionalCommitment, 
                         TeacherProfile teacherProfile) {
        this.blendedLearning = blendedLearning;
        this.classroomManagementAndStudentRelations = classroomManagementAndStudentRelations;
        this.courseDescriptionAndAssessmentMethods = courseDescriptionAndAssessmentMethods;
        this.coursesAndTeachingPortfolio = coursesAndTeachingPortfolio;
        this.finalScore = finalScore;
        this.id = id;
        this.professionalCommitment = professionalCommitment;
        this.teacherProfile = teacherProfile;
    }
    
    // الـ Getters/Setters
    public TeacherProfile getTeacherProfile() {
        return teacherProfile;
    }
    
    public void setTeacherProfile(TeacherProfile teacherProfile) {
        this.teacherProfile = teacherProfile;
    }

      public void setId(Long id) {
        this.id = id;
    }
    public void setCoursesAndTeachingPortfolio(int coursesAndTeachingPortfolio) {
        this.coursesAndTeachingPortfolio = coursesAndTeachingPortfolio;
    }

    public void setClassroomManagementAndStudentRelations(int classroomManagementAndStudentRelations) {
        this.classroomManagementAndStudentRelations = classroomManagementAndStudentRelations;
    }

    public void setBlendedLearning(int blendedLearning) {
        this.blendedLearning = blendedLearning;
    }

    public void setCourseDescriptionAndAssessmentMethods(int courseDescriptionAndAssessmentMethods) {
        this.courseDescriptionAndAssessmentMethods = courseDescriptionAndAssessmentMethods;
    }

    public void setProfessionalCommitment(int professionalCommitment) {
        this.professionalCommitment = professionalCommitment;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    // ========== Methods إضافية ==========
    
    // طريقة لحساب الدرجة النهائية تلقائياً
    public void calculateFinalScore() {
        this.finalScore = this.coursesAndTeachingPortfolio + 
                         this.classroomManagementAndStudentRelations + 
                         this.blendedLearning + 
                         this.courseDescriptionAndAssessmentMethods + 
                         this.professionalCommitment;
    }

    // طريقة للحصول على التقييم النصي
    public String getGrade() {
        if (finalScore >= 90) return "ممتاز";
        else if (finalScore >= 80) return "جيد جداً";
        else if (finalScore >= 70) return "جيد";
        else if (finalScore >= 60) return "مقبول";
        else return "ضعيف";
    }

    @Override
    public String toString() {
        return "TeacherQuality{" +
                "id=" + id +
                ", coursesAndTeachingPortfolio=" + coursesAndTeachingPortfolio +
                ", classroomManagementAndStudentRelations=" + classroomManagementAndStudentRelations +
                ", blendedLearning=" + blendedLearning +
                ", courseDescriptionAndAssessmentMethods=" + courseDescriptionAndAssessmentMethods +
                ", professionalCommitment=" + professionalCommitment +
                ", finalScore=" + finalScore +
                '}';
    }



    public Long getId() {
        return id;
    }

    public int getCoursesAndTeachingPortfolio() {
        return coursesAndTeachingPortfolio;
    }

    public int getClassroomManagementAndStudentRelations() {
        return classroomManagementAndStudentRelations;
    }

    public int getBlendedLearning() {
        return blendedLearning;
    }

    public int getCourseDescriptionAndAssessmentMethods() {
        return courseDescriptionAndAssessmentMethods;
    }

    public int getProfessionalCommitment() {
        return professionalCommitment;
    }

    public int getFinalScore() {
        return finalScore;
    }

}
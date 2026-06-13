/*
package com.aisha.real_time.web_based.teacher.performance.evaluation.system.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "axis_first")
public class TeacherQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "courses_and_teaching_portfolio")
    private int coursesAndTeachingPortfolio;

    @Column(name = "classroom_management_and_student_relations")
    private int classroomManagementAndStudentRelations;

    @Column(name = "blended_learning")
    private int blendedLearning;

    @Column(name = "course_description_and_assessment_methods")
    private int courseDescriptionAndAssessmentMethods;

    @Column(name = "professional_commitment")
    private int professionalCommitment;

    @Column(name = "final_score")
    private int finalScore;

    // ======================================================
    //  العلاقات
    // ======================================================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private MyUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_profile_id")
    private TeacherProfile teacherProfile;

    // العلاقة الجديدة: ملفات التقييم لكل فقرة
    @OneToMany(
        mappedBy = "teacherQuality",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    private List<EvaluationFile> evaluationFiles = new ArrayList<>();

    // ======================================================
    //  المنشئات
    // ======================================================

    public TeacherQuality() {}

    // ======================================================
    //  دوال مساعدة
    // ======================================================

    // حساب الدرجة النهائية من مجموع الفقرات الخمس 
    public void calculateFinalScore() {
        this.finalScore = coursesAndTeachingPortfolio
                        + classroomManagementAndStudentRelations
                        + blendedLearning
                        + courseDescriptionAndAssessmentMethods
                        + professionalCommitment;
    }

    // جلب ملف فقرة معينة (يرجع null إذا لم يُرفع بعد) 
    public EvaluationFile getFileForParagraph(int paragraphNumber) {
        return evaluationFiles.stream()
                .filter(f -> f.getParagraphNumber() == paragraphNumber)
                .findFirst()
                .orElse(null);
    }

    // عدد الملفات المرفوعة فعلاً 
    public long getUploadedFilesCount() {
        return evaluationFiles.stream()
                .filter(EvaluationFile::hasFile)
                .count();
    }

    // هل يوجد ملاحظات جديدة من الإدمن لم يقرأها المعلم؟ 
    public boolean hasUnreadNotes() {
        return evaluationFiles.stream().anyMatch(f ->
                f.getAdminNote() != null
                && !f.getAdminNote().isEmpty()
                && !f.isNoteRead());
    }

    //عدد الفقرات التي راجعها الإدمن 
    public long getReviewedCount() {
        return evaluationFiles.stream()
                .filter(EvaluationFile::isReviewed)
                .count();
    }
*/
    /*  ======================================================
    //  Getters & Setters
    // ======================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCoursesAndTeachingPortfolio() {
        return coursesAndTeachingPortfolio;
    }
    public void setCoursesAndTeachingPortfolio(int v) {
        this.coursesAndTeachingPortfolio = v;
    }

    public int getClassroomManagementAndStudentRelations() {
        return classroomManagementAndStudentRelations;
    }
    public void setClassroomManagementAndStudentRelations(int v) {
        this.classroomManagementAndStudentRelations = v;
    }

    public int getBlendedLearning() { return blendedLearning; }
    public void setBlendedLearning(int v) { this.blendedLearning = v; }

    public int getCourseDescriptionAndAssessmentMethods() {
        return courseDescriptionAndAssessmentMethods;
    }
    public void setCourseDescriptionAndAssessmentMethods(int v) {
        this.courseDescriptionAndAssessmentMethods = v;
    }

    public int getProfessionalCommitment() { return professionalCommitment; }
    public void setProfessionalCommitment(int v) { this.professionalCommitment = v; }

    public int getFinalScore() { return finalScore; }
    public void setFinalScore(int finalScore) { this.finalScore = finalScore; }

    public MyUser getUser() { return user; }
    public void setUser(MyUser user) { this.user = user; }

    public TeacherProfile getTeacherProfile() { return teacherProfile; }
    public void setTeacherProfile(TeacherProfile tp) { this.teacherProfile = tp; }

    public List<EvaluationFile> getEvaluationFiles() { return evaluationFiles; }
    public void setEvaluationFiles(List<EvaluationFile> files) {
        this.evaluationFiles = files;
    }
}
*/

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
@Table(name = "teacher_quality")
public class TeacherQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One of the 5 fixed item keys
    @Column(name = "item_name", nullable = false)
    private String itemName;

    // Grade: null until teacher uploads, then automatically set to 20
    @Column(name = "grade")
    private Integer grade;

    // Note: filled only by admin
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    // Just the file name, stored in static/uploads/teacher-quality/
    @Column(name = "file_name")
    private String fileName;

    // Link to the teacher (MyUser)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUser user;

    // Link to the TeacherProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_profile_id")
    private TeacherProfile teacherProfile;

    // ── The 5 Fixed Item Keys ──
    public static final String COURSES_AND_TEACHING_PORTFOLIO = "courses_and_teaching_portfolio";
    public static final String CLASSROOM_MANAGEMENT           = "classroom_management_and_student_relations";
    public static final String BLENDED_LEARNING               = "blended_learning";
    public static final String COURSE_DESCRIPTION             = "course_description_and_assessment_methods";
    public static final String PROFESSIONAL_COMMITMENT        = "professional_commitment";

    // Ordered array — always used to loop the 5 items in correct order
    public static final String[] ALL_ITEMS = {
        COURSES_AND_TEACHING_PORTFOLIO,
        CLASSROOM_MANAGEMENT,
        BLENDED_LEARNING,
        COURSE_DESCRIPTION,
        PROFESSIONAL_COMMITMENT
    };

    // Arabic display name per item key
    public static String getArabicName(String itemKey) {
        switch (itemKey) {
            case COURSES_AND_TEACHING_PORTFOLIO:
                return "المقررات التي قام بتدريسها وانجاز الحقيبة التدريسية الخاصة بتلك المقررات";
            case CLASSROOM_MANAGEMENT:
                return "ادارة الصف والعلاقة مع الطلبة واثارة دافعيتهم";
            case BLENDED_LEARNING:
                return "التعليم المدمج";
            case COURSE_DESCRIPTION:
                return "وصف المقرر الدراسي وتحديثه والاساليب المستعملة في تقييم الطلبة";
            case PROFESSIONAL_COMMITMENT:
                return "الالتزام الوظيفي";
            default:
                return itemKey;
        }
    }

    // Used by MyUser.getAverageScore()
    public int getFinalScore() {
        return grade != null ? grade : 0;
    }

    // ── Constructors ──
    public TeacherQuality() {}

    public TeacherQuality(String itemName, MyUser user, TeacherProfile teacherProfile) {
        this.itemName = itemName;
        this.user = user;
        this.teacherProfile = teacherProfile;
    }

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public MyUser getUser() { return user; }
    public void setUser(MyUser user) { this.user = user; }

    public TeacherProfile getTeacherProfile() { return teacherProfile; }
    public void setTeacherProfile(TeacherProfile teacherProfile) { this.teacherProfile = teacherProfile; }
}
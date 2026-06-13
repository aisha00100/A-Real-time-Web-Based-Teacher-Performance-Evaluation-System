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
@Table(name = "educational_activity")
public class EducationalActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "max_grade", nullable = false)
    private Integer maxGrade;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_profile_id")
    private TeacherProfile teacherProfile;

    // ── The 5 Fixed Item Keys ──
    public static final String COMMITTEE_PARTICIPATION       = "committee_participation";
    public static final String FIELD_VISITS                  = "field_visits";
    public static final String CONTINUOUS_EDUCATION          = "continuous_education";
    public static final String APPRECIATION_LETTERS          = "appreciation_letters";
    public static final String VOLUNTARY_WORK                = "voluntary_work";

    // Ordered array
    public static final String[] ALL_ITEMS = {
        COMMITTEE_PARTICIPATION,
        FIELD_VISITS,
        CONTINUOUS_EDUCATION,
        APPRECIATION_LETTERS,
        VOLUNTARY_WORK
    };

    // Max grade per item key
    public static int getMaxGrade(String itemKey) {
        switch (itemKey) {
            case COMMITTEE_PARTICIPATION: return 30;
            case FIELD_VISITS:            return 20;
            case CONTINUOUS_EDUCATION:    return 15;
            case APPRECIATION_LETTERS:    return 20;
            case VOLUNTARY_WORK:          return 15;
            default:                      return 0;
        }
    }

    // Arabic display name per item key
    public static String getArabicName(String itemKey) {
        switch (itemKey) {
            case COMMITTEE_PARTICIPATION:
                return "المشاركة في اللجان الدائمية والمؤقته داخل وزارة التعليم العالي والبحث العلمي";
            case FIELD_VISITS:
                return "المشاركة في الزيارات الميدانية والحقلية او اجراء اختبارات او تحليلات معملية او مختبرية وغيرها";
            case CONTINUOUS_EDUCATION:
                return "المشاركة في التعليم المستمر والسمنار";
            case APPRECIATION_LETTERS:
                return "كتب الشكر والتقدير او الشهادة التقديرية خلال عام التقييم";
            case VOLUNTARY_WORK:
                return "مساهمته في الاعمال التطوعية داخل الجامعة و المؤسسات العلمية او الوزارات الاخرى او المجتمع";
            default:
                return itemKey;
        }
    }

    public int getFinalScore() {
        return grade != null ? grade : 0;
    }

    // ── Constructors ──
    public EducationalActivity() {}

    public EducationalActivity(String itemName, MyUser user, TeacherProfile teacherProfile) {
        this.itemName = itemName;
        this.maxGrade = getMaxGrade(itemName);
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

    public Integer getMaxGrade() { return maxGrade; }
    public void setMaxGrade(Integer maxGrade) { this.maxGrade = maxGrade; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public MyUser getUser() { return user; }
    public void setUser(MyUser user) { this.user = user; }

    public TeacherProfile getTeacherProfile() { return teacherProfile; }
    public void setTeacherProfile(TeacherProfile teacherProfile) { this.teacherProfile = teacherProfile; }
}

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
@Table(name = "penalty")
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "deduction")
    private Integer deduction; // القيمة المخصومة (سالبة أو صفر)

    @Column(name = "max_deduction", nullable = false)
    private Integer maxDeduction; // الحد الأقصى للخصم

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

    // ── The 6 Fixed Penalty Types ──
    public static final String WARNING = "warning"; // لفت نظر
    public static final String NOTICE = "notice"; // الإنذار
    public static final String SALARY_CUT = "salary_cut"; // قطع الراتب
    public static final String REPRIMAND = "reprimand"; // التوبيخ
    public static final String SALARY_REDUCTION = "salary_reduction"; // إنقاص الراتب
    public static final String DEMOTION = "demotion"; // تنزيل الدرجة

    // Ordered array
    public static final String[] ALL_ITEMS = {
        WARNING,
        NOTICE,
        SALARY_CUT,
        REPRIMAND,
        SALARY_REDUCTION,
        DEMOTION
    };

    // Max deduction per penalty type
    public static int getMaxDeduction(String itemKey) {
        switch (itemKey) {
            case WARNING:            return 3;
            case NOTICE:             return 5;
            case SALARY_CUT:         return 7;
            case REPRIMAND:          return 11;
            case SALARY_REDUCTION:   return 13;
            case DEMOTION:           return 15;
            default:                 return 0;
        }
    }

    // Arabic display name per penalty type
    public static String getArabicName(String itemKey) {
        switch (itemKey) {
            case WARNING:
                return "لفت نظر";
            case NOTICE:
                return "الإنذار";
            case SALARY_CUT:
                return "قطع الراتب";
            case REPRIMAND:
                return "التوبيخ";
            case SALARY_REDUCTION:
                return "إنقاص الراتب";
            case DEMOTION:
                return "تنزيل الدرجة";
            default:
                return itemKey;
        }
    }

    // Display text for deduction amount
    public static String getDeductionText(String itemKey) {
        int amount = getMaxDeduction(itemKey);
        return "تخصم (" + amount + ") " + (amount == 11 || amount == 13 || amount == 15 ? "درجة" : "درجات");
    }

    public int getFinalScore() {
        return deduction != null ? deduction : 0;
    }

    // ── Constructors ──
    public Penalty() {}

    public Penalty(String itemName, MyUser user, TeacherProfile teacherProfile) {
        this.itemName = itemName;
        this.maxDeduction = getMaxDeduction(itemName);
        this.user = user;
        this.teacherProfile = teacherProfile;
    }

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getDeduction() { return deduction; }
    public void setDeduction(Integer deduction) { this.deduction = deduction; }

    public Integer getMaxDeduction() { return maxDeduction; }
    public void setMaxDeduction(Integer maxDeduction) { this.maxDeduction = maxDeduction; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public MyUser getUser() { return user; }
    public void setUser(MyUser user) { this.user = user; }

    public TeacherProfile getTeacherProfile() { return teacherProfile; }
    public void setTeacherProfile(TeacherProfile teacherProfile) { this.teacherProfile = teacherProfile; }
}

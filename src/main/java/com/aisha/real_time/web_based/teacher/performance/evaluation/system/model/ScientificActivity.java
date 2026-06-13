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
@Table(name = "scientific_activity")
public class ScientificActivity {

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

    // ── The 3 Fixed Item Keys ──
    public static final String PUBLISHED_RESEARCH_INDEXED    = "published_research_indexed";
    public static final String PUBLISHED_RESEARCH            = "published_research";
    public static final String BOOKS_AND_SUPERVISION         = "books_and_supervision";

    // Ordered array
    public static final String[] ALL_ITEMS = {
        PUBLISHED_RESEARCH_INDEXED,
        PUBLISHED_RESEARCH,
        BOOKS_AND_SUPERVISION
    };

    // Max grade per item key
    public static int getMaxGrade(String itemKey) {
        switch (itemKey) {
            case PUBLISHED_RESEARCH_INDEXED: return 75;
            case PUBLISHED_RESEARCH:         return 15;
            case BOOKS_AND_SUPERVISION:      return 10;
            default:                         return 0;
        }
    }

    // Arabic display name per item key
    public static String getArabicName(String itemKey) {
        switch (itemKey) {
            case PUBLISHED_RESEARCH_INDEXED:
                return "البحوث العلمية المنشورة في المجلات المفهرسة ضمن المستوعبات العالمية";
            case PUBLISHED_RESEARCH:
                return "البحوث العلمية المنشورة";
            case BOOKS_AND_SUPERVISION:
                return "الكتب والاشراف على الطلبة والتقويم العلمي";
            default:
                return itemKey;
        }
    }

    public int getFinalScore() {
        return grade != null ? grade : 0;
    }

    // ── Constructors ──
    public ScientificActivity() {}

    public ScientificActivity(String itemName, MyUser user, TeacherProfile teacherProfile) {
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


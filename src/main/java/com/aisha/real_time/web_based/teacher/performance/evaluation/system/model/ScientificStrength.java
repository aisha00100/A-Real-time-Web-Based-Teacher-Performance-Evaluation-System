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
@Table(name = "scientific_strength")
public class ScientificStrength {

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

    // ── The 9 Fixed Item Keys ──
    public static final String PATENTS = "patents";
    public static final String H_INDEX = "h_index";
    public static final String WOMENS_UNIT = "womens_unit";
    public static final String ELECTRONIC_SYSTEM = "electronic_system";
    public static final String GUIDANCE_UNITS = "guidance_units";
    public static final String ACCREDITATION_COUNCILS = "accreditation_councils";
    public static final String QUALITY_MANAGERS = "quality_managers";
    public static final String CERTIFIED_TRAINER = "certified_trainer";
    public static final String STUDENT_CLUB_SUPPORT = "student_club_support";

    // Ordered array
    public static final String[] ALL_ITEMS = {
        PATENTS,
        H_INDEX,
        WOMENS_UNIT,
        ELECTRONIC_SYSTEM,
        GUIDANCE_UNITS,
        ACCREDITATION_COUNCILS,
        QUALITY_MANAGERS,
        CERTIFIED_TRAINER,
        STUDENT_CLUB_SUPPORT
    };

    // Max grade per item key
    public static int getMaxGrade(String itemKey) {
        switch (itemKey) {
            case PATENTS:                return 3;
            case H_INDEX:                return 3;
            case WOMENS_UNIT:            return 3;
            case ELECTRONIC_SYSTEM:      return 3;
            case GUIDANCE_UNITS:         return 3;
            case ACCREDITATION_COUNCILS: return 4;
            case QUALITY_MANAGERS:       return 5;
            case CERTIFIED_TRAINER:      return 5;
            case STUDENT_CLUB_SUPPORT:   return 3;
            default:                     return 0;
        }
    }

    // Arabic display name per item key
    public static String getArabicName(String itemKey) {
        switch (itemKey) {
            case PATENTS:
                return "براءات الاختراع والجوائز(أي جائزة تم منحها ومطابقة في بياناتها لمتطلبات النظام الإلكتروني)في عام التقييم حصرا";
            case H_INDEX:
                return "امتلاك التدريسي لمعامل هيرش h-index على صفحته في Scopus (الحد الأدنى لمعامل هيرش هو واحد) (١-٣) تنتج درجة واحدة (٤-٦) تنتج ٢ درجة (٧ فأكثر)تنتج ٣ درجات";
            case WOMENS_UNIT:
                return "مسؤول وحدة شؤون المرأة وجميع العاملين معهم";
            case ELECTRONIC_SYSTEM:
                return "تطوير منظومة إلكترونية لإدارة أحد البرامج على مستوى الجامعة أو الوزارة";
            case GUIDANCE_UNITS:
                return "مسؤولي الشعب والوحدات الارشادية وأعضاء الارتباط";
            case ACCREDITATION_COUNCILS:
                return "اعضاء مجالس الاعتماد البرامجي والمؤسسي";
            case QUALITY_MANAGERS:
                return "مدراء اقسام ضمان الجودة والأداء الجامعي وجميع العاملين معهم كمسؤولي شعب وأعضاء ارتباط";
            case CERTIFIED_TRAINER:
                return "المدرب المعتمد في طرائق التدريس من قبل وزارة التعليم العالي والبحث العلمي مشروطة بـ ١- أن تكون التغذية الراجعة عن مستوى الأداء لذلك المدرب في التدريب إيجابية بمستوى متمكن بالتنسيق مع دائرة الدراسات ٢- أن يلتزم المدربون المعتمدون من وزارة التعليم العالي والبحث العلمي بالتكليف الوزاري المدرسي بالتكليف الوزاري وطرائق التدريس المتضمن الجدول والموقع والمدربين على حسب الترشيح الوارد من الجامعات وبما تحدده الوزارة";
            case STUDENT_CLUB_SUPPORT:
                return "دعم نادي الطلبة بمبلغ (٢٠٠٠) دينار شهرياً";
            default:
                return itemKey;
        }
    }

    public int getFinalScore() {
        return grade != null ? grade : 0;
    }

    // ── Constructors ──
    public ScientificStrength() {}

    public ScientificStrength(String itemName, MyUser user, TeacherProfile teacherProfile) {
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
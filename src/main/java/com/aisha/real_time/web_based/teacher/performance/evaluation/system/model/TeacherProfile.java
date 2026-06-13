package com.aisha.real_time.web_based.teacher.performance.evaluation.system.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;



@Entity
@Table(name = "TeacherProfile")
public class TeacherProfile {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // البيانات الرئيسية
    @Column(name = "university")
    private String university; // الجامعة
    
    @Column(name = "college")
    private String college; // الكلية
    
    @Column(name = "department")
    private String department; // القسم/الفرع
    
    // المعلومات الشخصية (الاسم والعائلة)
    @Column(name = "first_name")
    private String firstName; // الاسم
    
    @Column(name = "father_name")
    private String fatherName; // اسم الاب
    
    @Column(name = "grandfather_name")
    private String grandfatherName; // اسم الجد
    
    @Column(name = "great_grandfather_name")
    private String greatGrandfatherName; // اسم جد الاب
    
    @Column(name = "family_name")
    private String familyName; // اللقب
    
    // معلومات الأم
    @Column(name = "mother_name")
    private String motherName; // اسم الام
    
    @Column(name = "mother_father_name")
    private String motherFatherName; // اسم والد الأم
    
    @Column(name = "mother_grandfather_name")
    private String motherGrandfatherName; // اسم جد الام
    
    @Column(name = "mother_great_grandfather_name")
    private String motherGreatGrandfatherName; // اسم جد الام (الجد الثاني)
    
    // معلومات الهوية
    @Column(name = "national_id_number")
    private String nationalIdNumber; // رقم الجنسية او البطاقة الموحدة
    
    @Column(name = "civil_record")
    private String civilRecord; // السجل
    
    @Column(name = "page_number")
    private String pageNumber; // الصحيفة
    
    @Column(name = "issue_year")
    private Integer issueYear; // سنة الإصدار
    
    @Column(name = "issue_month")
    private Integer issueMonth; // شهر الإصدار
    
    @Column(name = "issue_day")
    private Integer issueDay; // يوم الإصدار
    
    // القسم والاسم (مكرر)
    @Column(name = "department_name")
    private String departmentName; // القسم
    
    @Column(name = "full_name")
    private String fullName; // الاسم
    
    // معلومات الشهادة
    @Column(name = "certificate_type")
    private String certificateType; // الشهادة
    
    @Column(name = "ministerial_order_number")
    private String ministerialOrderNumber; // رقم وتاريخ الامر الوزاري او الجامعي
    
    @Column(name = "certificate_grant_month")
    private Integer certificateGrantMonth; // شهر منح الشهادة
    
    @Column(name = "certificate_grant_day")
    private Integer certificateGrantDay; // يوم منح الشهادة
    
    @Column(name = "issuing_country")
    private String issuingCountry; // البلد المانح
    
    @Column(name = "certificate_university")
    private String certificateUniversity; // الجامعة
    
    @Column(name = "certificate_college")
    private String certificateCollege; // الكلية
    
    @Column(name = "certificate_department")
    private String certificateDepartment; // القسم
    
    // التخصص
    @Column(name = "general_specialization")
    private String generalSpecialization; // الاختصاص العام
    
    @Column(name = "detailed_specialization")
    private String detailedSpecialization; // الاختصاص الدقيق
    
    // اللقب العلمي
    @Column(name = "academic_title")
    private String academicTitle; // اللقب العلمي
    
    @Column(name = "title_granting_institution")
    private String titleGrantingInstitution; // الجهة المانحة
    
    @Column(name = "title_year")
    private Integer titleYear; // السنة
    
    @Column(name = "title_month")
    private Integer titleMonth; // الشهر
    
    @Column(name = "title_day")
    private Integer titleDay; // اليوم
    
    // معلومات الاتصال
    @Column(name = "model_number")
    private String modelNumber; // رقم الموديل
    
    @Column(name = "email")
    private String email; // البريد الالكتروني
    
    // حقول التدقيق
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // تاريخ الإنشاء
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // تاريخ التحديث
    





    // المنشئات
    public TeacherProfile() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }




@OneToMany(mappedBy = "teacherProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TeacherQuality> teacherQualities = new ArrayList<>();
    
    // الـ Getter/Setter
    public List<TeacherQuality> getTeacherQualities() {
        return teacherQualities;
    }
    
    public void setTeacherQualities(List<TeacherQuality> teacherQualities) {
        this.teacherQualities = teacherQualities;
    }
    
    // طريقة مساعدة لإضافة تقييم
    public void addTeacherQuality(TeacherQuality quality) {
        teacherQualities.add(quality);
        quality.setTeacherProfile(this);
    }

    







@OneToOne
    @JoinColumn(name = "user_id", unique = true) // unique يحمي one-to-one في مستوى قاعدة البيانات
    private MyUser user;

public MyUser getUser() {
    return user;
}

public void setUser(MyUser user) {
    this.user = user;
}
    // وسائل الوصول (Getters and Setters)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getGreatGrandfatherName() {
        return greatGrandfatherName;
    }

    public void setGreatGrandfatherName(String greatGrandfatherName) {
        this.greatGrandfatherName = greatGrandfatherName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherFatherName() {
        return motherFatherName;
    }

    public void setMotherFatherName(String motherFatherName) {
        this.motherFatherName = motherFatherName;
    }

    public String getMotherGrandfatherName() {
        return motherGrandfatherName;
    }

    public void setMotherGrandfatherName(String motherGrandfatherName) {
        this.motherGrandfatherName = motherGrandfatherName;
    }

    public String getMotherGreatGrandfatherName() {
        return motherGreatGrandfatherName;
    }

    public void setMotherGreatGrandfatherName(String motherGreatGrandfatherName) {
        this.motherGreatGrandfatherName = motherGreatGrandfatherName;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getCivilRecord() {
        return civilRecord;
    }

    public void setCivilRecord(String civilRecord) {
        this.civilRecord = civilRecord;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getIssueYear() {
        return issueYear;
    }

    public void setIssueYear(Integer issueYear) {
        this.issueYear = issueYear;
    }

    public Integer getIssueMonth() {
        return issueMonth;
    }

    public void setIssueMonth(Integer issueMonth) {
        this.issueMonth = issueMonth;
    }

    public Integer getIssueDay() {
        return issueDay;
    }

    public void setIssueDay(Integer issueDay) {
        this.issueDay = issueDay;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getMinisterialOrderNumber() {
        return ministerialOrderNumber;
    }

    public void setMinisterialOrderNumber(String ministerialOrderNumber) {
        this.ministerialOrderNumber = ministerialOrderNumber;
    }

    public Integer getCertificateGrantMonth() {
        return certificateGrantMonth;
    }

    public void setCertificateGrantMonth(Integer certificateGrantMonth) {
        this.certificateGrantMonth = certificateGrantMonth;
    }

    public Integer getCertificateGrantDay() {
        return certificateGrantDay;
    }

    public void setCertificateGrantDay(Integer certificateGrantDay) {
        this.certificateGrantDay = certificateGrantDay;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getCertificateUniversity() {
        return certificateUniversity;
    }

    public void setCertificateUniversity(String certificateUniversity) {
        this.certificateUniversity = certificateUniversity;
    }

    public String getCertificateCollege() {
        return certificateCollege;
    }

    public void setCertificateCollege(String certificateCollege) {
        this.certificateCollege = certificateCollege;
    }

    public String getCertificateDepartment() {
        return certificateDepartment;
    }

    public void setCertificateDepartment(String certificateDepartment) {
        this.certificateDepartment = certificateDepartment;
    }

    public String getGeneralSpecialization() {
        return generalSpecialization;
    }

    public void setGeneralSpecialization(String generalSpecialization) {
        this.generalSpecialization = generalSpecialization;
    }

    public String getDetailedSpecialization() {
        return detailedSpecialization;
    }

    public void setDetailedSpecialization(String detailedSpecialization) {
        this.detailedSpecialization = detailedSpecialization;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getTitleGrantingInstitution() {
        return titleGrantingInstitution;
    }

    public void setTitleGrantingInstitution(String titleGrantingInstitution) {
        this.titleGrantingInstitution = titleGrantingInstitution;
    }

    public Integer getTitleYear() {
        return titleYear;
    }

    public void setTitleYear(Integer titleYear) {
        this.titleYear = titleYear;
    }

    public Integer getTitleMonth() {
        return titleMonth;
    }

    public void setTitleMonth(Integer titleMonth) {
        this.titleMonth = titleMonth;
    }

    public Integer getTitleDay() {
        return titleDay;
    }

    public void setTitleDay(Integer titleDay) {
        this.titleDay = titleDay;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }













}
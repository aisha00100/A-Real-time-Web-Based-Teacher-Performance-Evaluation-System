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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table (name="users")

public class MyUser {
  
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id; 
   @Column(nullable= false, unique= true)  
   private String username;
   @Column(nullable= false, unique= true)  
   private String email;
   @Column(nullable= false)  
   private String password;
   @Column(nullable= false)  
   private String role;

   public MyUser(){}
    public MyUser(String email, Long id, String password, String role, String username) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.role = role;
        this.username = username;
    }

     @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private TeacherProfile teacherProfile;




// ⭐ إضافة العلاقة الجديدة مع TeacherQuality
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TeacherQuality> teacherQualities = new ArrayList<>();


 





   //Setter
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }




// ⬇️ أضفهما هنا
public TeacherProfile getTeacherProfile() {
    return teacherProfile;
}

public void setTeacherProfile(TeacherProfile teacherProfile) {
    this.teacherProfile = teacherProfile;
}











//Getter
   public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }




















// في MyUser.java - أضف هذه الدوال في النهاية
public int getEvaluationCount() {
    return teacherQualities != null ? teacherQualities.size() : 0;
}

public double getAverageScore() {
    if (teacherQualities == null || teacherQualities.isEmpty()) return 0.0;
    return teacherQualities.stream()
            .mapToInt(TeacherQuality::getFinalScore)
            .average()
            .orElse(0.0);
}

public boolean hasEvaluations() {
    return teacherQualities != null && !teacherQualities.isEmpty();
}

// ⭐ ⭐ ⭐ أضف الـ Getter لـ teacherQualities ⭐ ⭐ ⭐
public List<TeacherQuality> getTeacherQualities() {
    return teacherQualities;
}

public void setTeacherQualities(List<TeacherQuality> teacherQualities) {
    this.teacherQualities = teacherQualities;
}















 
}
package com.school.entity;



import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "schoolDetails")
public class SchoolDetails implements Serializable {

    private static final long serialVersionUID = -5937501502150399893L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String uuidChildDetails;

    @Column(nullable = false)
    private String uuidChild;

    @Column(nullable = false)
    private String schoolUserName;

    @Column(nullable = false)
    private String schoolPassword;

    private Boolean deleted = false;

    public SchoolDetails(long id, String uuidChildDetails, String uuidChild, String schoolUserName, String schoolPassword, Boolean deleted) {
        this.id = id;
        this.uuidChildDetails = uuidChildDetails;
        this.uuidChild = uuidChild;
        this.schoolUserName = schoolUserName;
        this.schoolPassword = schoolPassword;
        this.deleted = deleted;
    }

    public SchoolDetails() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuidChild() {
        return uuidChild;
    }

    public void setUuidChild(String uuidChild) {
        this.uuidChild = uuidChild;
    }

    public String getSchoolUserName() {
        return schoolUserName;
    }

    public void setSchoolUserName(String schoolUserName) {
        this.schoolUserName = schoolUserName;
    }

    public String getSchoolPassword() {
        return schoolPassword;
    }

    public void setSchoolPassword(String schoolPassword) {
        this.schoolPassword = schoolPassword;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUuidChildDetails() {
        return uuidChildDetails;
    }

    public void setUuidChildDetails(String uuidChildDetails) {
        this.uuidChildDetails = uuidChildDetails;
    }
}


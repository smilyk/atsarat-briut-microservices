package com.users.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "resp_person")
public class ResponsePerson implements Serializable {

        private static final long serialVersionUID = 3979450294533245255L;

        @Id
        @GeneratedValue
        private long id;

        @Column(nullable = false)
        private String uuidRespPerson;

        @Column(nullable = false)
        private String firstName;

        private String secondName;

        @Column(nullable = false)
        private String emailRespPerson;

        //teudat-zeut - needed for some atsarat briut. save in byCrypt
        private String tzRespPers;

        private Boolean deleted = false;


    public ResponsePerson() {
    }

    public ResponsePerson(long id, String uuidRespPerson, String firstName, String secondName, String emailRespPerson,
                          String tzRespPers, Boolean deleted) {
        this.id = id;
        this.uuidRespPerson = uuidRespPerson;
        this.firstName = firstName;
        this.secondName = secondName;
        this.emailRespPerson = emailRespPerson;
        this.tzRespPers = tzRespPers;
        this.deleted = deleted;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public String getUuidRespPerson() {
        return uuidRespPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmailRespPerson() {
        return emailRespPerson;
    }

    public String getTz() {
        return tzRespPers;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUuidRespPerson(String uuidRespPerson) {
        this.uuidRespPerson = uuidRespPerson;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setEmailRespPerson(String emailRespPerson) {
        this.emailRespPerson = emailRespPerson;
    }

    public void setTz(String tz) {
        this.tzRespPers = tz;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

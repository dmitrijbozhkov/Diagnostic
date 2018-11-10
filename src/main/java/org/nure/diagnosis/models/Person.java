package org.nure.diagnosis.models;

import lombok.*;
import org.neo4j.ogm.annotation.*;
import org.nure.diagnosis.models.enums.Gender;

import java.util.Date;
import java.util.List;

@NodeEntity
public class Person {

    public Person() { }

    public Person(List<String> labels, String name, String surname, String lastName, Gender gender, Date birthday, String email, String password) {
        this.labels = labels;
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Labels
    @NonNull
    private List<String> labels;

    @Index
    @NonNull
    private String name;
    @Index
    @NonNull
    private String surname;
    @Index
    @NonNull
    private String lastName;
    @NonNull
    private Gender gender;
    @NonNull
    private Date birthday;
    @Index(unique = true)
    @NonNull
    private String email;
    @NonNull
    private String password;

    public Long getId() {
        return id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

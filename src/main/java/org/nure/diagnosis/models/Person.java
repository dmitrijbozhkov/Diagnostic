package org.nure.diagnosis.models;

import lombok.*;
import org.neo4j.ogm.annotation.*;
import org.nure.diagnosis.models.enums.Gender;

import java.util.Date;
import java.util.List;

@NodeEntity
@NoArgsConstructor
public class Person {

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
    @Getter
    private Long id;

    @Labels
    @NonNull
    @Getter
    @Setter
    private List<String> labels;

    @Index
    @NonNull
    @Getter
    @Setter
    private String name;
    @Index
    @NonNull
    @Getter
    @Setter
    private String surname;
    @Index
    @NonNull
    @Getter
    @Setter
    private String lastName;
    @NonNull
    @Getter
    @Setter
    private Gender gender;
    @NonNull
    @Getter
    @Setter
    private Date birthday;
    @Index(unique = true)
    @NonNull
    @Getter
    @Setter
    private String email;
    @NonNull
    @Getter
    @Setter
    private String password;
}

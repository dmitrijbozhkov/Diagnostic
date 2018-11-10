package org.nure.Diagnosis.Models;

import lombok.*;
import org.neo4j.ogm.annotation.*;
import org.nure.Diagnosis.Models.Enums.Gender;

import java.util.Date;
import java.util.List;

@NodeEntity
@RequiredArgsConstructor
public class Person {

    @Id
    @GeneratedValue
    @Getter
    private long id;

    @Labels
    @Getter
    @Setter
    @NonNull
    private List<String> labels;

    @Getter
    @Setter
    @Index
    @NonNull
    private String name;
    @Getter
    @Setter
    @Index
    @NonNull
    private String surname;
    @Getter
    @Setter
    @Index
    @NonNull
    private String lastName;
    @Getter
    @Setter
    @NonNull
    private Gender gender;
    @Getter
    @Setter
    @NonNull
    private Date birthday;
    @Getter
    @Setter
    @Index(unique = true)
    @NonNull
    private String email;
    @Getter
    @Setter
    @NonNull
    private String password;
}

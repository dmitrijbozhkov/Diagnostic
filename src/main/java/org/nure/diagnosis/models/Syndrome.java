package org.nure.diagnosis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.services.common.IdEntity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
public class Syndrome {

    public Syndrome(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Syndrome(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @Getter
    private Long id;
    @Getter
    @Setter
    @Index(unique = true)
    @NonNull
    private String name;
    @Getter
    @Setter
    @NonNull
    private String description;

    @Getter
    @Setter
    @Relationship(type = "HAS_SYNDROME", direction = Relationship.INCOMING)
    private Set<Disease> inDeseases = new HashSet<Disease>();

    @Getter
    @Setter
    @Relationship(type = "SYNDROME_HAS_SYMPTOM", direction = Relationship.OUTGOING)
    private Set<Symptom> hasSymptoms = new HashSet<Symptom>();
}

package org.nure.diagnosis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.services.common.IdEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
public class Disease {

    public Disease(String name, String description, Gender gendered) {
        this.name = name;
        this.description = description;
        this.gendered = gendered;
    }

    public Disease(Long id) {
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
    private Gender gendered;
    @Getter
    @Setter
    @Relationship(type = "HAS_SYNDROME", direction = Relationship.OUTGOING)
    private Set<Syndrome> hasSyndromes = new HashSet<Syndrome>();
    @Getter
    @Setter
    @Relationship(type = "DISEASE_HAS_SYMPTOM", direction = Relationship.OUTGOING)
    private Set<DiseaseHasSymptom> hasSymptoms = new HashSet<DiseaseHasSymptom>();
}

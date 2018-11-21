package org.nure.diagnosis.models;

import lombok.*;
import org.neo4j.ogm.annotation.*;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.services.common.IdEntity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
public class Symptom{

    public Symptom(String name, String description, Gender gendered, String confirmationQuestion) {
        this.name = name;
        this.description = description;
        this.gendered = gendered;
        this.confirmationQuestion = confirmationQuestion;
    }

    public Symptom(Long id) {
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
    @NonNull
    private String confirmationQuestion;

    @Getter
    @Setter
    @Relationship(type = "INVERSE_TO", direction = Relationship.UNDIRECTED)
    private Set<Symptom> inverseTo = new HashSet<Symptom>();

    @Getter
    @Setter
    @Relationship(type = "DISEASE_HAS_SYMPTOM", direction = Relationship.INCOMING)
    private Set<DiseaseHasSymptom> inDeseases = new HashSet<DiseaseHasSymptom>();

    @Getter
    @Setter
    @Relationship(type = "SYNDROME_HAS_SYMPTOM", direction = Relationship.INCOMING)
    private Set<Syndrome> inSyndromes = new HashSet<Syndrome>();
}

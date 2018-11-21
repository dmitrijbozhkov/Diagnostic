package org.nure.diagnosis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "DISEASE_HAS_SYMPTOM")
@NoArgsConstructor
public class DiseaseHasSymptom {

    public DiseaseHasSymptom(boolean isCharacteristic, Disease disease, Symptom symptom) {
        this.disease = disease;
        this.symptom = symptom;
        this.isCharacteristic = isCharacteristic;
    }

    public DiseaseHasSymptom(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @Getter
    private Long id;
    @Getter
    @Setter
    @Property
    private boolean isCharacteristic;
    @StartNode
    @Getter
    @Setter
    private Disease disease;
    @EndNode
    @Getter
    @Setter
    private Symptom symptom;
}

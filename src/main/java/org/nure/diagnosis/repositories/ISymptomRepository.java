package org.nure.diagnosis.repositories;

import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.Symptom;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface ISymptomRepository extends Neo4jRepository<Symptom, Long> {
    Optional<Symptom> findByName(String name);
}

package org.nure.diagnosis.repositories;

import org.nure.diagnosis.models.Disease;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface IDiseaseRepository extends Neo4jRepository<Disease, Long> {
    Optional<Disease> findByName(String name);
}

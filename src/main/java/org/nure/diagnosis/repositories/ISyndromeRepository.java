package org.nure.diagnosis.repositories;

import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.Syndrome;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface ISyndromeRepository extends Neo4jRepository<Syndrome, Long> {
    Optional<Syndrome> findByName(String name);
}

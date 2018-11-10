package org.nure.diagnosis.repositories;

import org.nure.diagnosis.models.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface IPersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}

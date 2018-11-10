package org.nure.Diagnosis.Repositories;

import org.nure.Diagnosis.Models.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IPersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> findByEmail(@Param("email") String email);
}

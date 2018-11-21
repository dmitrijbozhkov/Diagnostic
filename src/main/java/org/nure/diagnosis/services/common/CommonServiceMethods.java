package org.nure.diagnosis.services.common;

import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommonServiceMethods {
    public boolean addAdditionalRelationships(Neo4jRepository repository, Set<IdEntity> relationships, List<Long> updateRelationships) {
        for (long id : updateRelationships) {
            Optional<? extends IdEntity> diseaseRelated = relationships
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (!diseaseRelated.isPresent()) {
                Optional entity = repository.findById(id);
                if (entity.isPresent()) {
                    relationships.add((IdEntity) entity.get());
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}

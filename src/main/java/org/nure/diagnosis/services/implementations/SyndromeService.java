package org.nure.diagnosis.services.implementations;

import lombok.NoArgsConstructor;
import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.repositories.IDiseaseRepository;
import org.nure.diagnosis.repositories.ISymptomRepository;
import org.nure.diagnosis.repositories.ISyndromeRepository;
import org.nure.diagnosis.services.common.CommonServiceMethods;
import org.nure.diagnosis.services.common.IdEntity;
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@NoArgsConstructor
public class SyndromeService implements ISyndromeService {

    @Autowired
    private ISyndromeRepository syndromeRepository;

    @Autowired
    private ISymptomService symptomService;
    @Autowired
    private IDiseaseService diseaseService;

    public SyndromeService(ISyndromeRepository syndromeRepository, ISymptomService symptomService, IDiseaseService diseaseService) {
        this.syndromeRepository = syndromeRepository;
        this.symptomService = symptomService;
        this.diseaseService = diseaseService;
    }

    private void setRelationships(Syndrome syndrome, List<Long> diseases, List<Long> symptomes) {
        Set<Disease> inDiseases = syndrome.getInDeseases();
        Set<Symptom> hasSymptoms = syndrome.getHasSymptoms();
        Set<Disease> updatedInDiseases = new HashSet<>();
        Set<Symptom> updatedHasSymptoms = new HashSet<>();
        Optional<Disease> diseaseRelated;
        for (long id : diseases) {
             diseaseRelated = inDiseases
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (diseaseRelated.isPresent()) {
                updatedInDiseases.add(diseaseRelated.get());
                inDiseases.remove(diseaseRelated.get());
            } else {
                updatedInDiseases.add(diseaseService.getDisease(id));
            }
        }
        Optional<Symptom> symptomRelated;
        for (long id : symptomes) {
             symptomRelated = hasSymptoms
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (symptomRelated.isPresent()) {
                updatedHasSymptoms.add(symptomRelated.get());
                hasSymptoms.remove(symptomRelated.get());
            } else {
                updatedHasSymptoms.add(symptomService.getSymptom(id));
            }
        }
        syndrome.setInDeseases(updatedInDiseases);
        syndrome.setHasSymptoms(updatedHasSymptoms);
    }

    @Override
    public boolean isSyndromeNameTaken(String name) {
        Optional<Syndrome> search = syndromeRepository.findByName(name);
        return search.isPresent();
    }

    @Override
    public void createSyndrome(String name, String description, List<Long> diseases, List<Long> symptomes) throws EntityAlreadyExistsException {
        if (isSyndromeNameTaken(name)) {
            throw new EntityAlreadyExistsException(String.format("Syndrome by the name of %s already exists", name));
        }
        Syndrome syndrome = new Syndrome(name, description);
        setRelationships(syndrome, diseases, symptomes);
        syndromeRepository.save(syndrome);
    }

    @Override
    public void updateSyndrome(long id, String name, String description, List<Long> diseases, List<Long> symptomes) throws EntityNotFoundException {
        Syndrome syndrome = getSyndrome(id);
        syndrome.setName(name);
        syndrome.setDescription(description);
        setRelationships(syndrome, diseases, symptomes);
        syndromeRepository.save(syndrome);
    }

    @Override
    public Syndrome getSyndrome(long id) throws EntityNotFoundException {
        Optional<Syndrome> search = syndromeRepository.findById(id);
        if (!search.isPresent()) {
            throw new EntityNotFoundException(String.format("Can't find syndrme by id %d", id));
        }
        return search.get();
    }

    @Override
    public void deleteSyndrome(long id) throws EntityNotFoundException {
        Syndrome syndrome = getSyndrome(id);
        syndromeRepository.delete(syndrome);
    }
}

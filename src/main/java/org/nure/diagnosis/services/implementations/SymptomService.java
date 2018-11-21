package org.nure.diagnosis.services.implementations;

import lombok.NoArgsConstructor;
import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.DiseaseHasSymptom;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.repositories.IDiseaseRepository;
import org.nure.diagnosis.repositories.ISymptomRepository;
import org.nure.diagnosis.repositories.ISyndromeRepository;
import org.nure.diagnosis.services.common.CommonServiceMethods;
import org.nure.diagnosis.services.common.IdEntity;
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.nure.diagnosis.services.models.CharacteristicToDisease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@NoArgsConstructor
@Service
public class SymptomService implements ISymptomService {

    @Autowired
    private ISymptomRepository symptomRepository;

    @Autowired
    private IDiseaseService diseaseService;
    @Autowired
    private ISyndromeService syndromeService;

    public SymptomService(IDiseaseService diseaseService, ISymptomRepository symptomRepository, ISyndromeService syndromeService) {
        this.diseaseService = diseaseService;
        this.syndromeService = syndromeService;
        this.symptomRepository = symptomRepository;
    }

    private void setRelationships(Symptom symptom, List<Long> syndromes, List<Long> symptomes) {
        Set<Syndrome> inSyndromes = symptom.getInSyndromes();
        Set<Symptom> inverse = symptom.getInverseTo();
        Set<Syndrome> updatedInSyndromes = new HashSet<>();
        Set<Symptom> updatedInverse = new HashSet<>();
        Optional<Symptom> hasInverse;
        for (long id : symptomes) {
            hasInverse = inverse
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (hasInverse.isPresent()) {
                updatedInverse.add(hasInverse.get());
                inverse.remove(hasInverse.get());
            } else {
                updatedInverse.add(this.getSymptom(id));
            }
        }
        Optional<Syndrome> syndromeRelated;
        for (long id : syndromes) {
            syndromeRelated = inSyndromes
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (syndromeRelated.isPresent()) {
                updatedInSyndromes.add(syndromeRelated.get());
                inSyndromes.remove(syndromeRelated.get());
            } else {
                updatedInSyndromes.add(syndromeService.getSyndrome(id));
            }
        }
        symptom.setInSyndromes(updatedInSyndromes);
        symptom.setInverseTo(updatedInverse);
    }

    private void setCharacteristicRelaionship(Symptom symptom, List<CharacteristicToDisease> diseases) {
        Set<DiseaseHasSymptom> diseaseHasSymptoms = symptom.getInDeseases();
        Set<DiseaseHasSymptom> updatedDiseaseHasSymptoms = new HashSet<>();
        Optional<DiseaseHasSymptom> relationship;
        Disease createDiseaseRelationship;
        for (CharacteristicToDisease sc : diseases) {
             relationship = diseaseHasSymptoms
                    .stream()
                    .filter((ds) -> {
                        return ds.getDisease().getId() == sc.getDiseaseId();
                    }).findFirst();
            if (relationship.isPresent()) {
                relationship.get().setCharacteristic(sc.isCharacteristic());
                updatedDiseaseHasSymptoms.add(relationship.get());
                diseaseHasSymptoms.remove(relationship.get());
            } else {
                createDiseaseRelationship = this.diseaseService.getDisease(sc.getDiseaseId());
                updatedDiseaseHasSymptoms.add(new DiseaseHasSymptom(sc.isCharacteristic(), createDiseaseRelationship, symptom));
            }
        }
        symptom.setInDeseases(updatedDiseaseHasSymptoms);
    }

    @Override
    public boolean isSymptomNameTaken(String name) {
        Optional<Symptom> search = symptomRepository.findByName(name);
        return search.isPresent();
    }

    @Override
    @Transactional
    public void createSymptom(String name, String description, Gender gendered, String confirmationQuestion, List<Long> syndromes, List<Long> symptomes, List<CharacteristicToDisease> diseases) throws EntityAlreadyExistsException {
        if (isSymptomNameTaken(name)) {
            throw new EntityAlreadyExistsException(String.format("Symptom by the name of %s already exists", name));
        }
        Symptom created = new Symptom(name, description, gendered, confirmationQuestion);
        setCharacteristicRelaionship(created, diseases);
        setRelationships(created, syndromes, symptomes);
        symptomRepository.save(created);
    }

    @Override
    @Transactional
    public void updateSymptom(long id, String name, String description, Gender gendered, String confirmationQuestion, List<Long> syndromes, List<Long> symptomes, List<CharacteristicToDisease> diseases) throws EntityNotFoundException {
        Symptom symptom = getSymptom(id);
        symptom.setName(name);
        symptom.setDescription(description);
        symptom.setGendered(gendered);
        symptom.setConfirmationQuestion(confirmationQuestion);
        setCharacteristicRelaionship(symptom, diseases);
        setRelationships(symptom, syndromes, symptomes);
        symptomRepository.save(symptom);
    }

    @Override
    public Symptom getSymptom(long id) throws EntityNotFoundException {
        Optional<Symptom> search = symptomRepository.findById(id);
        if (!search.isPresent()) {
            throw new EntityNotFoundException(String.format("Can't find symptom by id %d", id));
        }
        return search.get();
    }

    @Override
    public void deleteSymptom(long id) throws EntityNotFoundException {
        Symptom symptom = getSymptom(id);
        symptomRepository.delete(symptom);
    }
}

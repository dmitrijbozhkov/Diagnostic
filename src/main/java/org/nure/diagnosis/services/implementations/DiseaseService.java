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
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.nure.diagnosis.services.models.SymptomCharacteristic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@NoArgsConstructor
public class DiseaseService implements IDiseaseService {

    @Autowired
    private IDiseaseRepository diseaseRepository;

    @Autowired
    private ISymptomService symptomService;
    @Autowired
    private ISyndromeService syndromeService;
    @Autowired
    private CommonServiceMethods commonServiceMethods;

    public DiseaseService(IDiseaseRepository diseaseRepository, ISymptomService symptomService, ISyndromeService syndromeService) {
        this.diseaseRepository = diseaseRepository;
        this.syndromeService = syndromeService;
        this.symptomService = symptomService;
    }

    private void setRelationships(Disease disease, List<Long> syndromes) {
        Set<Syndrome> diseaseSyndromes = disease.getHasSyndromes();
        Set<Syndrome> updatedDiseaseSyndromes = new HashSet<>();
        Optional<Syndrome> hasSyndrome;
        for (long id : syndromes) {
            hasSyndrome = diseaseSyndromes
                    .stream()
                    .filter((d) -> {
                        return d.getId() == id;
                    }).findFirst();
            if (hasSyndrome.isPresent()) {
                updatedDiseaseSyndromes.add(hasSyndrome.get());
                diseaseSyndromes.remove(hasSyndrome.get());
            } else {
                updatedDiseaseSyndromes.add(syndromeService.getSyndrome(id));
            }
        }
        disease.setHasSyndromes(updatedDiseaseSyndromes);
    }

    private void setCharacteristicRelaionship(Disease disease, List<SymptomCharacteristic> symptomes) {
        Set<DiseaseHasSymptom> diseaseHasSymptoms = disease.getHasSymptoms();
        Set<DiseaseHasSymptom> updatedDiseaseHasSymptoms = new HashSet<>();
        Symptom symptom;
        Optional<DiseaseHasSymptom> relationship;
        for (SymptomCharacteristic sc : symptomes) {
             relationship = diseaseHasSymptoms
                    .stream()
                    .filter((ds) -> {
                        return ds.getSymptom().getId() == sc.getSymptomId();
                    }).findFirst();
            if (relationship.isPresent()) {
                relationship.get().setCharacteristic(sc.isCharacteristic());
                updatedDiseaseHasSymptoms.add(relationship.get());
                diseaseHasSymptoms.remove(relationship.get());
            } else {
                symptom = this.symptomService.getSymptom(sc.getSymptomId());
                updatedDiseaseHasSymptoms.add(new DiseaseHasSymptom(sc.isCharacteristic(), disease, symptom));
            }
        }
        disease.setHasSymptoms(updatedDiseaseHasSymptoms);
    }

    @Override
    public boolean isDeseaseNameTaken(String name) {
        Optional<Disease> search = diseaseRepository.findByName(name);
        return search.isPresent();
    }

    @Override
    @Transactional
    public void createDisease(String name, String description, Gender gendered, List<Long> syndromes, List<SymptomCharacteristic> symptomes) throws EntityAlreadyExistsException {
        if (isDeseaseNameTaken(name)) {
            throw new EntityAlreadyExistsException(String.format("Disease by the name of %s already exists", name));
        }
        Disease created = new Disease(name, description, gendered);
        setCharacteristicRelaionship(created, symptomes);
        setRelationships(created, syndromes);
        diseaseRepository.save(created);
    }

    @Override
    @Transactional
    public void updateDisease(long id, String name, String description, Gender gendered, List<Long> syndromes, List<SymptomCharacteristic> symptomes) throws EntityNotFoundException {
        Disease disease = getDisease(id);
        disease.setName(name);
        disease.setDescription(description);
        disease.setGendered(gendered);
        setCharacteristicRelaionship(disease, symptomes);
        setRelationships(disease, syndromes);
        diseaseRepository.save(disease);
    }

    @Override
    public Disease getDisease(long id) throws EntityNotFoundException {
        Optional<Disease> search = diseaseRepository.findById(id);
        if (!search.isPresent()) {
            throw new EntityNotFoundException(String.format("Can't find disease by id %d", id));
        }
        return search.get();
    }

    @Override
    public void deleteDesease(long id) throws EntityNotFoundException {
        Disease disease = getDisease(id);
        diseaseRepository.delete(disease);
    }
}

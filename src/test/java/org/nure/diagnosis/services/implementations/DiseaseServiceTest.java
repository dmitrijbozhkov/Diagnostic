package org.nure.diagnosis.services.implementations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.DiseaseHasSymptom;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.repositories.IDiseaseRepository;
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.nure.diagnosis.services.models.SymptomCharacteristic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiseaseServiceTest {

    private ISymptomService symptomService;
    private IDiseaseRepository diseaseRepository;
    private IDiseaseService diseaseService;
    private ISyndromeService syndromeService;

    private final long diseaseId = 25;
    private final String diseaseName = "Diharrea";
    private final String diseaseDescription = "stuff happens";
    private final Gender diseaseGender = Gender.MALE;

    @Before
    public void setUpRepos() {
        this.symptomService = mock(ISymptomService.class);
        this.diseaseRepository = mock(IDiseaseRepository.class);
        this.syndromeService = mock(ISyndromeService.class);
        this.diseaseService = new DiseaseService(this.diseaseRepository, this.symptomService, this.syndromeService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetDiseaseShouldThrowEntityNotFoundExceptionIfDiseaseNotFoundByProvidedId() {
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.empty());
        diseaseService.getDisease(diseaseId);
    }

    @Test
    public void testGetDiseaseShouldReturnFoundDisease() {
        Disease disease = new Disease();
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.of(disease));
        Disease toTest = diseaseService.getDisease(diseaseId);
        assertEquals(disease.getName(), toTest.getName());
        assertEquals(disease.getDescription(), toTest.getDescription());
        assertEquals(disease.getGendered(), toTest.getGendered());
    }

    @Test
    public void testDeleteDiseaseShouldCallDeleteOnReposiotryWithIt() {
        Disease disease = new Disease();
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.of(disease));
        diseaseService.deleteDesease(diseaseId);
        verify(diseaseRepository).delete(disease);
    }

    @Test
    public void testCreateDiseaseShouldSetDiseaseNameDescriptionAndGender() {
        List<Long> syndromes = new ArrayList<>();
        List<SymptomCharacteristic> symptomes = new ArrayList<>();
        diseaseService.createDisease(diseaseName, diseaseDescription, diseaseGender, syndromes, symptomes);
        verify(diseaseRepository).save(argThat(arg -> {
            return diseaseName.equals(((Disease) arg).getName()) &&
                    diseaseDescription.equals(((Disease) arg).getDescription()) &&
                    diseaseGender.equals(((Disease) arg).getGendered());
        }));
    }

    @Test
    public void testCreateDiseaseShouldSetSyndromesRelationship() {
        long syndromeId = 45;
        Syndrome syndrome = new Syndrome();
        List<Long> syndromes = Arrays.asList(syndromeId);
        List<SymptomCharacteristic> symptomes = new ArrayList<>();
        given(syndromeService.getSyndrome(syndromeId)).willReturn(syndrome);
        diseaseService.createDisease(diseaseName, diseaseDescription, diseaseGender, syndromes, symptomes);
        verify(diseaseRepository).save(argThat(arg -> {
            return ((Disease) arg).getHasSyndromes().stream().anyMatch(s -> s.equals(syndrome));
        }));
    }

    @Test
    public void testCreateDiseaseShouldSetDiseaseHasSymptomesRelationship() {
        long symptomId = 23;
        boolean isCharacteristic = true;
        SymptomCharacteristic symptomCharacteristic = new SymptomCharacteristic(symptomId, isCharacteristic);
        Symptom symptom = new Symptom();
        List<Long> syndromes = new ArrayList<>();
        List<SymptomCharacteristic> symptomes = Arrays.asList(symptomCharacteristic);
        given(symptomService.getSymptom(symptomId)).willReturn(symptom);
        diseaseService.createDisease(diseaseName, diseaseDescription, diseaseGender, syndromes, symptomes);
        verify(diseaseRepository).save(argThat(arg -> {
            DiseaseHasSymptom ds = ((Disease) arg).getHasSymptoms().iterator().next();
            return ds.getSymptom().equals(symptom) && ds.isCharacteristic();
        }));
    }

    @Test
    public void testUpdateDiseaseShouldUpdateDiseaseFields() {
        String nextDiseaseName = "Cancer";
        String nextDiseaseDescription = "Sweats alot";
        Gender nextDiseaseGendered = Gender.FEMALE;
        Disease oldDisease = new Disease(diseaseName, diseaseDescription, diseaseGender);
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.of(oldDisease));
        List<SymptomCharacteristic> symptomes = new ArrayList<>();
        List<Long> syndromes = new ArrayList<>();
        diseaseService.updateDisease(
                diseaseId,
                nextDiseaseName,
                nextDiseaseDescription,
                nextDiseaseGendered,
                syndromes,
                symptomes);
        verify(diseaseRepository).save(argThat(s -> {
            return ((Disease) s).getName().equals(nextDiseaseName) &&
                    ((Disease) s).getDescription().equals(nextDiseaseDescription) &&
                    ((Disease) s).getGendered().equals(nextDiseaseGendered);
        }));
    }

    @Test
    public void testUpdateDiseaseShouldUpdateSymptomRelationships() {
        long oldSymptomId = 34;
        long oldRelationshipId = 25;
        long symptomId = 5;
        Disease disease = new Disease(diseaseId);
        Symptom oldSymptom = new Symptom(oldSymptomId);
        DiseaseHasSymptom oldDiseaseHasSymptom = new DiseaseHasSymptom(oldRelationshipId);
        Symptom symptom = new Symptom(symptomId);
        oldDiseaseHasSymptom.setSymptom(oldSymptom);
        oldDiseaseHasSymptom.setDisease(disease);
        oldSymptom.setInDeseases(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        disease.setHasSymptoms(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        SymptomCharacteristic relationship = new SymptomCharacteristic(symptomId, true);
        given(symptomService.getSymptom(symptomId)).willReturn(symptom);
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.of(disease));
        List<Long> syndromes = new ArrayList<>();
        List<SymptomCharacteristic> diseases = Arrays.asList(relationship);
        diseaseService.updateDisease(
                diseaseId,
                diseaseName,
                diseaseDescription,
                diseaseGender,
                syndromes,
                diseases);
        verify(diseaseRepository).save(argThat(s -> {
            return ((Disease) s).getHasSymptoms().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getSymptom().equals(symptom)) &&
                    !((Disease) s).getHasSymptoms().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getDisease().equals(oldSymptom));
        }));
    }

    @Test
    public void testUpdateSymptomShouldUpdateCharacteristicFieldOnExistingRelationship() {
        long oldSymptomId = 34;
        long oldRelationshipId = 25;
        long symptomId = 5;
        Disease disease = new Disease(diseaseId);
        DiseaseHasSymptom oldDiseaseHasSymptom = new DiseaseHasSymptom(oldRelationshipId);
        Symptom symptom = new Symptom(symptomId);
        oldDiseaseHasSymptom.setSymptom(symptom);
        oldDiseaseHasSymptom.setDisease(disease);
        oldDiseaseHasSymptom.setCharacteristic(false);
        symptom.setInDeseases(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        disease.setHasSymptoms(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        SymptomCharacteristic relationship = new SymptomCharacteristic(symptomId, true);
        given(symptomService.getSymptom(symptomId)).willReturn(symptom);
        given(diseaseRepository.findById(diseaseId)).willReturn(Optional.of(disease));
        List<Long> syndromes = new ArrayList<>();
        List<SymptomCharacteristic> diseases = Arrays.asList(relationship);
        diseaseService.updateDisease(
                diseaseId,
                diseaseName,
                diseaseDescription,
                diseaseGender,
                syndromes,
                diseases);
        verify(diseaseRepository).save(argThat(s -> {
            return ((Disease) s).getHasSymptoms().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getSymptom().equals(symptom) &&
                    ((DiseaseHasSymptom) r).isCharacteristic());
        }));
    }
}

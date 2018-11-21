package org.nure.diagnosis.services.implementations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.repositories.IDiseaseRepository;
import org.nure.diagnosis.repositories.ISymptomRepository;
import org.nure.diagnosis.repositories.ISyndromeRepository;
import org.nure.diagnosis.services.common.CommonServiceMethods;
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SyndromeServiceTest {

    private ISymptomService symptomService;
    private ISyndromeRepository syndromeRepository;
    private IDiseaseService diseaseService;
    private ISyndromeService syndromeService;

    private final long syndromeId = 25;
    private final String syndromeName = "Autoimmune";
    private final String syndromeDescription = "stuff happens";

    @Before
    public void setUpRepos() {
        this.symptomService = mock(ISymptomService.class);
        this.syndromeRepository = mock(ISyndromeRepository.class);
        this.diseaseService = mock(IDiseaseService.class);
        this.syndromeService = new SyndromeService(this.syndromeRepository, this.symptomService, this.diseaseService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetSyndromeShouldThrowEntityNotFoundExceptionIfSyndromeNotFound() {
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.empty());
        syndromeService.getSyndrome(syndromeId);
    }

    @Test
    public void testGetSyndromeShouldReturnFoundSyndrome() {
        Syndrome syndrome = new Syndrome(syndromeName, syndromeDescription);
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.of(syndrome));
        Syndrome toTest = syndromeService.getSyndrome(syndromeId);
        assertEquals(syndrome.getName(), toTest.getName());
        assertEquals(syndrome.getDescription(), toTest.getDescription());
    }

    @Test
    public void testDeleteSyndromeShouldCallDeleteOnReposiotryWithIt() {
        Syndrome syndrome = new Syndrome(syndromeName, syndromeDescription);
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.of(syndrome));
        syndromeService.deleteSyndrome(syndromeId);
        verify(syndromeRepository).delete(syndrome);
    }

    @Test
    public void testCreateSyndromeShouldSetSyndromeNameAndDescription() {
        List<Long> diseases = new ArrayList<>();
        List<Long> symptomes = new ArrayList<>();
        syndromeService.createSyndrome(syndromeName, syndromeDescription, diseases, symptomes);
        verify(syndromeRepository).save(argThat(arg -> {
            return syndromeName.equals(((Syndrome) arg).getName()) && syndromeDescription.equals(((Syndrome) arg).getDescription());
        }));
    }

    @Test
    public void testCreateSyndromeShouldSetInDiseasesRelationship() {
        long diseaseId = 45;
        Disease connectedDisease = new Disease();
        given(diseaseService.getDisease(diseaseId)).willReturn(connectedDisease);
        List<Long> diseases = Arrays.asList(diseaseId);
        List<Long> symptomes = new ArrayList<>();
        syndromeService.createSyndrome(syndromeName, syndromeDescription, diseases, symptomes);
        verify(syndromeRepository).save(argThat(arg -> {
            return ((Syndrome) arg).getInDeseases().iterator().next().equals(connectedDisease);
        }));
    }

    @Test
    public void testCreateSyndromeShouldSetHasSymptomesRelationship() {
        long symptomId = 45;
        Symptom hasSymptom = new Symptom();
        given(symptomService.getSymptom(symptomId)).willReturn(hasSymptom);
        List<Long> diseases = new ArrayList<>();
        List<Long> symptomes = Arrays.asList(symptomId);
        syndromeService.createSyndrome(syndromeName, syndromeDescription, diseases, symptomes);
        verify(syndromeRepository).save(argThat(arg -> {
            return ((Syndrome) arg).getHasSymptoms().iterator().next().equals(hasSymptom);
        }));
    }

    @Test
    public void testUpdateSyndromeShouldUpdateSyndromeFields() {
        String nextSyndromeName = "Sweating";
        String nextSyndromeDescription = "Sweats alot";
        Syndrome oldSyndrome = new Syndrome(nextSyndromeName, nextSyndromeDescription);
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.of(oldSyndrome));
        List<Long> inDiseases = new ArrayList<>();
        List<Long> symptomes = new ArrayList<>();
        syndromeService.updateSyndrome(
                syndromeId,
                nextSyndromeName,
                nextSyndromeDescription,
                inDiseases,
                symptomes);
        verify(syndromeRepository).save(argThat(s -> {
            return ((Syndrome) s).getName().equals(nextSyndromeName) &&
                    ((Syndrome) s).getDescription().equals(nextSyndromeDescription);
        }));
    }


    @Test
    public void testUpdateSyndromeShouldUpdateSymptomRelationship() {
        long syndromeId = 5;
        long oldSymptomId = 34;
        long symptomId = 35;
        Symptom oldSymptom = new Symptom(oldSymptomId);
        Syndrome syndrome = new Syndrome();
        syndrome.setHasSymptoms(new HashSet<>(Arrays.asList(oldSymptom)));
        Symptom symptom = new Symptom(symptomId);
        given(symptomService.getSymptom(symptomId)).willReturn(symptom);
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.of(syndrome));
        List<Long> symptoms = Arrays.asList(symptomId);
        List<Long> diseases = new ArrayList<>();
        syndromeService.updateSyndrome(
                syndromeId,
                syndromeName,
                syndromeDescription,
                diseases,
                symptoms);
        verify(syndromeRepository).save(argThat(s -> {
            return ((Syndrome) s).getHasSymptoms().stream().anyMatch(i -> i.equals(symptom)) &&
                    !((Syndrome) s).getHasSymptoms().stream().anyMatch(i -> i.equals(oldSymptom));
        }));
    }

    @Test
    public void testUpdateSyndromeShouldUpdateDiseaseRelationship() {
        long syndromeId = 5;
        long oldDiseaseId = 34;
        long diseaseId = 35;
        Disease oldDisease = new Disease(oldDiseaseId);
        Syndrome syndrome = new Syndrome();
        syndrome.setInDeseases(new HashSet<>(Arrays.asList(oldDisease)));
        Disease disease = new Disease(diseaseId);
        given(diseaseService.getDisease(diseaseId)).willReturn(disease);
        given(syndromeRepository.findById(syndromeId)).willReturn(Optional.of(syndrome));
        List<Long> symptoms = new ArrayList<>();
        List<Long> diseases = Arrays.asList(diseaseId);
        syndromeService.updateSyndrome(
                syndromeId,
                syndromeName,
                syndromeDescription,
                diseases,
                symptoms);
        verify(syndromeRepository).save(argThat(s -> {
            return ((Syndrome) s).getInDeseases().stream().anyMatch(i -> i.equals(disease)) &&
                    !((Syndrome) s).getInDeseases().stream().anyMatch(i -> i.equals(oldDisease));
        }));
    }
}

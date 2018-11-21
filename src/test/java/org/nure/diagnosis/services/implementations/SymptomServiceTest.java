package org.nure.diagnosis.services.implementations;

import org.apache.commons.lang3.Conversion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.DiseaseHasSymptom;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.repositories.ISymptomRepository;
import org.nure.diagnosis.services.interfaces.IDiseaseService;
import org.nure.diagnosis.services.interfaces.ISymptomService;
import org.nure.diagnosis.services.interfaces.ISyndromeService;
import org.nure.diagnosis.services.models.CharacteristicToDisease;
import org.nure.diagnosis.services.models.SymptomCharacteristic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SymptomServiceTest {

    private ISymptomRepository symptomRepository;
    private IDiseaseService diseaseService;
    private ISyndromeService syndromeService;
    private ISymptomService symptomService;

    private final long symptomId = 32;
    private final String symptomName = "Temperature";
    private final String symptomDescription = "Stuff happens";
    private final Gender symptomGendered = Gender.FEMALE;
    private final String symptomConfirmationQuestion = "confirm me";

    @Before
    public void setUpRepos() {
        this.symptomRepository = mock(ISymptomRepository.class);
        this.diseaseService = mock(IDiseaseService.class);
        this.syndromeService = mock(ISyndromeService.class);
        this.symptomService = new SymptomService(this.diseaseService, this.symptomRepository, this.syndromeService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetSymptomShouldThrowEntityNotFoundExceptionIfSymptomNotFoundByProvidedId() {
        given(symptomRepository.findById(symptomId)).willReturn(Optional.empty());
        symptomService.getSymptom(symptomId);
    }

    @Test
    public void testGetSymptomShouldReturnFoundSymptom() {
        Symptom symptom = new Symptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion);
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(symptom));
        Symptom toTest = symptomService.getSymptom(symptomId);
        assertEquals(symptom.getName(), toTest.getName());
        assertEquals(symptom.getDescription(), toTest.getDescription());
        assertEquals(symptom.getGendered(), toTest.getGendered());
        assertEquals(symptom.getConfirmationQuestion(), toTest.getConfirmationQuestion());
    }

    @Test
    public void testDeleteSymptomShouldCallDeleteOnReposiotryWithIt() {
        Symptom symptom = new Symptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion);
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(symptom));
        symptomService.deleteSymptom(symptomId);
        verify(symptomRepository).delete(symptom);
    }

    @Test
    public void testCreateSymptomShouldSetDiseaseNameDescriptionAndGender() {
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        symptomService.createSymptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion, syndromes, inverses, diseases);
        verify(symptomRepository).save(argThat(arg -> {
            return symptomName.equals(((Symptom) arg).getName()) &&
                    symptomDescription.equals(((Symptom) arg).getDescription()) &&
                    symptomGendered.equals(((Symptom) arg).getGendered()) &&
                    symptomConfirmationQuestion.equals(((Symptom) arg).getConfirmationQuestion());
        }));
    }

    @Test
    public void testCreateSymptomShouldSetSyndromesRelationship() {
        long syndromeId = 45;
        Syndrome syndrome = new Syndrome();
        List<Long> syndromes = Arrays.asList(syndromeId);
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        given(syndromeService.getSyndrome(syndromeId)).willReturn(syndrome);
        symptomService.createSymptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion, syndromes, inverses, diseases);
        verify(symptomRepository).save(argThat(arg -> {
            return ((Symptom) arg).getInSyndromes().stream().anyMatch(s -> s.equals(syndrome));
        }));
    }

    @Test
    public void testCreateSymptomShouldSetInversesRelationship() {
        long inverseId = 45;
        Symptom symptom = new Symptom();
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = Arrays.asList(inverseId);
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        given(symptomRepository.findById(inverseId)).willReturn(Optional.of(symptom));
        symptomService.createSymptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion, syndromes, inverses, diseases);
        verify(symptomRepository).save(argThat(arg -> {
            return ((Symptom) arg).getInverseTo().stream().anyMatch(s -> s.equals(symptom));
        }));
    }

    @Test
    public void testCreateSymptomShouldSetSymptomHasSymptomesRelationship() {
        long diseaseId = 23;
        boolean isCharacteristic = true;
        Disease disease = new Disease();
        CharacteristicToDisease characteristicToDisease = new CharacteristicToDisease(diseaseId, isCharacteristic);
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = Arrays.asList(characteristicToDisease);
        given(diseaseService.getDisease(diseaseId)).willReturn(disease);
        symptomService.createSymptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion, syndromes, inverses, diseases);
        verify(symptomRepository).save(argThat(arg -> {
            DiseaseHasSymptom ds = ((Symptom) arg).getInDeseases().iterator().next();
            return ds.getDisease().equals(disease) && ds.isCharacteristic();
        }));
    }

    @Test
    public void testUpdateSymptomShouldResetSymptomFields() {
        String nextSymptomName = "Sweating";
        String nextSymptomDescription = "Sweats alot";
        Gender nextSymptomGendered = Gender.MALE;
        String nextSymptomConfirmationQuestion = "Is it ok?";
        Symptom oldSymptom = new Symptom(symptomName, symptomDescription, symptomGendered, symptomConfirmationQuestion);
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(oldSymptom));
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        symptomService.updateSymptom(
                symptomId,
                nextSymptomName,
                nextSymptomDescription,
                nextSymptomGendered,
                nextSymptomConfirmationQuestion,
                syndromes,
                inverses,
                diseases);
        verify(symptomRepository).save(argThat(s -> {
            return ((Symptom) s).getName().equals(nextSymptomName) &&
                    ((Symptom) s).getDescription().equals(nextSymptomDescription) &&
                    ((Symptom) s).getGendered().equals(nextSymptomGendered) &&
                    ((Symptom) s).getConfirmationQuestion().equals(nextSymptomConfirmationQuestion);
        }));
    }

    @Test
    public void testUpdateSymptomShouldUpdateInverseSymptoms() {
        long inverseId = 5;
        long oldInverseId = 34;
        Symptom oldInverse = new Symptom(oldInverseId);
        Symptom oldSymptom = new Symptom();
        oldSymptom.setInverseTo(new HashSet<Symptom>(Arrays.asList(oldInverse)));
        Symptom inverse = new Symptom();
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(oldSymptom));
        given(symptomRepository.findById(inverseId)).willReturn(Optional.of(inverse));
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = Arrays.asList(inverseId);
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        symptomService.updateSymptom(
                symptomId,
                symptomName,
                symptomDescription,
                symptomGendered,
                symptomConfirmationQuestion,
                syndromes,
                inverses,
                diseases);
        verify(symptomRepository).save(argThat(s -> {
            return ((Symptom) s).getInverseTo().stream().anyMatch(i -> i.equals(inverse)) &&
                    !((Symptom) s).getInverseTo().stream().anyMatch(i -> i.equals(oldInverse));
        }));
    }

    @Test
    public void testUpdateSymptomShouldUpdateSyndromes() {
        long syndromeId = 5;
        long oldSyndromeId = 34;
        Syndrome oldSyndrome = new Syndrome(oldSyndromeId);
        Symptom oldSymptom = new Symptom();
        oldSymptom.setInSyndromes(new HashSet<>(Arrays.asList(oldSyndrome)));
        Syndrome syndrome = new Syndrome();
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(oldSymptom));
        given(syndromeService.getSyndrome(syndromeId)).willReturn(syndrome);
        List<Long> syndromes = Arrays.asList(syndromeId);
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = new ArrayList<>();
        symptomService.updateSymptom(
                symptomId,
                symptomName,
                symptomDescription,
                symptomGendered,
                symptomConfirmationQuestion,
                syndromes,
                inverses,
                diseases);
        verify(symptomRepository).save(argThat(s -> {
            return ((Symptom) s).getInSyndromes().stream().anyMatch(i -> i.equals(syndrome)) &&
                    !((Symptom) s).getInSyndromes().stream().anyMatch(i -> i.equals(oldSyndrome));
        }));
    }

    @Test
    public void testUpdateSymptomShouldUpdateDiseaseRelationships() {
        long oldDiseaseId = 34;
        long oldRelationshipId = 25;
        long diseaseId = 5;
        Disease oldDisease = new Disease(oldDiseaseId);
        DiseaseHasSymptom oldDiseaseHasSymptom = new DiseaseHasSymptom(oldRelationshipId);
        Disease disease = new Disease(diseaseId);
        Symptom oldSymptom = new Symptom();
        oldDiseaseHasSymptom.setSymptom(oldSymptom);
        oldDiseaseHasSymptom.setDisease(oldDisease);
        oldSymptom.setInDeseases(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        CharacteristicToDisease relationship = new CharacteristicToDisease(diseaseId, true);
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(oldSymptom));
        given(diseaseService.getDisease(diseaseId)).willReturn(disease);
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = Arrays.asList(relationship);
        symptomService.updateSymptom(
                symptomId,
                symptomName,
                symptomDescription,
                symptomGendered,
                symptomConfirmationQuestion,
                syndromes,
                inverses,
                diseases);
        verify(symptomRepository).save(argThat(s -> {
            return ((Symptom) s).getInDeseases().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getDisease().equals(disease)) &&
                    !((Symptom) s).getInDeseases().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getDisease().equals(oldDisease));
        }));
    }

    @Test
    public void testUpdateSymptomShouldUpdateCharacteristicFieldOnExistingRelationship() {
        long oldDiseaseId = 34;
        long oldRelationshipId = 25;
        Disease oldDisease = new Disease(oldDiseaseId);
        DiseaseHasSymptom oldDiseaseHasSymptom = new DiseaseHasSymptom(oldRelationshipId);
        Symptom oldSymptom = new Symptom();
        oldDiseaseHasSymptom.setSymptom(oldSymptom);
        oldDiseaseHasSymptom.setDisease(oldDisease);
        oldDiseaseHasSymptom.setCharacteristic(false);
        oldSymptom.setInDeseases(new HashSet<>(Arrays.asList(oldDiseaseHasSymptom)));
        CharacteristicToDisease relationship = new CharacteristicToDisease(oldDiseaseId, true);
        given(symptomRepository.findById(symptomId)).willReturn(Optional.of(oldSymptom));
        given(diseaseService.getDisease(oldDiseaseId)).willReturn(oldDisease);
        List<Long> syndromes = new ArrayList<>();
        List<Long> inverses = new ArrayList<>();
        List<CharacteristicToDisease> diseases = Arrays.asList(relationship);
        symptomService.updateSymptom(
                symptomId,
                symptomName,
                symptomDescription,
                symptomGendered,
                symptomConfirmationQuestion,
                syndromes,
                inverses,
                diseases);
        verify(symptomRepository).save(argThat(s -> {
            return ((Symptom) s).getInDeseases().stream().anyMatch(r -> ((DiseaseHasSymptom) r).getDisease().equals(oldDisease) &&
                    ((DiseaseHasSymptom) r).isCharacteristic());
        }));
    }
}

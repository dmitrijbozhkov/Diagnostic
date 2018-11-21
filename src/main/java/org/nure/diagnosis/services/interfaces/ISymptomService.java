package org.nure.diagnosis.services.interfaces;

import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.Symptom;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.services.models.CharacteristicToDisease;

import java.util.HashMap;
import java.util.List;

public interface ISymptomService {
    boolean isSymptomNameTaken(String name);
    void createSymptom(String name, String description, Gender gendered, String confirmationQuestion, List<Long> syndrmes, List<Long> symptomes, List<CharacteristicToDisease> diseases) throws EntityAlreadyExistsException;
    void updateSymptom(long id, String name, String description, Gender gendered, String confirmationQuestion, List<Long> syndrmes, List<Long> symptomes, List<CharacteristicToDisease> diseases) throws EntityNotFoundException;
    Symptom getSymptom(long id) throws EntityNotFoundException;
    void deleteSymptom(long id) throws EntityNotFoundException;
}

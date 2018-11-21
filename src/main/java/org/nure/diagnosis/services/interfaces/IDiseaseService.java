package org.nure.diagnosis.services.interfaces;

import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Disease;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.services.models.SymptomCharacteristic;

import java.util.HashMap;
import java.util.List;

public interface IDiseaseService {
    boolean isDeseaseNameTaken(String name);
    void createDisease(String name, String description, Gender gendered, List<Long> syndromes, List<SymptomCharacteristic> symptomes) throws EntityAlreadyExistsException;
    void updateDisease(long id, String name, String description, Gender gendered, List<Long> syndromes, List<SymptomCharacteristic> symptomes) throws EntityNotFoundException;
    Disease getDisease(long id) throws EntityNotFoundException;
    void deleteDesease(long id) throws EntityNotFoundException;
}

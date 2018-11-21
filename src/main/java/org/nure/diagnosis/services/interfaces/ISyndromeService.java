package org.nure.diagnosis.services.interfaces;

import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.Syndrome;
import org.nure.diagnosis.models.enums.Gender;

import java.util.HashMap;
import java.util.List;

public interface ISyndromeService {
    boolean isSyndromeNameTaken(String name);
    void createSyndrome(String name, String description, List<Long> diseases, List<Long> symptomes) throws EntityAlreadyExistsException;
    void updateSyndrome(long id, String name, String description, List<Long> diseases, List<Long> symptomes) throws EntityNotFoundException;
    Syndrome getSyndrome(long id) throws EntityNotFoundException;
    void deleteSyndrome(long id) throws EntityNotFoundException;
}

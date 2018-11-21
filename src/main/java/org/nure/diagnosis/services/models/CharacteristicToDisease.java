package org.nure.diagnosis.services.models;

import lombok.Value;

@Value
public class CharacteristicToDisease {
    private long diseaseId;
    private boolean isCharacteristic;
}

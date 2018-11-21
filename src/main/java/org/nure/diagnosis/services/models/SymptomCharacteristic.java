package org.nure.diagnosis.services.models;

import lombok.Value;

@Value
public class SymptomCharacteristic {
    private long symptomId;
    private boolean isCharacteristic;
}

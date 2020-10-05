package pl.edu.agh.casting_dss.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MechanicalProperty {
    RM("Rm"),
    RP02("Rp02"),
    HB("HB"),
    A5("A5"),
    K("K");

    @JsonValue
    private final String propertyName;

    MechanicalProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return propertyName;
    }
}

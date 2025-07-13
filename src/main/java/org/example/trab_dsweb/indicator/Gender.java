package org.example.trab_dsweb.indicator;

public enum Gender {
    MALE("Masculino"),
    FEMALE("Feminino"),
    OTHER("Outro");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
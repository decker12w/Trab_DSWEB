package org.example.trab_dsweb.indicator;

public enum JobType {
    FULL_TIME("Tempo Integral"),
    INTERNSHIP("Estágio");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
package org.example.trab_dsweb.enums;

public enum Status {
    OPEN("Aberto"),
    UNSELECTED("Não selecionado"),
    INTERVIEW("Entrevista");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
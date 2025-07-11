package org.example.trab_dsweb.indicator;

public enum Gender {
    // Para cada valor, passamos o nome de exibição para o construtor
    MALE("Masculino"),
    FEMALE("Feminino"),
    OTHER("Outro");

    // 1. Campo para armazenar o nome de exibição
    private final String displayName;

    // 2. Construtor privado para inicializar o campo
    Gender(String displayName) {
        this.displayName = displayName;
    }

    // 3. Método público (não-estático) para obter o nome de exibição
    public String getDisplayName() {
        return displayName;
    }
}